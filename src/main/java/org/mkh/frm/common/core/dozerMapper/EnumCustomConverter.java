package org.mkh.frm.common.core.dozerMapper;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class EnumCustomConverter implements CustomConverter {
	
	@Override
	public Object convert(Object destination, Object source, Class<?> destinationClass,    Class<?> sourceClass) {
	    if(source == null)
	        return null;
	    if(destinationClass != null){
	        if(destinationClass.getSimpleName().equalsIgnoreCase("Integer")){
	            return 1;// this.getInteger(source);
	        }else if( destinationClass.isEnum()){
	            return this.getEnum(destinationClass, source);
	        }else{
	            throw new MappingException(
	            		new StringBuilder("Converter").append(this.getClass().getSimpleName())
	                       .append(" was used incorrectly. Arguments were: ")
	                       .append(destinationClass.getClass().getName())
	                       .append(" and ")
	                       .append(source).toString());
	        }
	    }
	    return null;
	}
	private Object getString(Object object){
	    String value = object.toString();
	    return value;
	}
	
	private Object getEnum(Class<?> destinationClass, Object source){
	    Object enumeration = null;

	    Method [] ms = destinationClass.getMethods();
	    for(Method m : ms){
	        if(m.getName().equalsIgnoreCase("valueOfIndex")){
	            try {
	                enumeration = m.invoke( destinationClass.getClass(), (Integer)source);
	            }
	            catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            }
	            catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	            catch (InvocationTargetException e) {
	                e.printStackTrace();
	            }
	            return enumeration;
	        }
	    }
	    return null;
	}
	
	private Integer getInteger( Object source){//enum
		Integer enumeration = null;
	    Method [] ms = source.getClass().getMethods();
	    for(Method m : ms){
	        if(m.getName().equalsIgnoreCase("getIndex")){
	            try {
	                enumeration = (Integer)m.invoke( source);
	            }
	            catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            }
	            catch (IllegalAccessException e) {
	                e.printStackTrace();
	            }
	            catch (InvocationTargetException e) {
	                e.printStackTrace();
	            }
	            return enumeration;
	        }
	    }
	    return null;
	}
}
