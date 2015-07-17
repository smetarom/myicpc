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
 * @author Roman Smetana
 */
@Service
public class KioskMngService {

    @Autowired
    private PublishService publishService;

    @Autowired
    private KioskContentRepository kioskContentRepository;

    @Transactional
    public void updateKioskContent(final KioskContent kioskContent, final Contest contest) {
        if (kioskContent.isActive()) {
            List<KioskContent> kioskContents = kioskContentRepository.findByContest(contest);
            for (KioskContent content : kioskContents) {
                if (!content.equals(kioskContent)) {
                    content.setActive(false);
                }
            }
            kioskContentRepository.save(kioskContents);
        }
        kioskContentRepository.save(kioskContent);
        if (kioskContent.isActive()) {
            publishService.broadcastKioskPage(contest.getCode());
        }
    }
}
