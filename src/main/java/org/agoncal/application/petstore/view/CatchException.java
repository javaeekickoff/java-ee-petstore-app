package org.agoncal.application.petstore.view;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org -- Any JSF backing bean using this interceptor binding will
 * catch and display exceptions on the JSF page
 */

@InterceptorBinding
@Target({ METHOD, TYPE })
@Retention(RUNTIME)
public @interface CatchException {
}