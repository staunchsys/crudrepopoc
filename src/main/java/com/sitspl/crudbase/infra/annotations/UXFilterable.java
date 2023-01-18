package com.sitspl.crudbase.infra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sitspl.crudbase.infra.enums.UIElement;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface UXFilterable {

    public String label() default "";

    public UIElement type() default UIElement.NONE;
    
    public String autoCompleteUrl() default "";
}
