package com.myicpc.social.service;

import com.myicpc.service.listener.ContestListener;

/**
 * Implements callbacks of {@link ContestListener}
 *
 * It calls logic inside the social module based on events sent
 * from contest lifecycle
 *
 * @author Roman Smetana
 */
public interface ContestSocialService extends ContestListener {
}
