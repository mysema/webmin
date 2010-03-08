package com.mysema.webmin;

/**
 * MinifierException provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MinifierException extends RuntimeException {

    private static final long serialVersionUID = -7583412721915563759L;

    public MinifierException(String message) {
        super(message);
    }

    public MinifierException(Throwable cause) {
        super(cause);
    }

    public MinifierException(String message, Throwable cause) {
        super(message, cause);
    }

}
