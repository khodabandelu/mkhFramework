package org.mkh.frm.service.security;

import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.service.IGenericService;

import java.util.Date;
import java.util.List;


public interface IUserService extends IGenericService<User, Integer> {

    User findByUsername(String username);

    Boolean saveGroup(int userId, List<Group> groups);

    Boolean addGroup(int userId, int groupId);

    boolean changePassword(String newPassword);

//	UserHistoryDto getUserHistory(int userId) throws SQLException;

    Integer save(User entity);

    int updateLastUserVisitDate(Date lastVisitDate);

    List<Group> getUserGroups(int userId);

//	List<OrganizationStructure> getUserPowers(int userId);

    void changeAvatar(Integer userId, String imageCode);

    List<User> listByGroupId(Integer groupId);

}
