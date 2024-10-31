package com.example.prm392.common;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public interface OnFragmentNavigationListener {
    void navigateToFragment(Fragment fragment, String title, Bundle args);
}
