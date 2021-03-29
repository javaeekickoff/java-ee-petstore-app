package org.agoncal.application.petstore.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Constraint(validatedBy = {})
@NotNull
@Size(min = 1)
@ReportAsSingleViolation
@Retention(RUNTIME)
@Target({ METHOD, FIELD, PARAMETER, TYPE, ANNOTATION_TYPE, CONSTRUCTOR })
@Documented
public @interface NotEmpty {

    // ======================================
    // = Attributes =
    // ======================================

    String message() default "{org.agoncal.application.petstore.constraints.NotEmpty.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // ======================================
    // = Inner Annotation =
    // ======================================

    @Retention(RUNTIME)
    @Target({ METHOD, FIELD, PARAMETER, TYPE, ANNOTATION_TYPE, CONSTRUCTOR })
    public @interface List {
        NotEmpty[] value();
    }
}
