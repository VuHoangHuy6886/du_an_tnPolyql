package com.poliqlo.validation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsIdValidator implements ConstraintValidator<ExistsId, Number> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<?> entityClass;

    @Override
    public void initialize(ExistsId constraintAnnotation) {
        this.entityClass = constraintAnnotation.entityClass();
    }

    @Override
    public boolean isValid(Number id, ConstraintValidatorContext context) {
        if (id == null) {
            return false;
        }

        String query = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e.id  = :id";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return count == 1;
    }
}
