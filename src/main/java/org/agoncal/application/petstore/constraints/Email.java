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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Constraint(validatedBy = {})
@Size(min = 5)
@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\." + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
        + "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
@ReportAsSingleViolation
@Retention(RUNTIME)
@Target({ METHOD, FIELD, PARAMETER, TYPE, ANNOTATION_TYPE, CONSTRUCTOR })
@Documented
public @interface Email {

    // ======================================
    // = Attributes =
    // ======================================

    String message() default "{org.agoncal.application.petstore.constraints.Email.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // ======================================
    // = Inner Annotation =
    // ======================================

    @Retention(RUNTIME)
    @Target({ METHOD, FIELD, PARAMETER, TYPE, ANNOTATION_TYPE, CONSTRUCTOR })
    public @interface List {
        Email[] value();
    }
}
