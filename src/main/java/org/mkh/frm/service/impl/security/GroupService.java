package org.mkh.frm.service.impl.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.dao.security.IGroupRepository;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.service.impl.GenericService;
import org.mkh.frm.service.security.IGroupService;
import org.mkh.frm.service.security.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService extends GenericService<Group, Integer> implements IGroupService {

    @Autowired(required = true)
    private IGroupRepository groupRepository;

    @Autowired
    private IUserService iUserService;

    @Override
    protected IGenericRepository<Group, Integer> getGenericRepo() {
        return groupRepository;
    }

    @Override
    @Transactional
    public Boolean saveAction(int groupId, List<Action> actions) {
        return groupRepository.saveAction(groupId, actions);
    }


    @Override
    public PagingResult<User> searchUserByGroupId(Integer groupId, String username, String firstName, String lastName, Integer powerId, PagingRequest searchOption) {
        return groupRepository.searchUserByGroupId(groupId, username, firstName, lastName, powerId, searchOption);
    }

    @Override
    @Transactional
    public Boolean removeUserGroup(Integer groupId, Integer userId) {
        Group group = loadByEntityId(groupId);
        Boolean res = group.getUsers().remove(iUserService.loadByEntityId(userId));
        super.save(group);
        return res;
    }

    @Override
    @Transactional
    public Integer save(Group entity) {
        boolean insertMode = (entity.getId() != null && entity.getId() <= 0);
        super.save(entity);
        if (insertMode) {
            iUserService.addGroup(SecurityUtility.getAuthenticatedUserId(), entity.getId());
        }
        return entity.getId();
    }

    @Override
    public PagingResult<Group> listGrid(String groupName, Long actionId, PagingRequest searchOption) {
        return groupRepository.listGrid(groupName, actionId, searchOption);
    }

    @Override
    public Group findByCode(String code) {
        return groupRepository.findByCode(code);
    }

    @Override
    public List<Group> findByUserId(Integer userId) {
        return groupRepository.findByUserId(userId);
    }
}
