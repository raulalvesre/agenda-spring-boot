package raul.phonebook.validation;

import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class UniquenessValidator implements ConstraintValidator<Unique, String> {

    private Class<?> entity;
    private String field;

    private final EntityManager entityManager;

    public UniquenessValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(Unique unique) {
        System.out.println(Arrays.toString(unique.payload()));
        entity = unique.entity();
        field = unique.field();
    }

    @Override
    public boolean isValid(String valueToBeValidated, ConstraintValidatorContext context) {
        Assert.notNull(entityManager, "The entity manager should not be null");
        String jpql = String.format("SELECT COUNT(1) > 0 FROM %s WHERE %s = :value", entity.getSimpleName(), field);

        boolean existsInDb = entityManager
                .createQuery(jpql, Boolean.class)
                .setParameter("value", valueToBeValidated)
                .getSingleResult();

        return !existsInDb;
    }
}
