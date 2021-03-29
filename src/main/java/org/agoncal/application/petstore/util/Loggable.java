package org.agoncal.application.petstore.util;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@InterceptorBinding
@Retention(RUNTIME)
@Target({ METHOD, TYPE })
@Documented
public @interface Loggable {
}
