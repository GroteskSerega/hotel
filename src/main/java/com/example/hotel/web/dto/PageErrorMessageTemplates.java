package com.example.hotel.web.dto;

public class PageErrorMessageTemplates {

    public static final String VALIDATE_PAGE_SIZE_MIN_INCORRECT =
            "Размер количества результатов в выдаче должен быть больше {value}";
    public static final String VALIDATE_PAGE_SIZE_MAX_INCORRECT =
            "Размер количества результатов в выдаче должен быть меньше {value}";

    public static final String VALIDATE_PAGE_NUMBER_INCORRECT =
            "Номер выдачи результатов должен быть целым положительным числом";
}
