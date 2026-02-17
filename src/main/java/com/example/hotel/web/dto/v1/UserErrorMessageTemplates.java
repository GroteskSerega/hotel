package com.example.hotel.web.dto.v1;

public class UserErrorMessageTemplates {

    public static final String VALIDATE_USER_USERNAME_BLANK =
            "Поле Логин пользователя должно быть заполнено";
    public static final String VALIDATE_USER_USERNAME_INCORRECT_SIZE =
            "Поле Логин пользователя должно содержать от {min} до {max} символов";
    public static final String VALIDATE_USER_USERNAME_INCORRECT_REGEX =
            "Поле Логин пользователя должно содержать символы латиницы";

    public static final String VALIDATE_USER_PASSWORD_BLANK =
            "Поле Пароль должно быть заполнен";
    public static final String VALIDATE_USER_PASSWORD_INCORRECT_SIZE =
            "Поле Пароль должно содержать от {min} до {max} символов";

    public static final String VALIDATE_USER_EMAIL_BLANK =
            "Поле Email должно быть заполнено";
    public static final String VALIDATE_USER_EMAIL_INCORRECT_SIZE =
            "Поле Email должно содержать от {min} до {max} символов";
    public static final String VALIDATE_USER_EMAIL_INCORRECT_REGEX =
            "Поле Email должно соответствовать маске ***@***.***, где * - символ цифры или латинского алфавита";

}
