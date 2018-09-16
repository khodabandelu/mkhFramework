package org.mkh.frm.web.viewModel.security;

import lombok.Getter;
import lombok.Setter;
import org.mkh.frm.web.viewModel.BaseEntityViewModel;


@Getter
@Setter
public class GroupViewModel extends BaseEntityViewModel<Integer> {
	private String code;
	private String groupName;
}
