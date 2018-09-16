package org.mkh.frm.dao.impl.hibernate.security;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.impl.hibernate.GenericRepository;
import org.mkh.frm.dao.security.IUserRepository;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.security.RowFilter;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.utility.HQLUtility;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository extends GenericRepository<User, Integer> implements IUserRepository {

    @Override
    protected Class<User> getDomainClass() {
        return User.class;
    }


    @Override
    public User findByUsername(String username) {

        Session session = getSession();
        String hql = " 	select u "
                + "	  	from User u "
                + "			left join fetch u.organizationStructure  "
                + "	  	where  lower(u.username)=:username";
        Query query = session.createQuery(hql);
        query.setParameter("username", username.toLowerCase());
        return (User) query.uniqueResult();
    }


    public Boolean addGroup(int userId, int groupId) {
        Session session = getSession();
        User user = session.load(User.class, userId);
        user.getGroups().add(new Group(groupId));
        session.update(user);
        return true;
    }


    @Override
    public Boolean saveGroup(int userId, List<Group> groups) {
        Session session = getSession();
        User user = session.load(User.class, userId);
        user.getGroups().clear();
        for (Group group : groups) {
            user.getGroups().add(group);
        }
        session.update(user);
        return true;
    }

    @Override
    public int changePassword(int userId, String newPassword) {
        Session session = getSession();
        String hql = "update User set password = :newPassword where id = :userId ";
        Query query = session.createQuery(hql);
        query.setParameter("newPassword", newPassword);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }


    @Override
    public User update(User entity) {
        Session session = getSession();
        User user = session.load(User.class, entity.getId());
        return super.update(entity);
    }


    @Override
    public int updateUser(int userId, boolean isEnabled) {
        String hql = "update User set enabled= :isEnabled where id = :userId ";
        Query query = getSession().createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("isEnabled", isEnabled);
        return query.executeUpdate();
    }


    @Override
    public int updateLastUserVisitDate(Date lastVisitDate, int userId) {
        String jql = "update User set lastVisitDate= :lastVisitDate where id = :userId ";
        Query query = getSession().createQuery(jql);
        query.setParameter("lastVisitDate", lastVisitDate);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

    @Override
    public PagingResult<User> getAllGrid(PagingRequest searchOption) {

        String hqlQuery = "from " + domainClass.getName() + " e ";

        Map<String, Object> filterParams = new HashMap<String, Object>();
        filterParams.put("userId", SecurityUtility.getAuthenticatedUserId());

        List<RowFilter> filters = new ArrayList<RowFilter>();
//        if (!SecurityUtility.isInRole("ROLE_ADMIN")) {
//            filters.add(new RowFilter("userAuthorize", filterParams));
//        }
        return super.getAllGrid(hqlQuery, new HashMap(), searchOption, null, filters);
    }


    @Override
    public PagingResult<User> getOnlineUsers(List<User> users, PagingRequest searchOption) {

        Map<String, Object> filterParams = new HashMap<String, Object>();
        filterParams.put("userId", SecurityUtility.getAuthenticatedUserId());

        List<RowFilter> filters = new ArrayList<RowFilter>();
        filters.add(new RowFilter("userAuthorize", filterParams));

        String hqlQuery = "from " + domainClass.getName() + " e  where e in (:users)";

        Map<String, Object> params = new HashMap();
        params.put("users", users);

        return super.getAllGrid(hqlQuery, params, searchOption, null, filters);
    }

    @Override
    public PagingResult<User> getUserByAuthorizedOrg(Integer powerId, PagingRequest searchOption) {

        String hqlQuery = " from User e " +
                " where e.id in(" +
                "		select u.id from User u" +
                "		join u.organizationStructures up " +
                "		where up.hierarchiCode like (select p.hierarchiCode from OrganizationStructure p where p.id=:powerId) || '%' )";

        Map<String, Object> params = new HashMap();
        params.put("powerId", powerId);
        return getAllGrid(hqlQuery, params, searchOption);
    }

    @Override
    public User findOneByResetKey(String resetKey) {

        Session session = getSession();
        String hql = " 	select u from User u where  resetKey=:resetKey";
        Query query = session.createQuery(hql);
        query.setParameter("resetKey", resetKey);
        return (User) query.uniqueResult();
    }

    @Override
    public List<User> listByGroupId(Integer groupId) {
        String hql = " select e from " + getDomainClass().getName() + " e join e.groups g where g.id = :groupId ";
        Map<String, Object> params = new HashMap();
        params.put("groupId", groupId);
        Query query = getSession().createQuery(hql);
        HQLUtility.setQueryParameters(query, params);
        return query.list();
    }

}
