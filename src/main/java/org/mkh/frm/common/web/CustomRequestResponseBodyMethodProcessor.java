package org.mkh.frm.common.web;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.lang.reflect.Type;
import java.util.List;

public class CustomRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

   public CustomRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
      super(messageConverters);
   }

   public CustomRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters,
         ContentNegotiationManager contentNegotiationManager) {

      super(messageConverters, contentNegotiationManager);
   }

   @Override
   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
         NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
      return super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
   }

   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,NativeWebRequest webRequest, WebDataBinderFactory binderFactory, Type type) throws Exception {
      Object argument = readWithMessageConverters(webRequest, parameter, type);
      return argument;
   }

}