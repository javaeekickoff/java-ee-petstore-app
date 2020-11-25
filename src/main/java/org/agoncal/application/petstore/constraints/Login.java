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

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Constraint(validatedBy = {})
@NotNull
@Size(min = 1, max = 10)
@ReportAsSingleViolation
@Retention(RUNTIME)
@Target({ METHOD, FIELD, PARAMETER, TYPE, ANNOTATION_TYPE, CONSTRUCTOR })
@Documented
public @interface Login {

    // ======================================
    // = Attributes =
    // ======================================

    String message() default "{org.agoncal.application.petstore.constraints.Login.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // ======================================
    // = Inner Annotation =
    // ======================================

    @Retention(RUNTIME)
    @Target({ METHOD, FIELD, PARAMETER, TYPE, ANNOTATION_TYPE, CONSTRUCTOR })
    public @interface List {
        Login[] value();
    }
}
