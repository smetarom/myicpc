package com.myicpc.social.service;

import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.NotificationRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author Roman Smetana
 */
@Service
@Transactional
public class StaleCheckerService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void checkNotification(Long notificationId) throws IOException {
        Notification notification = notificationRepository.findOne(notificationId);
        if (notification == null) {
            return;
        }
        boolean stale = false;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            if (!stale) {
                if (!StringUtils.isEmpty(notification.getImageUrl())) {
                    stale = checkURL(httpclient, notification.getImageUrl());
                }
            }
            if (!stale) {
                if (!StringUtils.isEmpty(notification.getVideoUrl())) {
                    stale = checkURL(httpclient, notification.getVideoUrl());
                }
            }
        }

        if (stale) {
            notification.setDeleted(true);
            notificationRepository.save(notification);
        }
    }

    private boolean checkURL(CloseableHttpClient httpclient, String url) throws IOException {
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        boolean stale = false;
        try {
            response = httpclient.execute(get);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                stale = true;
            }
            EntityUtils.consume(response.getEntity());
        } finally {
            get.releaseConnection();
            if (response != null) response.close();
        }
        return stale;
    }
}
