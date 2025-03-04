package com.poliqlo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String message() default "Giá trị đã tồn tại trong hệ thống";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fieldName();

    Class<?> entityClass();
}
