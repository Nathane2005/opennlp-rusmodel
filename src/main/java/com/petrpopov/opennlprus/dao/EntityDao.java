package com.petrpopov.opennlprus.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * User: petrpopov
 * Date: 27.11.13
 * Time: 22:52
 */

@Component
@Transactional
public class EntityDao<T> {

    @Resource
    private SessionFactory sessionFactory;

    private Class<T> domainClass;

    public EntityDao() {
    }

    public EntityDao(Class<T> domainClass) {
        this.domainClass = domainClass;
    }

    public T find(Serializable id) {
        Session sess = this.currentSession();
        T get = (T) sess.get(domainClass, id);
        return get;
    }

    private Session currentSession()
    {
        Session sess = sessionFactory.getCurrentSession();
        return sess;
    }
}
