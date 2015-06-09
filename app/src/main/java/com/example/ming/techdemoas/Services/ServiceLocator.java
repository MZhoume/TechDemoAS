package com.example.ming.techdemoas.Services;

public class ServiceLocator {
    private static WebSocketService webSocketService = new WebSocketService();
    private static DataService dataService = new DataService();

    public static WebSocketService getWebSocketService() {
        return webSocketService;
    }

    public static DataService getDataService() {
        return dataService;
    }
}
