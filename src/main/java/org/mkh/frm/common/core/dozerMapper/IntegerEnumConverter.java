package org.mkh.frm.common.core.dozerMapper;

import org.apache.commons.beanutils.MethodUtils;
import org.dozer.MappingException;

public class IntegerEnumConverter extends CustomDozerConverter<Integer, Enum> {

    //private static ConcurrentMap<Class, Enum[]>  cache =new ConcurrentHashMap<>();

    public IntegerEnumConverter() {
        super(Integer.class, Enum.class);
    }

    // Method first checks exact type matches and only then checks for assignement

    @Override
    public Enum convertTo(final Integer source, final Class srcClass, final Enum destination, final Class dstClass) {
    	if(source!=null && source.intValue()>=0)
    		return getValues(dstClass)[source];
    	else
    		return null;
    }

    @Override
    public Integer convertFrom(final Enum source, final Class srcClass, final Integer destination, final Class dstClass) {
    	if(source!=null)
    		return  source.ordinal();
    	else
    		return null;
    }

    private static Enum[] getValues(final Class clazz) {
        /*Enum[]  retval = cache.get(clazz);

        //If we don't have values yet, add them to the map
        if (retval == null) {
            try {
                cache.putIfAbsent(   clazz,   retval = (Enum[]) MethodUtils.invokeStaticMethod(clazz, "values", null));
            }
            catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new MappingException("Unable to get values() from enum of type: " + clazz.getName());
            }
        }
        */
    	Enum[]  retval ;
    	try {
				retval = (Enum[]) MethodUtils.invokeStaticMethod(clazz, "values", null);
    	}
        catch (Exception e /*| IllegalAccessException | InvocationTargetException e*/) {
            throw new MappingException("Unable to get values() from enum of type: " + clazz.getName());
        }
        return  retval;
    }
}
