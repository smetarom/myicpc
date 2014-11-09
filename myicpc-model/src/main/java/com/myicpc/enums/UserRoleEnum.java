package com.myicpc.enums;

import com.myicpc.commons.enums.GeneralEnum;

/**
 * @author Roman Smetana
 */
public enum UserRoleEnum implements GeneralEnum {
    ROLE_ADMIN("user.role.admin", "ADMIN"), ROLE_USER("user.role.user", "USER");

    private String code;
    private String label;

    UserRoleEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
