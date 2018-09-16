package org.mkh.frm.service.impl;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.domain.BaseEntity;
import org.mkh.frm.service.IGenericService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public abstract class GenericService<T extends BaseEntity, PK extends Serializable> implements IGenericService<T, PK> {

    protected abstract IGenericRepository<T, PK> getGenericRepo();

    @Override
    public List<T> getAll() {
        return getGenericRepo().getAll();
    }

    @Override
    public List<T> getAll(PagingRequest searchOption) {
        return getGenericRepo().getAll(searchOption);
    }

    @Override
    public PagingResult<T> getAllGrid(PagingRequest searchOption) {
        return getGenericRepo().getAllGrid(searchOption);
    }

    @Override
    public T loadByEntityId(PK entityId) {
        return getGenericRepo().loadByEntityId(entityId);
    }

    @Override
    public T single(String where) {
        return null;
    }

    @Override
    @Transactional
    public T add(T entity) {
        return getGenericRepo().add(entity);
    }

    @Override
    @Transactional
    public void add(Set<T> entities) {
        getGenericRepo().add(entities);
    }

    @Override
    @Transactional
    public boolean delete(Set<PK> entities) {
        getGenericRepo().delete(entities);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(T entity) {
        getGenericRepo().delete(entity);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByEntityId(PK entityId) {
        getGenericRepo().deleteByEntityId(entityId);
        return true;
    }

    @Override
    @Transactional
    public T update(T entity) {
        return getGenericRepo().update(entity);
    }


//	@Override
//	public int count() {
//		return	getGenericRepo().count();
//	}

    @Override
    @Transactional
    public PK save(T entity) {
        if (entity.getId() instanceof Integer && entity.getId() != null && (Integer) entity.getId() <= 0) {
            entity.setId(null);
        } else if (entity.getId() instanceof Long && entity.getId() != null && (Long) entity.getId() <= 0) {
            entity.setId(null);
        }

        if (entity.getId() == null)
            getGenericRepo().add(entity);
        else
            getGenericRepo().update(entity);
        return (PK) entity.getId();
    }


    @Override
    @Transactional
    public T saveEntity(T entity) {
        return saveInternal(entity);
    }

    private T saveInternal(T entity) {
        if (entity.getId() instanceof Integer && entity.getId() != null && (Integer) entity.getId() <= 0) {
            entity.setId(null);
        } else if (entity.getId() instanceof Long && entity.getId() != null && (Long) entity.getId() <= 0) {
            entity.setId(null);
        }

        if (entity.getId() == null)
            return getGenericRepo().add(entity);
        else
            return getGenericRepo().update(entity);
    }

    @Transactional
    public T saveAndFlush(T entity) {
        saveInternal(entity);
        getGenericRepo().flush();
        return entity;
    }

    @Override
    @Transactional
    public boolean save(Set<T> entities) {
        for (T t : entities) {
            save(t);
        }
        return true;
    }
}
