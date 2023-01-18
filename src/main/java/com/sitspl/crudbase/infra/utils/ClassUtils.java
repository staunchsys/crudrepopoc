package com.sitspl.crudbase.infra.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClassUtils {

    public static String[] getAnnotatedDeclaredFieldsName(Class<?> clazz, Class<? extends Annotation> annotationClass, boolean recursively) {
        Field[] allFields = getDeclaredFields(clazz, recursively);
        List<String> annotatedFields = new LinkedList<String>();

        for (Field field : allFields) {
            if (field.isAnnotationPresent(annotationClass))
                annotatedFields.add(field.getName());
        }

        return annotatedFields.toArray(new String[annotatedFields.size()]);
    }
    
    public static String[] getOneToManyAnnotatedField(Class<?> clazz, boolean recursively) {
        Field[] allFields = getDeclaredFields(clazz, recursively);
        List<String> annotatedFields = new LinkedList<String>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(OneToMany.class)) {
                annotatedFields.add(field.getName());
            }
        }
        return annotatedFields.toArray(new String[annotatedFields.size()]);
    }

    /**
     * Retrieving fields list of specified class If recursively is true, retrieving
     * fields from all class hierarchy
     *
     * @param clazz       where fields are searching
     * @param recursively param
     * @return list of fields
     */
    public static Field[] getDeclaredFields(Class<?> clazz, boolean recursively) {
        List<Field> fields = new LinkedList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Collections.addAll(fields, declaredFields);

        Class<?> superClass = clazz.getSuperclass();

        if (superClass != null && recursively) {
            Field[] declaredFieldsOfSuper = getDeclaredFields(superClass, recursively);
            if (declaredFieldsOfSuper.length > 0)
                Collections.addAll(fields, declaredFieldsOfSuper);
        }

        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * Retrieving fields list of specified class If recursively is true, retrieving
     * fields from all class hierarchy
     *
     * @param clazz       where fields are searching
     * @param recursively param
     * @return list of fields
     */
    public static Map<String, Class<?>> getDeclaredEnumFieldsReccursively(Class<?> clazz, boolean recursively, boolean foreignKeyRecursively) {
        Map<String, Class<?>> enumFieldList = new HashMap<String, Class<?>>();
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            for (Field field : declaredFields) {
                boolean isForeignKey = isForeignKey(field);
                if (isForeignKey && foreignKeyRecursively) {
                    enumFieldList.putAll(getDeclaredEnumFieldsReccursively(field.getType(), true, false));
                }
                if (field.getType().isEnum()) {
                    enumFieldList.put(field.getName(), field.getType());
                }
            }
        }

        Class<?> superClass = clazz.getSuperclass();

        if (superClass != null && recursively) {
            Field[] declaredFieldsOfSuper = getDeclaredFields(superClass, recursively);
            if (declaredFieldsOfSuper != null && declaredFieldsOfSuper.length > 0) {
                for (Field superClassField : declaredFieldsOfSuper) {
                    if (superClassField.getType().isEnum()) {
                        enumFieldList.put(superClassField.getName(), superClassField.getType());
                    }
                }
            }

        }
        return enumFieldList;
    }

    /**
     * Retrieving fields list of specified class and which are annotated by incoming
     * annotation class If recursively is true, retrieving fields from all class
     * hierarchy
     *
     * @param clazz           - where fields are searching
     * @param annotationClass - specified annotation class
     * @param recursively     param
     * @return list of annotated fields
     */
    public static Field[] getAnnotatedDeclaredFields(Class<?> clazz, Class<? extends Annotation> annotationClass, boolean recursively) {
        Field[] allFields = getDeclaredFields(clazz, recursively);
        List<Field> annotatedFields = new LinkedList<Field>();

        for (Field field : allFields) {
            if (field.isAnnotationPresent(annotationClass))
                annotatedFields.add(field);
        }

        return annotatedFields.toArray(new Field[annotatedFields.size()]);
    }

    /**
     * Create new instance of specified class and type
     *
     * @param clazz of instance
     * @param <T>   type of object
     * @return new Class instance
     */
    public static <T> T getInstance(Class<T> clazz) {
        T t = null;
        try {
            try {
                t = clazz.getDeclaredConstructor().newInstance();
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                log.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage(), e);
            } catch (SecurityException e) {
                log.error(e.getMessage(), e);
            }
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }

        return t;
    }

    public static boolean isForeignKey(Field field) {
        if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
            return true;
        }
        return false;
    }
    
    public static <T> Object getMethodValue(T item, String name) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object value = name;
        if (name.contains(".")) {
            String className = name.substring(0, name.indexOf("."));
            Method superMethod = getMethod(item.getClass(), className);
            value = getMethodValue(superMethod.invoke(item), name.substring(name.indexOf(".") + 1));
        } else {
            Method method = getMethod(item.getClass(), name);
            value = method != null ? method.invoke(item) : name;
        }
        return value;
    }

    public static Method getMethod(Class<?> clazz, String name) {
        Method method = null;
        try {
            method = clazz.getMethod(SystemUtils.getInstance().buildFieldGetter(name));
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                method = getMethod(superClass, name);
            }
        }
        return method;
    }
}
