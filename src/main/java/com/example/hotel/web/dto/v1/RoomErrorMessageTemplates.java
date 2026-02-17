package com.example.hotel.web.dto.v1;

public class RoomErrorMessageTemplates {

    public static final String VALIDATE_ROOM_NAME_BLANK =
            "Поле Наименование (name) комнаты должно быть заполнено";
    public static final String VALIDATE_ROOM_NAME_INCORRECT_SIZE =
            "Поле Наименование (name) комнаты должно содержать от {min} до {max} символов";
    public static final String VALIDATE_ROOM_NAME_INCORRECT_REGEX =
            "Поле Наименование (name) комнаты должно содержать латиницы, кириллицы и знаки препинаний";

    public static final String VALIDATE_ROOM_DESCRIPTION_BLANK =
            "Поле Описание (description) комнаты должно быть заполнено";
    public static final String VALIDATE_ROOM_DESCRIPTION_INCORRECT_SIZE =
            "Поле Описание (description) комнаты должно содержать от {min} до {max} символов";
    public static final String VALIDATE_ROOM_DESCRIPTION_INCORRECT_REGEX =
            "Поле Описание (description) комнаты должно содержать латиницы, кириллицы и знаки препинаний";

    public static final String VALIDATE_ROOM_ROOM_NUMBER_BLANK =
            "Поле Номер (roomNumber) комнаты должно быть заполнено";
    public static final String VALIDATE_ROOM_ROOM_NUMBER_INCORRECT_SIZE =
            "Поле Номер (roomNumber) комнаты должно содержать от {min} до {max} символов";
    public static final String VALIDATE_ROOM_ROOM_NUMBER_INCORRECT_REGEX =
            "Поле Номер (roomNumber) комнаты должно содержать латиницы, кириллицы и знаки препинаний";

    public static final String VALIDATE_ROOM_CAPACITY_BLANK =
            "Поле Максимальное количество людей (capacity) должно быть заполнено";
    public static final String VALIDATE_ROOM_CAPACITY_MIN_INCORRECT =
            "Поле Максимальное количество людей (capacity) должно быть больше {min}";
    public static final String VALIDATE_ROOM_CAPACITY_MAX_INCORRECT =
            "Поле Максимальное количество людей (capacity) должно быть больше {max}";
}
