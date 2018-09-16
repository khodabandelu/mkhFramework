package org.mkh.frm.dao.security;

import org.mkh.frm.common.core.PagingRequest;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.domain.security.Action;

import java.util.List;

public interface IActionRepository extends IGenericRepository<Action,Integer> {
	
	
	List<Action> getListMenuItemByNullParentId();
	
	int loadActionIdByUrl(String url);
	
	PagingResult<Action> getAllGridList(PagingRequest searchOption);
	
	List<Action> getActionsByParentId(int parentId);

	int setEnabled(int actionId, boolean isEnabled);
	
}
