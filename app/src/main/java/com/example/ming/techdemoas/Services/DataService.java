package com.example.ming.techdemoas.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class DataService {

    private List<onIntroductionListener> listeners = new ArrayList<>();
    private List<onDataReceivedListener> dataReceivedListeners = new ArrayList<>();

    public DataService() {
        ServiceLocator.getWebSocketService().addOnMessageListener(new WebSocketService.onMessageListener() {
            @Override
            public void onMessage(String s) throws JSONException {
                try {
                    JSONObject json = new JSONObject(s);
                    String introduction = json.getString("introduction");

                    for (onIntroductionListener lis : listeners) {
                        lis.onIntroduction(introduction);
                    }
                } catch (Exception ex) {
                    JSONArray jArray = new JSONArray(s);

                    DataModel[] dms = new DataModel[jArray.length()];

                    for (int i = 0; i < jArray.length(); i++) {
                        dms[i] = new DataModel(jArray.getJSONObject(i));
                    }

                    for (onDataReceivedListener lis : dataReceivedListeners) {
                        lis.onDataReceived(dms);
                    }
                }
            }
        });
    }

    public void addOnIntroductionListener(onIntroductionListener listener) {
        listeners.add(listener);
    }

    public void addOnDataReceivedListener(onDataReceivedListener listener) {
        dataReceivedListeners.add(listener);
    }

    public void removeOnDataReceivedListener(onDataReceivedListener listener) {
        dataReceivedListeners.remove(listener);
    }

    public interface onIntroductionListener extends EventListener {
        void onIntroduction(String s);
    }

    public interface onDataReceivedListener extends EventListener {
        void onDataReceived(DataModel[] models);
    }
}
