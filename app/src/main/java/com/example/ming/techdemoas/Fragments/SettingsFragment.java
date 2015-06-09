package com.example.ming.techdemoas.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ming.techdemoas.R;
import com.example.ming.techdemoas.Services.ServiceLocator;
import com.example.ming.techdemoas.Services.WebSocketService;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingsFragment extends Fragment implements IUserFragment, Validator.ValidationListener {

    Button btnStart;
    Validator mValidator;
    private boolean isListening;
    private boolean gotIntroduction;
    private List<String> messages = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> arrayAdapter;
    @NotEmpty
    private EditText txtIpAddr;
    @NotEmpty
    private EditText txtPort;
    private onMessageListener listener = new onMessageListener();
    private onErrorListener errorListener = new onErrorListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, messages);
        listView.setAdapter(arrayAdapter);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        txtIpAddr = (EditText) view.findViewById(R.id.txtIpAddress);
        txtPort = (EditText) view.findViewById(R.id.txtPort);

        txtIpAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mValidator.validate();
            }
        });
        txtPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mValidator.validate();
            }
        });

        btnStart = (Button) view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView txtIpAddrBtm = (TextView) view.findViewById(R.id.txtIpAddressBtm);
                final TextView txtPortBtm = (TextView) view.findViewById(R.id.txtPortBtm);

                if (isListening) {
                    messages.add("Stop listening...");
                    ServiceLocator.getWebSocketService().removeOnMessageListener(listener);
                    ServiceLocator.getWebSocketService().StopListening();

                    btnStart.setText("Start listening");
                    isListening = false;
                    gotIntroduction = false;
                } else {
                    progressDialog = ProgressDialog.show(getActivity(), "Retrieving data", "Please wait...");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            messages.clear();

                            messages.add("Start listening...");

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtIpAddrBtm.setText(txtIpAddr.getText());
                                    txtPortBtm.setText(txtPort.getText());
                                    btnStart.setText("Stop Listening");
                                }
                            });

                            String ipAddr = String.valueOf(txtIpAddr.getText());
                            int port = Integer.parseInt(String.valueOf(txtPort.getText()));

                            ServiceLocator.getWebSocketService().addOnMessageListener(listener);
                            ServiceLocator.getWebSocketService().addOnErrorListener(errorListener);

                            ServiceLocator.getWebSocketService().StartListening(ipAddr, port);

                            isListening = true;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        return view;
    }

    @Override
    public void onValidationSucceeded() {
        btnStart.setEnabled(true);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }

    @Override
    public String GetTitle() {
        return "Settings";
    }

    @Override
    public void onStop() {
        super.onStop();
        ServiceLocator.getWebSocketService().StopListening();
    }

    private class onMessageListener implements WebSocketService.onMessageListener {
        @Override
        public void onMessage(String s) throws JSONException {
            if (!gotIntroduction) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                    }
                });
                gotIntroduction = true;
            }

            messages.add(Calendar.getInstance().getTime().toString());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arrayAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class onErrorListener implements WebSocketService.onErrorListener {
        @Override
        public void onError(Exception ex) {
            if (!gotIntroduction) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                    }
                });
                gotIntroduction = true;
            }

            String msg = ex.getMessage();
            if (msg == null) {
                msg = ex.toString().substring(ex.toString().lastIndexOf('.') + 1,
                        ex.toString().length());
            }

            messages.add(msg);
            messages.add("Stop listening...");
            ServiceLocator.getWebSocketService().StopListening();
            ServiceLocator.getWebSocketService().removeOnMessageListener(listener);
            ServiceLocator.getWebSocketService().removeOnErrorListener(errorListener);

            final Button btnStart = (Button) getActivity().findViewById(R.id.btnStart);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnStart.setText("Start listening");
                }
            });

            isListening = false;
            gotIntroduction = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arrayAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}