package org.mkh.frm.dao.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;

import java.util.Date;
import java.util.List;

public interface IUserRepository extends IGenericRepository<User, Integer> {

	User findByUsername(String username);
	
	Boolean saveGroup(int userId, List<Group> groups);

	Boolean addGroup(int userId, int groupId);

	int changePassword(int userId, String newPassword);

	int updateUser(int userId, boolean isEnabled);

	int updateLastUserVisitDate(Date lastVisitDate, int userId);

	PagingResult<User> getOnlineUsers(List<User> users, PagingRequest searchOption);

	PagingResult<User> getUserByAuthorizedOrg(Integer powerId, PagingRequest searchOption);
	
	User findOneByResetKey(String resetKey);

	List<User> listByGroupId(Integer groupId);

}
