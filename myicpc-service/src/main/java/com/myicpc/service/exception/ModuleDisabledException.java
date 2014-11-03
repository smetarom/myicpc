package com.myicpc.service.exception;

/**
 * Requested MyICPC module is not available at that time
 *
 * @author Roman Smetana
 */
public class ModuleDisabledException extends RuntimeException {
    private static final long serialVersionUID = 3405381847410719817L;

    public ModuleDisabledException() {
        super();
    }

    public ModuleDisabledException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ModuleDisabledException(final String message) {
        super(message);
    }

    public ModuleDisabledException(final Throwable cause) {
        super(cause);
    }
}
