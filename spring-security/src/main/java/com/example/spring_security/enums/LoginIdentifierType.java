package com.example.spring_security.enums;


import java.util.regex.Pattern;

public enum LoginIdentifierType {

    EMAIL("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"),
    PHONE("^[6-9]\\d{9}$"),
    USERNAME("^[A-Za-z0-9_]{3,20}$");

    private final Pattern pattern;

    LoginIdentifierType(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String input) {
        return pattern.matcher(input).matches();
    }

    public static LoginIdentifierType from(String input) {

        for (LoginIdentifierType type : values()) {
            if (type.matches(input)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid login identifier format");
    }
}