package com.example.hotel.aop;

public class AspectMessagesTemplates {

    public static final String CALL_OPERATION =
            "\nCheck operation:\n username: {},\n nameMethod: {},\n params: {},\n args: {}\n";

    public static final String TEMPLATE_OPERATION_FORBIDDEN = "Операция недоступна";

    public static final String TEMPLATE_OPERATION_UNAUTHORIZED = "Пользователь не аутентифицирован";
}
