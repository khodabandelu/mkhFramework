package org.mkh.frm.common.web;


import org.mkh.frm.web.viewModel.BaseEntityViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestResponseView {
   @SuppressWarnings("rawtypes") Class<? extends BaseEntityViewModel> value();
   boolean setNulls() default true;
}
