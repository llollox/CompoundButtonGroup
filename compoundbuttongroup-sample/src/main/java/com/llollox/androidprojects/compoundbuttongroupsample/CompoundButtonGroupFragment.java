package com.llollox.androidprojects.compoundbuttongroupsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup;

import java.util.ArrayList;

/**
 * Created by lorenzorigato on 28/04/2017.
 */

public class CompoundButtonGroupFragment extends Fragment {

    private static class Argument {
        private static final String LAYOUT_ID = "layoutId";
    }

    public static CompoundButtonGroupFragment newInstance(int layoutId) {
        Bundle args = new Bundle();
        args.putInt(Argument.LAYOUT_ID, layoutId);
        CompoundButtonGroupFragment compoundButtonGroupFragment = new CompoundButtonGroupFragment();
        compoundButtonGroupFragment.setArguments(args);
        return compoundButtonGroupFragment;
    }

    private CompoundButtonGroup compoundButtonGroup;
    private String[] planets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int layoutId;
        if (getArguments() != null && getArguments().containsKey(Argument.LAYOUT_ID)) {
            layoutId = getArguments().getInt(Argument.LAYOUT_ID);
        }
        else {
            throw new RuntimeException("A compound button group fragment requires a layout id as argument");
        }

        planets = getResources().getStringArray(R.array.planets);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(
                R.layout.compound_button_group_fragment, container, false);

        compoundButtonGroup = (CompoundButtonGroup) layoutInflater.inflate(layoutId, relativeLayout, false);


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) compoundButtonGroup.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ABOVE, R.id.get_value_btn);

        compoundButtonGroup.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
            @Override
            public void onButtonSelected(int position, boolean isChecked) {
                String planet   = planets[position];
                String checked  = getString(isChecked ? R.string.checked : R.string.unchecked);
                Toast.makeText(getActivity(), checked + ": " + planet, Toast.LENGTH_SHORT).show();
            }
        });

        Button getValueBtn = (Button) relativeLayout.findViewById(R.id.get_value_btn);
        getValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checked = getString(R.string.checked);
                ArrayList<String> checkedPlanets = new ArrayList<>();
                for (int position : compoundButtonGroup.getSelectedPositions()) {
                    checkedPlanets.add(planets[position]);
                }
                Toast.makeText(getActivity(),
                        checked + ": " + String.valueOf(checkedPlanets),
                        Toast.LENGTH_SHORT).show();
            }
        });

        relativeLayout.addView(compoundButtonGroup, 0);

        return relativeLayout;
    }
}
