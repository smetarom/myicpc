package com.myicpc.master.dao;

import com.myicpc.model.contest.Contest;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Roman Smetana
 */
@Stateless
public class ContestDao extends GeneralDao {

    public List<Contest> getActiveContests() {
        TypedQuery<Contest> query = em.createQuery("FROM Contest c", Contest.class);
        return query.getResultList();
    }
}
