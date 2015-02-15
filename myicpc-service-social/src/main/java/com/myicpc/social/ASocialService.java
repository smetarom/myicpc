package com.myicpc.social;

import com.myicpc.repository.contest.ContestRepository;
import com.myicpc.repository.social.BlacklistedUserRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.notification.NotificationService;
import com.myicpc.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Roman Smetana
 */
public abstract class ASocialService {
    @Autowired
    protected NotificationService notificationService;

    @Autowired
    protected BlacklistedUserRepository blacklistedUserRepository;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected PublishService publishService;
}
