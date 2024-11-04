package com.etisalat.exceptions;

/**
 *
 * @author Ahmed Tolba
 */
public class ItemNotFoundException extends RuntimeException {
    private String message;
    
    public ItemNotFoundException(String message)
    {
        this.message = message;
    }
}
