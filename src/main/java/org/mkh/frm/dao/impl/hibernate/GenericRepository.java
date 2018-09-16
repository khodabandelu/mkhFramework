package org.mkh.frm.dao.impl.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.config.WebConfigurer;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.domain.BaseEntity;
import org.mkh.frm.security.RowFilter;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.utility.HQLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public abstract class GenericRepository<T extends BaseEntity, PK extends Serializable> implements IGenericRepository<T, PK> {

    protected Class<T> domainClass = getDomainClass();

    protected abstract Class<T> getDomainClass();

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);


    @PersistenceContext
    private EntityManager em;

    public Session getSession() {
        return em.unwrap(Session.class);
    }


    @Override
    public T loadByEntityId(PK entityId) {
        Session session = getSession();

        applyDafaultAuthorizeFilter();

        return session.get(domainClass, entityId);
    }

    @Override
    public T add(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public void add(Set<T> entities) {
        for (T t : entities) {
            em.persist(t);
        }
    }

    @Override
    public void delete(Set<PK> entities) {
        for (PK pk : entities) {
            deleteByEntityId(pk);
        }
    }

    @Override
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    public void deleteByEntityId(PK entityId) {
        T obj = em.find(domainClass, entityId);
        em.remove(obj);
    }

    @Override
    public T update(T entity) {
        return em.merge(entity);
    }

    public int count2() {
        javax.persistence.Query query = em.createQuery("select count(*) from " + domainClass.getName());
        return ((Long) query.getSingleResult()).intValue();
    }


    @Override
    public List<T> getAll() {
        Session session = getSession();
        applyDafaultAuthorizeFilter();
        Criteria criteria = session.createCriteria(domainClass.getName());
        return criteria.list();
    }

    @Override
    public List<T> getAll(PagingRequest searchOption) {
        applyDafaultAuthorizeFilter();

        StringBuffer jql = new StringBuffer("from " + domainClass.getName() + " e ");
        HQLUtility.toHQL(jql, searchOption.getSearchFilter(), searchOption.getSort());
        javax.persistence.Query query = em.createQuery(jql.toString());
        if (searchOption.getSize() > 0) {
            query.setFirstResult(searchOption.getPage() * searchOption.getSize());
            query.setMaxResults(searchOption.getSize());
        }
        return query.getResultList();
    }


    @Override
    public <U> List<U> getAll(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass, List<RowFilter> filters) {
        Session session = getSession();

        applyDafaultAuthorizeFilter();
        if (filters != null) {
            for (RowFilter rowFilter : filters) {
                applyFilter(rowFilter);
            }
        }

        StringBuffer hqlBuff = new StringBuffer(hql);
        if (searchOption != null)
            HQLUtility.toHQL(hqlBuff, searchOption.getSearchFilter(), searchOption.getSort());

        Query query = null;
        if (transformerClass != null)
            query = session.createQuery(hqlBuff.toString()).setResultTransformer(Transformers.aliasToBean(transformerClass));
        else
            query = session.createQuery(hqlBuff.toString());
        if (searchOption != null) {
            if (searchOption.getSize() > 0) {
                query.setFirstResult(searchOption.getPage() * searchOption.getSize());
                query.setMaxResults(searchOption.getSize());
            }
        }
        if (params != null)
            HQLUtility.setQueryParameters(query, params);
        return query.list();
    }

    public <U> List<U> getAll(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass) {
        return getAll(hql, params, searchOption, transformerClass, null);
    }

    @Override
    public <U> List<U> getAll(String hql, Map<String, Object> params, PagingRequest searchOption) {
        return getAll(hql, params, searchOption, null);
    }

    @Override
    public <U> List<U> getAll(String hql, Map<String, Object> params) {
        return getAll(hql, params, null, null);
    }

    @Override
    public <U> List<U> getAll(String hql, PagingRequest searchOption, Class<U> transformerClass) {
        return getAll(hql, null, searchOption, transformerClass);
    }

    @Override
    public <U> List<U> getAll(String hql, Map<String, Object> params, Class<U> transformerClass) {
        return getAll(hql, params, null, transformerClass);
    }

    @Override
    public <U> List<U> getAll(String hql, PagingRequest searchOption) {
        return getAll(hql, null, searchOption, null);
    }

    @Override
    public <U> List<U> getAll(String hql) {
        return getAll(hql, null, null, null);
    }


    @Override
    public PagingResult<T> getAllGrid(PagingRequest searchOption) {

        applyDafaultAuthorizeFilter();

        StringBuffer jql = new StringBuffer("from " + domainClass.getName() + " e ");
        HQLUtility.toHQL(jql, searchOption.getSearchFilter(), searchOption.getSort());
        javax.persistence.Query query = em.createQuery(jql.toString());
        if (searchOption.getSize() > 0) {
            query.setFirstResult(searchOption.getPage() * searchOption.getSize());
            query.setMaxResults(searchOption.getSize());
        }
        List<T> list = query.getResultList();
        StringBuffer jqlCount = new StringBuffer("select count(*)  from " + domainClass.getName() + " e ");
        HQLUtility.toHQL(jqlCount, searchOption.getSearchFilter(), "");
        int count = ((Long) em.createQuery(jqlCount.toString()).getSingleResult()).intValue();
        return new PagingResult<T>(searchOption.getPage(), count, searchOption.getSize(), list);
    }


    @Override
    public <U> PagingResult<U> getAllGridSql(String sql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass) {
        Session session = getSession();
        StringBuffer hqlBuff = new StringBuffer(" " + sql);
        if (searchOption != null)
            HQLUtility.toHQL(hqlBuff, searchOption.getSearchFilter(), searchOption.getSort());

        Query query = null;
        if (transformerClass != null)
            query = session.createSQLQuery(hqlBuff.toString()).setResultTransformer(Transformers.aliasToBean(transformerClass));
        else
            query = session.createSQLQuery(hqlBuff.toString());
        if (searchOption != null) {
            if (searchOption.getSize() > 0) {
                query.setFirstResult(searchOption.getPage() * searchOption.getSize());
                query.setMaxResults(searchOption.getSize());
            }
        }
        if (params != null)
            HQLUtility.setQueryParameters(query, params);

        List<U> list = query.list();

        Query countQuery = session.createSQLQuery("select count(*) " + HQLUtility.retriveCountQueryFromHql(hqlBuff));

        if (params != null)
            HQLUtility.setQueryParameters(countQuery, params);

        int count = ((java.math.BigDecimal) countQuery.uniqueResult()).intValue();
        if (searchOption != null)
            return new PagingResult<U>(searchOption.getPage(), count, searchOption.getSize(), list);
        else
            return new PagingResult<U>(0, count, 0, list);
    }

    @Override
    public <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass, List<RowFilter> filters) {
        Session session = getSession();
        applyDafaultAuthorizeFilter();
        if (filters != null) {
            for (RowFilter rowFilter : filters) {
                applyFilter(rowFilter);
            }
        }
        // use this to change (from e )---to--->( from e)
        StringBuffer hqlBuff = new StringBuffer(" " + hql);
        if (searchOption != null)
            HQLUtility.toHQL(hqlBuff, searchOption.getSearchFilter(), searchOption.getSort());

        Query query = null;
        if (transformerClass != null)
            query = session.createQuery(hqlBuff.toString()).setResultTransformer(Transformers.aliasToBean(transformerClass));
        else
            query = session.createQuery(hqlBuff.toString());
        if (searchOption != null) {
            if (searchOption.getSize() > 0) {
                query.setFirstResult(searchOption.getPage() * searchOption.getSize());
                query.setMaxResults(searchOption.getSize());
            }
        }
        if (params != null)
            HQLUtility.setQueryParameters(query, params);

        List<U> list = query.list();

        Query countQuery = session.createQuery("select count(*) " + HQLUtility.retriveCountQueryFromHql(hqlBuff));

        if (params != null)
            HQLUtility.setQueryParameters(countQuery, params);

        int count = ((Long) countQuery.uniqueResult()).intValue();
        if (searchOption != null)
            return new PagingResult<U>(searchOption.getPage(), count, searchOption.getSize(), list);
        else
            return new PagingResult<U>(0, count, 0, list);
    }

    public <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass) {
        return getAllGrid(hql, params, searchOption, transformerClass, null);
    }

    @Override
    public <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, PagingRequest searchOption) {
        return getAllGrid(hql, params, searchOption, null);
    }

    @Override
    public <U> PagingResult<U> getAllGrid(String hql, PagingRequest searchOption, Class<U> transformerClass) {
        return getAllGrid(hql, null, searchOption, transformerClass);
    }

    @Override
    public <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, Class<U> transformerClass) {
        return getAllGrid(hql, params, null, transformerClass);
    }

    @Override
    public <U> PagingResult<U> getAllGrid(String hql, PagingRequest searchOption) {
        return getAllGrid(hql, null, searchOption, null);
    }

    @Override
    public <U> PagingResult<U> getAllGrid(String hql) {
        return getAllGrid(hql, null, null, null);
    }

    @Override
    public <U> U find(String hql, Map<String, Object> params, Class<U> transformerClass, List<RowFilter> filters) {
        Session session = getSession();
        applyDafaultAuthorizeFilter();
        if (filters != null) {
            for (RowFilter rowFilter : filters) {
                applyFilter(rowFilter);
            }
        }
        Query query = null;
        if (transformerClass != null)
            query = session.createQuery(hql).setResultTransformer(Transformers.aliasToBean(transformerClass));
        else
            query = session.createQuery(hql);
        if (params != null)
            HQLUtility.setQueryParameters(query, params);
        return (U) query.uniqueResult();
    }

    @Override
    public <U> U find(String hql, Map<String, Object> params, Class<U> transformerClass) {
        return this.find(hql, params, transformerClass, null);
    }

    @Override
    public <U> U find(String hql, Map<String, Object> params) {
        return this.find(hql, params, null);
    }

    @Override
    public <U> U find(String hql, Class<U> transformerClass) {
        return this.find(hql, null, transformerClass);
    }

    @Override
    public <U> U find(String hql) {
        return this.find(hql, null, null);
    }

    @Override
    public <U> List<U> getAllWithCount(String hql, Long count) {
        Session session = getSession();

        applyDafaultAuthorizeFilter();

        Query query = session.createQuery(hql);
        if (count != -1l)
            query.setMaxResults(count.intValue());
        return query.list();
    }

    public void applyDafaultAuthorizeFilter() {

        Filter filter = getSession().enableFilter("defaultFilter");
        filter.setParameter("userId", SecurityUtility.getAuthenticatedUserId());
//        filter.setParameter("orgId", SecurityUtility.getCurrentOrganizationStructure().getId());
    }

    @Override
    public void applyFilter(RowFilter filter) {
        Session session = getSession();
        if (filter.isEnabled()) {
            Filter hibernateFilter = session.enableFilter(filter.getFilterName());
            Set nameValues = filter.getFilterValues().entrySet();

            for (Iterator iterator = nameValues.iterator(); iterator.hasNext(); ) {
                Map.Entry nameValue = (Map.Entry) iterator.next();
                hibernateFilter.setParameter((String) nameValue.getKey(), nameValue.getValue());
            }
        } else {
            session.disableFilter(filter.getFilterName());
        }
    }

    @Override
    public void flush() {
        this.em.flush();
    }

}
