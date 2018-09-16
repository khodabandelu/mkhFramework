package org.mkh.frm.dao;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.security.RowFilter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGenericRepository<T, PK extends Serializable> {
    List<T> getAll();

    List<T> getAll(PagingRequest searchOption);

    PagingResult<T> getAllGrid(PagingRequest searchOption);

    <U> List<U> getAll(String hql);

    <U> List<U> getAll(String hql, PagingRequest searchOption);

    <U> List<U> getAll(String hql, Map<String, Object> params, Class<U> transformerClass);

    <U> List<U> getAll(String hql, PagingRequest searchOption, Class<U> transformerClass);

    <U> List<U> getAll(String hql, Map<String, Object> params);

    <U> List<U> getAll(String hql, Map<String, Object> params, PagingRequest searchOption);

    <U> List<U> getAll(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass);

    <U> List<U> getAll(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass, List<RowFilter> filters);

    <U> List<U> getAllWithCount(String hql, Long count);

    <U> PagingResult<U> getAllGrid(String hql);

    <U> PagingResult<U> getAllGrid(String hql, PagingRequest searchOption);

    <U> PagingResult<U> getAllGrid(String hql, PagingRequest searchOption, Class<U> transformerClass);

    <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, Class<U> transformerClass);

    <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, PagingRequest searchOption);

    <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass);

    <U> PagingResult<U> getAllGrid(String hql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass, List<RowFilter> filters);

    <U> PagingResult<U> getAllGridSql(String sql, Map<String, Object> params, PagingRequest searchOption, Class<U> transformerClass);

    <U> U find(String hql);

    <U> U find(String hql, Class<U> transformerClass);

    <U> U find(String hql, Map<String, Object> params);

    <U> U find(String hql, Map<String, Object> params, Class<U> transformerClass);

    <U> U find(String hql, Map<String, Object> params, Class<U> transformerClass, List<RowFilter> filters);

    T loadByEntityId(PK entityId);

    T add(T entity);

    void add(Set<T> entities);

    void delete(Set<PK> entities);

    void delete(T entity);

    void deleteByEntityId(PK entityId);

    T update(T entity);

    void applyDafaultAuthorizeFilter();

    void applyFilter(RowFilter filter);

    void flush();
}
