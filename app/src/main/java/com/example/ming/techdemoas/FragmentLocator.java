package com.example.ming.techdemoas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.ming.techdemoas.Fragments.HomeFragment;
import com.example.ming.techdemoas.Fragments.IntroductionFragment;
import com.example.ming.techdemoas.Fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentLocator {
    private static FragmentManager fragmentManager;
    private static List<Fragment> _fragment = new ArrayList<>();
    private static boolean[] _hasAdded;

    static {
        _fragment.add(0, new IntroductionFragment());
        _fragment.add(1, new HomeFragment());
        _fragment.add(2, new SettingsFragment());

        _hasAdded = new boolean[_fragment.size()];
    }

    public static void setFragmentManager(FragmentManager mgr) {
        fragmentManager = mgr;
    }

    public static Fragment Get(int sectionNumber) {
        if (sectionNumber < _fragment.size()) {
            if (!_hasAdded[sectionNumber]) {
                fragmentManager.beginTransaction()
                        .add(R.id.container, _fragment.get(sectionNumber))
                        .hide(_fragment.get(sectionNumber))
                        .commit();
                _hasAdded[sectionNumber] = true;
            }

            return _fragment.get(sectionNumber);
        } else {
            return null;
        }
    }

    public static void InvalidateAll() {
        for (int i = 0; i < _hasAdded.length; i++) {
            _hasAdded[i] = false;
        }
    }
}
