package com.example.ming.techdemoas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.ming.techdemoas.Fragments.HomeFragment;
import com.example.ming.techdemoas.Fragments.IntroductionFragment;
import com.example.ming.techdemoas.Fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentLocator {
    private static FragmentManager fragmentManager;
    private static List<Fragment> _fragment = new ArrayList<>();

    static {
        _fragment.add(0, new IntroductionFragment());
        _fragment.add(1, new HomeFragment());
        _fragment.add(2, new SettingsFragment());
    }

    public static void setFragmentManager(FragmentManager mgr) {
        fragmentManager = mgr;

        FragmentTransaction trans = fragmentManager.beginTransaction();
        for (Fragment frag : _fragment) {
            trans.add(R.id.container, frag)
                    .hide(frag);
        }
        trans.commit();
    }

    public static Fragment Get(int sectionNumber) {
        if (sectionNumber < _fragment.size()) {
            return _fragment.get(sectionNumber);
        } else {
            return null;
        }
    }
}
