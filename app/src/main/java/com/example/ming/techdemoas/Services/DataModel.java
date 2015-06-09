package com.example.ming.techdemoas.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataModel {
    private long rowid;
    private int serverID;
    private String[] names;
    private double[] values;

    public DataModel(JSONObject json) throws JSONException {
        rowid = json.getLong("rowid");
        serverID = json.getInt("ServerID");

        JSONArray namesArr = json.getJSONArray("Names");
        names = new String[namesArr.length()];
        for (int i = 0; i < namesArr.length(); i++) {
            getNames()[i] = namesArr.getString(i);
        }

        JSONArray valueArr = json.getJSONArray("Values");
        values = new double[valueArr.length()];
        for (int i = 0; i < valueArr.length(); i++) {
            getValues()[i] = valueArr.getDouble(i);
        }
    }

    public long getRowid() {
        return rowid;
    }

    public int getServerID() {
        return serverID;
    }

    public String[] getNames() {
        return names;
    }

    public double[] getValues() {
        return values;
    }
}
