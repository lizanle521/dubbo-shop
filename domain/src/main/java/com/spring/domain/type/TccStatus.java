package com.spring.domain.type;

/**
 * @author Zhao Junjian
 */
public enum TccStatus {
    TRY(0), CONFIRM(1),TO_BE_CONFIRMED(2), CONFIRMED(3), CONFLICT(4), TIMEOUT(5);

    private final int status;

    TccStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
