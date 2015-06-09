package com.example.ming.techdemoas.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ming.techdemoas.FragmentLocator;
import com.example.ming.techdemoas.MainActivity;
import com.example.ming.techdemoas.R;
import com.example.ming.techdemoas.Services.DataService;
import com.example.ming.techdemoas.Services.ServiceLocator;

public class IntroductionFragment extends Fragment implements IUserFragment {
    private boolean isInSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_introduction, container, false);

        ServiceLocator.getDataService().addOnIntroductionListener(new DataService.onIntroductionListener() {
            @Override
            public void onIntroduction(final String s) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView txtIntroduction = (TextView) view.findViewById(R.id.txtIntroduction);
                        txtIntroduction.setText(s);
                    }
                });
            }
        });

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (isInSettings) {
                    ((MainActivity) getActivity()).selectFragment(0);
                    isInSettings = false;
                } else {
                    ((MainActivity) getActivity()).selectFragment(2);
                    isInSettings = true;
                }
            }
        });

        Button btn = (Button) view.findViewById(R.id.btnGoToSettings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out,
                                R.anim.abc_fade_in, R.anim.abc_slide_out_bottom)
                        .hide(FragmentLocator.Get(0))
                        .show(FragmentLocator.Get(2))
                        .addToBackStack("Settings")
                        .commit();
            }
        });

        return view;
    }

    @Override
    public String GetTitle() {
        return "Introduction";
    }
}
