package com.example.hotel.web.dto.v1;

public class HotelErrorMessageTemplates {

    public static final String VALIDATE_HOTEL_NAME_BLANK =
            "Поле Наименование (name) отеля должно быть заполнено";
    public static final String VALIDATE_HOTEL_NAME_INCORRECT_SIZE =
            "Поле Наименование (name) отеля должно содержать от {min} до {max} символов";
    public static final String VALIDATE_HOTEL_NAME_INCORRECT_REGEX =
            "Поле Наименование (name) отеля должно содержать латиницы, кириллицы и знаки препинаний";

    public static final String VALIDATE_AD_TITLE_BLANK =
            "Поле Заголовок (adTitle) объявления должно быть заполнено";
    public static final String VALIDATE_AD_TITLE_INCORRECT_SIZE =
            "Поле Заголовок (adTitle) объявления должно содержать от {min} до {max} символов";
    public static final String VALIDATE_AD_TITLE_INCORRECT_REGEX =
            "Поле Заголовок (adTitle) объявления должно содержать символы латиницы, кириллицы и знаки препинаний";

    public static final String VALIDATE_CITY_BLANK =
            "Поле Город (city) должно быть заполнено";
    public static final String VALIDATE_CITY_INCORRECT_SIZE =
            "Поле Город (city) должно содержать от {min} до {max} символов";
    public static final String VALIDATE_CITY_INCORRECT_REGEX =
            "Поле Город (city) должно содержать символы латиницы, кириллицы и знаки препинаний";

    public static final String VALIDATE_ADDRESS_BLANK =
            "Поле Адрес (address) должно быть заполнено";
    public static final String VALIDATE_ADDRESS_INCORRECT_SIZE =
            "Поле Адрес (address) должно содержать от {min} до {max} символов";
    public static final String VALIDATE_ADDRESS_INCORRECT_REGEX =
            "Поле Адрес (address) должно содержать символы латиницы, кириллицы и знаки препинаний";

    public static final String VALIDATE_RATING_BLANK =
            "Поле Рейтинг (newMark) должно быть заполнено";
    public static final String VALIDATE_RATING_INCORRECT_MIN_VALUE =
            "Поле Рейтинг (newMark) должно быть больше или равно {value}";
    public static final String VALIDATE_RATING_INCORRECT_MAX_VALUE =
            "Поле Рейтинг (newMark) должно быть меньше или равно {value}";
}
