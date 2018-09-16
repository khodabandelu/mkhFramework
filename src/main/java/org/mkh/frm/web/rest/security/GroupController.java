package org.mkh.frm.web.rest.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.common.core.dozerMapper.ModelMapper;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.service.security.IGroupService;
import org.mkh.frm.web.viewModel.security.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/security/group")
public class GroupController {

    @Autowired(required = true)
    private IGroupService groupService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PagingResult<Group> list(String groupName, Long actionId, PagingRequest searchOption) {
        return groupService.listGrid(groupName, actionId, searchOption);
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public List<Group> list() {
        return groupService.getAll();
    }

    @RequestMapping(value = "/findByAuthenticatedUser", method = RequestMethod.GET)
    public List<Group> findByAuthenticatedUser() {
        return groupService.findByUserId(SecurityUtility.getAuthenticatedUserId());
    }

    @RequestMapping(value = "/userList/{groupId}", method = RequestMethod.GET)
    public PagingResult<UserViewModel> userList(@PathVariable Integer groupId, String username, String firstName, String lastName, Integer powerId, PagingRequest searchOption) {
        return ModelMapper.mapQueryResult(groupService.searchUserByGroupId(groupId, username, firstName, lastName, powerId, searchOption), UserViewModel.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Group load(@PathVariable int id) {
        return groupService.loadByEntityId(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable int id) {
        return groupService.deleteByEntityId(id);
    }

    @RequestMapping(value = "/removeUserGroup/{groupId}/{userId}", method = RequestMethod.DELETE)
    public Boolean removeUserGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        return groupService.removeUserGroup(groupId, userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public int create(@RequestBody Group group) {
        return groupService.save(group);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public int update(@RequestBody Group group) {
        return groupService.save(group);
    }

    @RequestMapping(value = "/loadActionsXmlForTree/{groupId}", method = RequestMethod.GET)
    public Set<Action> loadActions(@PathVariable int groupId) {
        return groupService.loadByEntityId(groupId).getActions();
    }

    @RequestMapping(value = "/saveGroupActions/{groupId}", method = RequestMethod.POST)
    public Boolean saveGroupActions(@PathVariable int groupId, @RequestBody List<Action> actions) {
        return groupService.saveAction(groupId, actions);
    }

}
