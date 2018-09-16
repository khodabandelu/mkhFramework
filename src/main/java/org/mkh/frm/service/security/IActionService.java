package org.mkh.frm.service.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.domain.security.Action;
import org.mkh.frm.service.IGenericService;

import java.util.List;

public interface IActionService extends IGenericService<Action, Integer> {

    int loadActionIdByUrl(String url);

    PagingResult<Action> getAllGridList(PagingRequest searchOption);

    boolean setEnabled(int actionId, boolean isEnabled);

    List<Action> getAuthorizedActions(int actionId);

    List<Action> getActionsByParentId(int parentId);

}
