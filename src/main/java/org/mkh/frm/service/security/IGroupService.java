package org.mkh.frm.service.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.service.IGenericService;

import java.util.List;

public interface IGroupService extends IGenericService<Group, Integer> {

    Boolean saveAction(int groupId, List<Action> actions);

    Group findByCode(String code);

    PagingResult<User> searchUserByGroupId(Integer groupId, String username, String firstName, String lastName, Integer powerId, PagingRequest searchOption);

    Boolean removeUserGroup(Integer groupId, Integer userId);

    PagingResult<Group> listGrid(String groupName, Long actionId, PagingRequest searchOption);

    List<Group> findByUserId(Integer userId);

}
