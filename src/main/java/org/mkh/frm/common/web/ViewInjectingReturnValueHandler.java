package org.mkh.frm.common.web;

import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.common.core.dozerMapper.ModelMapper;
import org.mkh.frm.domain.BaseEntity;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@SuppressWarnings("rawtypes")
public class ViewInjectingReturnValueHandler implements HandlerMethodReturnValueHandler {


   private final HandlerMethodReturnValueHandler delegate;

   public ViewInjectingReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
      this.delegate = delegate;
   }

   @Override
   public boolean supportsReturnType(MethodParameter returnType) {
      return delegate.supportsReturnType(returnType);
   }

   @Override
   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
         NativeWebRequest webRequest) throws Exception {
	   RequestResponseView requestResponseView = returnType.getMethodAnnotation(RequestResponseView.class);
      if (requestResponseView != null) {
         if (returnValue instanceof BaseEntity) {
            returnValue = ModelMapper.map(returnValue, requestResponseView.value(),requestResponseView.setNulls());
         } else if (returnValue instanceof PagingResult) {
            returnValue = ModelMapper.mapQueryResult((PagingResult) returnValue, requestResponseView.value());
         } else if (returnValue instanceof List) {
            returnValue = ModelMapper.mapList((List) returnValue, requestResponseView.value());
         }
      }
      delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
   }


}