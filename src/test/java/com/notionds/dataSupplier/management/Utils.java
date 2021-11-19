package com.notionds.dataSupplier.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notionds.dataSupplier.NotionStartupException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Supplier;

public class Utils {

    private static final String TYPE_NAME_PREFIX = "class ";

    public static ObjectMapper objectMapper = new ObjectMapper();
    public static String getClassName(Type type) {
        if (type==null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    public static Class<?> getClass(Type type)
            throws ClassNotFoundException {
        String className = getClassName(type);
        if (className==null || className.isEmpty()) {
            return null;
        }
        return Class.forName(className);
    }

    public static Type[] getParameterizedTypes(Object object) {
        Type superclassType = object.getClass().getGenericSuperclass();
        if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
            return null;
        }
        return ((ParameterizedType)superclassType).getActualTypeArguments();
    }

    @SuppressWarnings("unchecked")
    public <X, O, IL> Supplier<X> invokeLibSupplier(Class<X> clazz, O options, UUID connectionId, IL invokeLibrary) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(UUID.class);
            return () -> {
                try {
                    return (X) constructor.newInstance(options, connectionId, invokeLibrary);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
        }
    }
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> uuIdSupplier(Class<T> clazz, UUID connectionId) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(UUID.class);
            return () -> {
                try {
                    return (T) constructor.newInstance(connectionId);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotionStartupException(NotionStartupException.Type.ReflectiveOperationFailed, this.getClass());
        }
    }

}
