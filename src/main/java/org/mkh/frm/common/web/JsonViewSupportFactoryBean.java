package org.mkh.frm.common.web;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * Modified Spring 3.1's internal Return value handlers, and wires up a
 * decorator
 * to add support for @JsonView
 * 
 * @author martypitt
 */
@Component
public class JsonViewSupportFactoryBean implements InitializingBean {

   @Autowired
   private RequestMappingHandlerAdapter adapter;

   @Override
   public void afterPropertiesSet() {
      List<HandlerMethodReturnValueHandler> handlers = Lists.newArrayList(adapter.getReturnValueHandlers());
      List<HandlerMethodArgumentResolver> argumentResolvers = Lists.newArrayList(adapter.getArgumentResolvers());
      decorateHandlers(handlers, argumentResolvers);
      adapter.setReturnValueHandlers(handlers);
      adapter.setArgumentResolvers(argumentResolvers);
   }

   private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers,  List<HandlerMethodArgumentResolver> argumentResolvers) {
      for (HandlerMethodReturnValueHandler handler : handlers) {
         if (handler instanceof RequestResponseBodyMethodProcessor) {
            ViewInjectingReturnValueHandler decorator = new ViewInjectingReturnValueHandler(handler);
            int index = handlers.indexOf(handler);
            handlers.set(index, decorator);
            break;
         }
      }
      for (HandlerMethodArgumentResolver argument : argumentResolvers) {
         if (argument instanceof RequestResponseBodyMethodProcessor) {
            ViewInjectingArgumentValueHandler decorator = new ViewInjectingArgumentValueHandler(adapter);
            int index = argumentResolvers.indexOf(argument);
            argumentResolvers.set(index, decorator);
            break;
         }
      }
   }

}