package com.example.hotel.service;

public class MessageTemplates {
    public static final String TEMPLATE_HOTEL_NOT_FOUND_EXCEPTION =
            "Отель с ID {0} не найден!";

    public static final String TEMPLATE_ROOM_NOT_FOUND_EXCEPTION =
            "Комната с ID {0} не найдена!";

    public static final String TEMPLATE_USER_NOT_FOUND_EXCEPTION =
            "Пользователь с ID {0} не найдена!";

    public static final String TEMPLATE_USER_ALREADY_EXISTS_EXCEPTION =
            "Пользователь с username {0} и email {1} уже существует!";

    public static final String TEMPLATE_BOOKING_NOT_FOUND_EXCEPTION =
            "Бронирование с ID {0} не найдена!";

    public static final String TEMPLATE_BOOKING_DATES_UNAVAILABLE_EXCEPTION =
            "Доступные даты для комнаты ID {0} с {1} по {2} недоступны!";
}
