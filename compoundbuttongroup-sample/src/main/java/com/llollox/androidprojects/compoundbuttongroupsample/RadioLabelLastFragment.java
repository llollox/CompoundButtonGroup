package com.llollox.androidprojects.compoundbuttongroupsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lorenzorigato on 27/04/2017.
 */

public class RadioLabelLastFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        return layoutInflater.inflate(R.layout.radio_label_last_fragment, container, false);
    }
}
