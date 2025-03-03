package com.poliqlo.validation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @PersistenceContext
    private EntityManager entityManager;

    private String fieldName;
    private Class<?> entityClass;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityClass = constraintAnnotation.entityClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String query = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}
