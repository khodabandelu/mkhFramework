package org.mkh.frm.dao.security;


import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;

import java.util.List;

public interface IGroupRepository extends IGenericRepository<Group, Integer> {

    Boolean saveAction(int groupId, List<Action> actions);

    Group findByCode(String code);

    PagingResult<User> searchUserByGroupId(Integer groupId, String username, String firstName, String lastName, Integer powerId, PagingRequest searchOption);

    PagingResult<Group> listGrid(String groupName, Long actionId, PagingRequest searchOption);

    List<Group> findByUserId(Integer userId);
}
