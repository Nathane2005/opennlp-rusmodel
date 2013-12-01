package com.petrpopov.opennlprus.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    public Long count() {
        Criteria criteria = currentSession().createCriteria(domainClass);
        Number number = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();
        return number.longValue();
    }

    public List<T> findAll()
    {
        return findAllObjects(domainClass);
    }

    protected List<T> findAllObjects(Class clazz)
    {
        Session sess = this.currentSession();

        Query namedQuery = sess.createQuery("select p from " + clazz.getName() + " p");
        namedQuery.setCacheable(true);
        List<T> list = namedQuery.list();

        return list;
    }

    public T find(Serializable id) {
        Session sess = this.currentSession();
        T get = (T) sess.get(domainClass, id);
        return get;
    }

    public T save(T obj) {
        this.currentSession().saveOrUpdate(obj);
        return obj;
    }

    public List<T> findByQuery(String querySource, String name, Object param)
    {
        Query query = createQuery(querySource);
        query.setParameter(name, param);

        List<T> list = query.list();
        return list;
    }

    public List<T> findByQuery(String querySource, Map<String, Object> params)
    {
        Query query = createQuery(querySource);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<T> list = query.list();
        return list;
    }

    private Query createQuery(String querySource)
    {
        Session sess = this.currentSession();

        Query query = sess.createQuery(querySource);
        query.setCacheable(true);

        return query;
    }


    private Session currentSession()
    {
        Session sess = sessionFactory.getCurrentSession();
        return sess;
    }
}
