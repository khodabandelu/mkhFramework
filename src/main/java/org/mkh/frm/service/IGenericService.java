package org.mkh.frm.service;


import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface IGenericService<T, PK extends Serializable> {
    List<T> getAll();

    List<T> getAll(PagingRequest searchOption);

    PagingResult<T> getAllGrid(PagingRequest searchOption);

    T loadByEntityId(PK entityId);

    T single(String where);

    T add(T entity);

    void add(Set<T> entities);

    boolean delete(T entity);

    boolean delete(Set<PK> entity);

    boolean deleteByEntityId(PK entityId);

    T update(T entity);

    PK save(T entity);

    T saveEntity(T entity);

    boolean save(Set<T> entity);
//	int count();
}
