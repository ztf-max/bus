package com.qt.bus.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    USER("USER"),
    DRIVER("DRIVER");

    private final String value;
}
