package org.mkh.frm.common.core.dozerMapper;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.mkh.frm.common.core.EnumEntryDescriptor;
import org.mkh.frm.common.core.PagingResult;
import org.mkh.frm.domain.BaseEntity;
import org.mkh.frm.utility.tree.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Component
public class ModelMapper {

    private static Mapper mapper;

    @Autowired(required = true)
    public ModelMapper(Mapper mapper) {
        ModelMapper.mapper = mapper;
    }

    public static Mapper getMapper() {

        if (mapper != null)
            return mapper;

        try {
            mapper = new DozerBeanMapper();
            System.out.println("not spring dozer");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            mapper = null;
        }
        return mapper;
    }

    public static <T, U> U map(T source, final Class<U> destType) {
        return map(source, destType, true);
    }

    public static <T, U> U map(T source, final Class<U> destType, Boolean isSetNullModels) {
        if (source != null) {
            U dest = getMapper().map(source, destType);
            if (isSetNullModels)
                setNullEntity(dest);
            return dest;
        } else
            return null;
    }

    public static <T> void setNullEntity(T source) {
        try {
            List<Field> fields = getAllFields(source);
            for (Field field : fields) {
                field.setAccessible(true);
                Object innerObject = field.get(source);
                if (innerObject instanceof BaseEntity<?>) {
                    Method[] methods = field.getType().getMethods();
                    for (Method method : methods) {
                        if (method.getName().contentEquals("getId")) {
                            Object invokeMethod = method.invoke(innerObject);
                            if (invokeMethod == null)
                                field.set(source, null);
                            else if (invokeMethod instanceof Integer) {
                                Integer intValue = (Integer) invokeMethod;
                                if (intValue == -1)
                                    field.set(source, null);
                            } else if (invokeMethod instanceof Long) {
                                Long longValue = (Long) invokeMethod;
                                if (longValue == -1l)
                                    field.set(source, null);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static <T> List<Field> getAllFields(T source) {
        List<Field> fields = new ArrayList<Field>();
        for (Field field : source.getClass().getDeclaredFields()) {
            fields.add(field);
        }

        if (source.getClass().getSuperclass() != null) {
            for (Field field : source.getClass().getSuperclass().getDeclaredFields())
                fields.add(field);
        }
        return fields;
    }

    public static <T> List<EnumEntryDescriptor> getEnumKeyValue(Class<T> clazz) {

        List<EnumEntryDescriptor> result = new ArrayList<EnumEntryDescriptor>();
        T[] enumConstants = clazz.getEnumConstants();
        for (T t : enumConstants) {
            EnumEntryDescriptor entry = new EnumEntryDescriptor();
            for (Method method : t.getClass().getMethods()) {
                try {
                    if (method.getName().contentEquals("ordinal")) {
                        entry.setIndex((Integer) method.invoke(t));
                    } else if (method.getName().contentEquals("name")) {
                        entry.setTitle((String) method.invoke(t));
                    } else if (method.getName().toLowerCase().contains("persiantitle")) {
                        entry.setPersianTitle((String) method.invoke(t));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            result.add(entry);
        }
        return result;
    }

    public static <T> List<EnumEntryDescriptor> getEnumKeyValue(String clazzName) {
        try {
            return getEnumKeyValue(Class.forName(clazzName));
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static <T, U> List<U> mapList(final List<T> source, final Class<U> destType) {
        final List<U> dest = new ArrayList();
        for (T element : source) {
            dest.add(getMapper().map(element, destType));
        }
        return dest;
    }

    public static <T, U> PagingResult<U> mapQueryResult(final PagingResult<T> source, final Class<U> destType) {
        final List<U> dest = new ArrayList();
        for (T element : source.getItems()) {
            dest.add(getMapper().map(element, destType));
        }
        PagingResult<U> result = new PagingResult<U>(source.getPage(), source.getTotalElements(), source.getSize(), dest);
        return result;
    }

    public static <T> TreeNode mapToTreeNode(List<T> source) {
        return new TreeNode();
    }
}
