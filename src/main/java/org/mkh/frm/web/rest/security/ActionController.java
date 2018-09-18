package org.mkh.frm.web.rest.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.common.core.dozerMapper.ModelMapper;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.service.security.IActionService;
import org.mkh.frm.service.security.IGroupService;
import org.mkh.frm.utility.tree.TreeNode;
import org.mkh.frm.web.viewModel.security.ActionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/security/action")
public class ActionController {

    @Autowired(required = true)
    private IActionService actionService;

    @Autowired(required = true)
    private IGroupService groupService;


    @RequestMapping(value = "/load/{id}", method = RequestMethod.GET)
    public ActionViewModel load(@PathVariable int id) {
        return ModelMapper.map(actionService.loadByEntityId(id), ActionViewModel.class);
    }

    @RequestMapping(value = "/setEnabled/{actionId}", method = RequestMethod.GET)
    public boolean setEnabled(@PathVariable int actionId, boolean isEnabled) {
        return actionService.setEnabled(actionId, isEnabled);
    }

    @RequestMapping(value = "/listGrid", method = RequestMethod.GET)
    public PagingResult<ActionViewModel> list(
            @RequestParam PagingRequest searchOption) {
        PagingResult<Action> reauthenticateFormsList = actionService.getAllGridList(searchOption);
        return ModelMapper.mapQueryResult(reauthenticateFormsList, ActionViewModel.class);
    }

    @RequestMapping(value = "/public/listGrid", method = RequestMethod.GET)
    public PagingResult<ActionViewModel> publicList(
            @RequestParam PagingRequest searchOption) {
        PagingResult<Action> reauthenticateFormsList = actionService.getAllGridList(searchOption);
        return ModelMapper.mapQueryResult(reauthenticateFormsList, ActionViewModel.class);
    }

    /**
     * authorize enable
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public List<TreeNode> listByParentId(@PathVariable int parentId) {
        return makeTreeNode(parentId, true);
    }

    @RequestMapping(value = "/list/authorize/{parentId}", method = RequestMethod.GET)
    public List<TreeNode> listAuthorizeByParentId(@PathVariable int parentId) {
        return makeTreeNode(parentId, false);
    }

    private List<TreeNode> makeTreeNode(int parentId, boolean ignoreAuthorizeCheck) {
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        List<Action> actions = actionService.getActionsByParentId(parentId);
        for (Action action : actions) {
            if (ignoreAuthorizeCheck || (SecurityUtility.isInRole("ROLE_" + action.getId()) && action.isEnabled())) {
                TreeNode treeNode = new TreeNode(action.getId().toString(), action.getTitle());
                treeNode.addAttr("url", action.getSrc());
                nodes.add(treeNode);
                mapToTreeNode(treeNode, action, ignoreAuthorizeCheck);
            }
        }
        return nodes;
    }

    /**
     * @param node
     * @param action
     */
    private void mapToTreeNode(TreeNode node, Action action, boolean ignoreAuthorizeCheck) {
        for (Action act : action.getChilds()) {
            if (ignoreAuthorizeCheck || (SecurityUtility.isInRole("ROLE_" + act.getId()) && act.isEnabled())) {
                TreeNode treeNode = new TreeNode(act.getId().toString(), act.getTitle());
                treeNode.addAttr("url", act.getSrc());
                node.getChilds().add(treeNode);
                mapToTreeNode(treeNode, act, ignoreAuthorizeCheck);
            }
        }
    }

    @RequestMapping(value = "/getGroupActions/{groupId}", method = RequestMethod.GET)
    public List<TreeNode> list(@PathVariable int groupId) {
        Set<Action> actions = groupService.loadByEntityId(groupId).getActions();
        List<TreeNode> nodes = listByParentId(1);
        setIterateToSetChecked(nodes, new ArrayList(actions));
        return nodes;
    }

    private void setIterateToSetChecked(List<TreeNode> nodes, List<Action> actions) {
        for (TreeNode node : nodes) {
            for (Action act : actions) {
                if (act.getId().toString().equals(node.getId())) {
                    node.setChecked(true);
                    break;
                }
            }
            setIterateToSetChecked(node.getChilds(), actions);
        }
    }

}
