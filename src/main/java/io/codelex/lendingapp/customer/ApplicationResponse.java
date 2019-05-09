package io.codelex.lendingapp.customer;

import lombok.Data;

@Data
class ApplicationResponse {

    public enum Status {
        APPROVED, REJECTED
    }

    private Status status;

    ApplicationResponse(Status status) {
        this.status = status;
    }
}
