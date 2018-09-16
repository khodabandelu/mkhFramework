package org.mkh.frm.dao.impl.hibernate.security;

import org.hibernate.query.Query;
import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.impl.hibernate.GenericRepository;
import org.mkh.frm.dao.security.IActionRepository;
import org.mkh.frm.domain.security.Action;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;


@Repository
public class ActionRepository extends GenericRepository<Action,Integer> implements IActionRepository {

	@Override
	protected Class<Action> getDomainClass() {
		return Action.class;
	}
	

	@Override
	public List<Action> getListMenuItemByNullParentId() {
		return  getSession().createQuery("from " + domainClass.getName()+ " e where e.parent.id=1 and e.enabled=true order by sortOrder asc").list();
	}

	@Override
	public int loadActionIdByUrl(String url) {
		Query query = getSession().createQuery("select e.id from " + domainClass.getName()+ " e where e.src =:src");
		query.setString("src", url);
		return (Integer) query.uniqueResult();
	}

	@Override
	public PagingResult<Action> getAllGridList(PagingRequest searchOption) {
		String hqlQuery = "select e from " + domainClass.getName() + " e where e.src like :src";
		HashMap<String,Object> params=new HashMap<String,Object>();
		params.put("src", "View%");
		return getAllGrid(hqlQuery, params, searchOption);
	}

	@Override
	public int setEnabled(int actionId, boolean isEnabled) {
		Query query = getSession().createQuery("update Action set forceReAuthenticate = :forceReAuthenticate where id = :actionId ");
		query.setParameter("forceReAuthenticate", isEnabled);
		query.setParameter("actionId", actionId);
		return query.executeUpdate();
	}
	
	
	@Override
	public List<Action> getActionsByParentId(int parentId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		String hql = "select a from Action a where a.parent.id=:parentId order by a.sortOrder";
		params.put("parentId", parentId);
		return getAll(hql,params);
	}
}
