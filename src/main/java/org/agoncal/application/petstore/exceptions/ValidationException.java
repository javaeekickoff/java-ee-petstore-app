package org.agoncal.application.petstore.exceptions;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org -- Thrown when data is not valid
 */

public class ValidationException extends RuntimeException {
    // ======================================
    // = Constructors =
    // ======================================

    private static final long serialVersionUID = 1L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }
}