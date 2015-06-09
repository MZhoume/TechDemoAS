package com.example.ming.techdemoas.Services;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class WebSocketService {

    private WebSocket webSocket;
    private List<onMessageListener> messageListeners = new ArrayList<>();
    private List<onErrorListener> errorListeners = new ArrayList<>();

    public void StartListening(String ipAddr, int port) {
        if (webSocket != null) {
            webSocket.close();
        }

        try {
            URI uri = URI.create("ws://" + ipAddr + ":" + port);
            webSocket = new WebSocket(uri);
            webSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
            for (onErrorListener lis : errorListeners) {
                lis.onError(e);
            }

            webSocket = null;
        }
    }

    public void StopListening() {
        if (webSocket != null) {
            webSocket.close();
        }

        webSocket = null;
    }

    public void addOnMessageListener(onMessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeOnMessageListener(onMessageListener listener) {
        messageListeners.remove(listener);
    }

    public void addOnErrorListener(onErrorListener listener) {
        errorListeners.add(listener);
    }

    public void removeOnErrorListener(onErrorListener listener) {
        errorListeners.remove(listener);
    }

    public interface onMessageListener extends EventListener {
        void onMessage(String s) throws JSONException;
    }

    public interface onErrorListener extends EventListener {
        void onError(Exception ex);
    }

    private class WebSocket extends WebSocketClient {
        private boolean mHasConnected;

        public WebSocket(URI serverURI) {
            super(serverURI);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            Log.d("WebSocket", "WebSocket started");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mHasConnected = true;

            sendResponse(false);
        }

        @Override
        public void onMessage(String s) {
            for (onMessageListener lis : messageListeners) {
                try {
                    lis.onMessage(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            sendResponse(false);
        }

        @Override
        public void onClose(int i, String s, boolean b) {
            Log.d("WebSocket", "WebSocket closed with " + s);

            sendResponse(true);
        }

        @Override
        public void onError(Exception e) {
            Log.d("WebSocket", "WebSocket got an error with: " + e.getMessage());
            e.printStackTrace();

            for (onErrorListener lis : errorListeners) {
                lis.onError(e);
            }

            onClose(0, null, false);
        }

        private void sendResponse(boolean isStopIntended) {
            if (!mHasConnected) {
                return;
            }

            JSONObject json = new JSONObject();
            try {
                json.put("isStopIntended", isStopIntended);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            send(json.toString());
        }
    }
}
