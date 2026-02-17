package com.example.hotel.web.dto;

public class RegexDto {

    public static final String CYRILLIC_REGEX =
            "[а-яА-Я]+";

    public static final String LATIN_REGEX =
            "[a-zA-Z]+";

    public static final String CYRILLIC_LATIN_REGEX =
            "[а-яА-Яa-zA-Z]+";

    public static final String CYRILLIC_LATIN_SPACE_REGEX =
            "[а-яА-Яa-zA-Z\\s]+";

    public static final String CYRILLIC_LATIN_SIGNS_REGEX =
            "[а-яА-Яa-zA-Z.,!?;:\\s]+";

    public static final String CYRILLIC_LATIN_DIGITS_SIGNS_REGEX =
            "[а-яА-Яa-zA-Z\\d.,!?;:\\-\\s]+";

    public static final String LATIN_DIGITS_REGEX =
            "[a-zA-Z\\d]+";

    public static final String LATIN_DIGITS_SIGNS_REGEX =
            "[a-zA-Z\\d.,!?;:\\-\\s]+";

    public static final String EMAIL_REGEX =
            "[a-zA-Z0-9._]+@[a-zA-Z0-9._]+\\.[a-zA-Z]+";

    public static final String INSTANT_REGEX =
            "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?Z$";
}
