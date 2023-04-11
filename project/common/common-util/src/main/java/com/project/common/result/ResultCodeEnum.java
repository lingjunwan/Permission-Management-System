package com.project.common.result;

import lombok.Getter;
    /**
     * Status information class for uniform return results
     *
     */
    @Getter
    public enum ResultCodeEnum {

        SUCCESS(200,"SUCCESS"),
        FAIL(201, "FAIL"),
        SERVICE_ERROR(2012, "SERVICE ERROR"),
        DATA_ERROR(204, "DATA ERROR"),
        ILLEGAL_REQUEST(205, "ILLEGAL REQUEST"),
        REPEAT_SUBMIT(206, "REPEAT SUBMIT"),
        ARGUMENT_VALID_ERROR(210, "ARGUMENT VALID ERROR"),

        LOGIN_AUTH(208, "Not logged in"),
        PERMISSION(209, "No permission"),
        ACCOUNT_ERROR(214, "ACCOUNT ERROR"),
        PASSWORD_ERROR(215, "PASSWORD ERROR"),
        LOGIN_MOBLE_ERROR( 216, "LOGIN ACCOUNT ERROR"),
        ACCOUNT_STOP( 217, "Account deactivated"),
        NODE_ERROR( 218, "There are child nodes under this node, which cannot be deleted")
        ;

        private Integer code;

        private String message;

        private ResultCodeEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
}
