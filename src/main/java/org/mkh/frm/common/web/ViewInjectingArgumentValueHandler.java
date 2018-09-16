package org.mkh.frm.common.web;

import org.mkh.frm.common.core.dozerMapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

@SuppressWarnings("rawtypes")
public class ViewInjectingArgumentValueHandler implements HandlerMethodArgumentResolver {


   private RequestMappingHandlerAdapter            requestMappingHandlerAdapter;

   private CustomRequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;

   public ViewInjectingArgumentValueHandler(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
      this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
   }

   private CustomRequestResponseBodyMethodProcessor getRequestResponseBodyMethodProcessor() {

      if (requestResponseBodyMethodProcessor == null) {
         List<HttpMessageConverter<?>> messageConverters = requestMappingHandlerAdapter.getMessageConverters();
         requestResponseBodyMethodProcessor = new CustomRequestResponseBodyMethodProcessor(messageConverters);
      }
      return requestResponseBodyMethodProcessor;
   }

   @Override
   public boolean supportsParameter(MethodParameter parameter) {
      return getRequestResponseBodyMethodProcessor().supportsParameter(parameter);
   }

   @Override
   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
         NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

	  RequestResponseView reqresView = parameter.getMethodAnnotation(RequestResponseView.class);
      
      Object retValue = null;
      
      if (reqresView != null) {
    	  
         retValue = getRequestResponseBodyMethodProcessor().resolveArgument(parameter, mavContainer, webRequest,binderFactory, reqresView.value());
         retValue = ModelMapper.map(retValue, (Class) parameter.getParameterType(),reqresView.setNulls());
      } else {
         retValue = getRequestResponseBodyMethodProcessor().resolveArgument(parameter, mavContainer, webRequest,binderFactory);
      }
      return retValue;
   }

}