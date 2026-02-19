package com.example.hotel.web.interceptors;

public class LoggingTemplates {
    public static final String TEMPLATE_HTTP_REQUEST_LOGGING =
            "\nRequest:\n method: {},\n uri: {},\n query: {},\n remoteAddr: {}\n";

    public static final String TEMPLATE_HTTP_RESPONSE_LOGGING =
            "\nRequest:\n method: {},\n uri: {},\n query: {},\n remoteAddr: {}\nResponse:\n http code: {}\n";
}
