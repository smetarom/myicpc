package com.myicpc.service.kiosk;

import com.myicpc.model.contest.Contest;
import com.myicpc.model.kiosk.KioskContent;
import com.myicpc.repository.kiosk.KioskContentRepository;
import com.myicpc.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for {@link KioskContent} management
 *
 * @author Roman Smetana
 */
@Service
public class KioskMngService {
    public static final String ACTION_UPDATE_PAGE = "pageChange";
    public static final String ACTION_CHANGE_MODE = "modeChange";

    @Autowired
    private PublishService publishService;

    @Autowired
    private KioskContentRepository kioskContentRepository;

    /**
     * Process the update of {@link KioskContent}
     *
     * If {@link KioskContent} is marked as true, it deactivates all
     * all other {@link KioskContent}s
     *
     * It broadcasts the change to the live channel
     *
     * @param kioskContent updated {@link KioskContent}
     */
    @Transactional
    public void updateKioskContent(final KioskContent kioskContent) {
        if (kioskContent == null || kioskContent.getContest() == null) {
            return;
        }
        if (kioskContent.isActive()) {
            List<KioskContent> kioskContents = kioskContentRepository.findByContest(kioskContent.getContest());
            for (KioskContent content : kioskContents) {
                if (!content.equals(kioskContent)) {
                    content.setActive(false);
                }
            }
            kioskContentRepository.save(kioskContents);
        }
        kioskContentRepository.save(kioskContent);
        if (kioskContent.isActive()) {
            publishService.broadcastKioskPage(kioskContent.getContest().getCode(), ACTION_UPDATE_PAGE);
        }
    }

    /**
     * Changes the current kiosk view to the next in the row
     *
     * The chain of views is: feed -> calendar -> custom -> feed
     *
     * @param contest contest
     */
    public void changeKioskMode(final Contest contest) {
        publishService.broadcastKioskPage(contest.getCode(), ACTION_CHANGE_MODE);
    }
}
