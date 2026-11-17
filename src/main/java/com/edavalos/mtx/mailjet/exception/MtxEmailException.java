package com.edavalos.mtx.mailjet.exception;

/**
 * Exception thrown when an error occurs during email composition, configuration,
 * or transport operations.
 */
public class MtxEmailException extends RuntimeException {

    /**
     * Constructs a new {@code MtxEmailException} with the specified detail message.
     *
     * @param message the detail message
     */
    public MtxEmailException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code MtxEmailException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the underlying cause
     */
    public MtxEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code MtxEmailException} with the specified cause.
     *
     * @param cause the underlying cause
     */
    public MtxEmailException(Throwable cause) {
        super(cause);
    }
}
