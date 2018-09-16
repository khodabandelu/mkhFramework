package org.mkh.frm.service.impl.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.dao.security.IActionRepository;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.service.impl.GenericService;
import org.mkh.frm.service.security.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActionService extends GenericService<Action, Integer> implements IActionService {

    @Autowired(required = true)
    private IActionRepository actionRepository;

    @Override
    protected IGenericRepository<Action, Integer> getGenericRepo() {
        return actionRepository;
    }

    @Override
    public int loadActionIdByUrl(String url) {
        return actionRepository.loadActionIdByUrl(url);
    }

    @Override
    public PagingResult<Action> getAllGridList(PagingRequest searchOption) {
        return actionRepository.getAllGridList(searchOption);
    }

    @Override
    @Transactional
    public boolean setEnabled(int actionId, boolean isEnabled) {
        int result = actionRepository.setEnabled(actionId, isEnabled);
        return result == 1 ? true : false;
    }

    @Override
    public List<Action> getAuthorizedActions(int actionId) {

        Map<Integer, Action> mapList = new HashMap<Integer, Action>();

        User authenticatedUser = SecurityUtility.getAuthenticatedUser();
        for (Group group : authenticatedUser.getGroups()) {
            for (Action action : group.getActions()) {
                if (action.getParent() != null && action.getParent().getId().equals(actionId) && !mapList.containsKey(action.getId())) {
                    mapList.put(action.getId(), action);
                }
            }
        }
        return new ArrayList<>(mapList.values());
    }

    @Override
    public List<Action> getActionsByParentId(int parentId) {
        return actionRepository.getActionsByParentId(parentId);
    }
}
