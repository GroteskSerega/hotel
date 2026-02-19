package com.example.hotel.web.dto.v1;

public class BookingErrorMessageTemplates {
    public static final String VALIDATE_BOOKING_CHECK_IN_DATE_BLANK =
            "Поле Дата заезда (checkInDate) бронирования должно быть заполнено";

    public static final String VALIDATE_BOOKING_CHECK_IN_DATE_FUTURE =
            "Поле Дата заезда (checkInDate) бронирования должно быть заполнено датой в будущем";

    public static final String VALIDATE_BOOKING_CHECK_OUT_DATE_BLANK =
            "Поле Дата выезда (checkOutDate) бронирования должно быть заполнено";

    public static final String VALIDATE_BOOKING_CHECK_OUT_DATE_FUTURE =
            "Поле Дата выезда (checkOutDate) бронирования должно быть заполнено датой в будущем";
}
