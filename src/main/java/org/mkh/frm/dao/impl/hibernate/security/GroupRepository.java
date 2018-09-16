package org.mkh.frm.dao.impl.hibernate.security;

import org.hibernate.Session;
import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.impl.hibernate.GenericRepository;
import org.mkh.frm.dao.security.IGroupRepository;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.security.SecurityUtility;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class GroupRepository extends GenericRepository<Group, Integer> implements IGroupRepository {

	@Override
	protected Class<Group> getDomainClass() {
		return Group.class;
	}

	@Override
	public Boolean saveAction(int groupId, List<Action> actions) {
		Session session = getSession();
		Group group = session.load(Group.class, groupId);
		group.getActions().clear();
		for (Action action : actions) {
			group.getActions().add(action);
		}
		session.update(group);
		return true;
	}


	@Override
	public List<Group> getAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		String hqlQuery = " select g from Group g join g.users gu where gu.id = :userId ";
		params.put("userId", SecurityUtility.getAuthenticatedUserId());
		return super.getAll(hqlQuery, params);
	}
	@Override
	public PagingResult<User> searchUserByGroupId(Integer groupId, String username, String firstName, String lastName, Integer powerId, PagingRequest searchOption) {

		String hql = " select u from " + domainClass.getName() + " e join e.users u where e.id = :groupId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("groupId", groupId);

		if (username != null && !username.equals("")) {
			hql += " and u.username like :username ";
			params.put("username", "%" + username + "%");
		}
		if (firstName != null && !firstName.equals("")) {
			hql += " and u.firstName like :firstName ";
			params.put("firstName", "%" + firstName + "%");
		}
		if (lastName != null && !lastName.equals("")) {
			hql += " and u.lastName like :lastName ";
			params.put("lastName", "%" + lastName + "%");
		}
		if (powerId != null && powerId > 0) {
			hql += " and u.organizationStructure.id = :powerId ";
			params.put("powerId", powerId);
		}
		return getAllGrid(hql, params, searchOption);
	}

	@Override
	public PagingResult<Group> listGrid(String groupName, Long actionId, PagingRequest searchOption) {

		String hql = " from " + domainClass.getName() + " e  where 1<>2";
		
		HashMap<String, Object> params = new HashMap<String, Object>();

		if (groupName != null && groupName.trim().length() != 0) {
			hql += " and e.groupName  like :groupName ";
			params.put("groupName", "%" + groupName + "%");
		}
		if (actionId != null && actionId != -1) {
			hql += " and e.id in (select g.id from Group g join g.actions ga where ga.id=:actionId)";
			params.put("actionId", actionId);
		}
		return getAllGrid(hql, params, searchOption);
	}

	@Override
	public Group findByCode(String code) {
		String hql = " from " + domainClass.getName() + " e  where e.code=:code";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("code", code);
		return find(hql, params);
	}

	@Override
	public List<Group> findByUserId(Integer userId) {
		String hql = " select e from Group e join e.users u where u.id = :userId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return getAll(hql, params);
	}
}
