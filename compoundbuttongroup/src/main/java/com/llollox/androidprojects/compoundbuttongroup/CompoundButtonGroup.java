package com.llollox.androidprojects.compoundbuttongroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigatol on 26/04/2017.
 */

public class CompoundButtonGroup extends ScrollView implements FullWidthCompoundButton.Listener {

    public interface OnButtonSelectedListener {
        void onButtonSelected(int position, boolean isChecked);
    }

    private FullWidthCompoundButton.CompoundType compoundType       = FullWidthCompoundButton.CompoundType.CHECK_BOX;
    private FullWidthCompoundButton.LabelOrder labelOrder           = FullWidthCompoundButton.LabelOrder.FIRST;
    private ArrayList<FullWidthCompoundButton> buttons              = new ArrayList<>();
    private int numCols                                             = 1;

    private OnButtonSelectedListener onButtonSelectedListener;
    private Context context;

    private LinearLayout containerLayout;

    public CompoundButtonGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        containerLayout = new LinearLayout(context);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CompoundButtonGroup, 0, 0);
        try {
            int compoundTypeInt = a.getInteger(R.styleable.CompoundButtonGroup_compound_type, 0);
            this.compoundType = getCompoundType(compoundTypeInt);

            int labelOrderInt = a.getInteger(R.styleable.CompoundButtonGroup_label_order, 0);
            this.labelOrder = getLabelOrder(labelOrderInt);

            numCols = a.getInteger(R.styleable.CompoundButtonGroup_num_cols, 1);

            CharSequence[] entries = a.getTextArray(R.styleable.CompoundButtonGroup_entries);
            if (entries != null) {
                setEntries(entries);
            }
        }
        finally {
            a.recycle();
        }

        addView(containerLayout);
    }

    private FullWidthCompoundButton.CompoundType getCompoundType (int compoundTypeInt) {
        switch (compoundTypeInt) {
            case 0: return FullWidthCompoundButton.CompoundType.CHECK_BOX;
            case 1: return FullWidthCompoundButton.CompoundType.RADIO;
            default: throw new RuntimeException("Unrecognized view type");
        }
    }

    private FullWidthCompoundButton.LabelOrder getLabelOrder (int labelOrder) {
        switch (labelOrder) {
            case 0: return FullWidthCompoundButton.LabelOrder.FIRST;
            case 1: return FullWidthCompoundButton.LabelOrder.LAST;
            default: throw new RuntimeException("Unrecognized label order");
        }
    }

    public void setEntries (List<String> entries) {
        setEntries(entries.toArray(new CharSequence[entries.size()]));
    }

    public void setOnButtonSelectedListener(OnButtonSelectedListener onButtonSelectedListener) {
        this.onButtonSelectedListener = onButtonSelectedListener;
    }

    private void setEntries(CharSequence[] entries) {
        removeAllViews();
        buttons.clear();

        LinearLayout container = null;

        for (int i=0; i<entries.length; i++) {

            if (i % numCols == 0) {
                container = new LinearLayout(context);
                container.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                container.setOrientation(LinearLayout.HORIZONTAL);
                containerLayout.addView(container);
            }

            String entry = entries[i].toString();
            FullWidthCompoundButton button = buildEntry(entry);
            container.addView(button);

            buttons.add(button);
        }

        // Ugly fix to force all cells to be equally distributed on parent's width
        for (int i=0; i<entries.length % numCols; i++) {
            FullWidthCompoundButton hiddenBtn = buildEntry("hidden");
            hiddenBtn.setVisibility(INVISIBLE);
            hiddenBtn.setClickable(false);
            container.addView(hiddenBtn);
        }
    }

    public FullWidthCompoundButton buildEntry(String entry) {
        FullWidthCompoundButton fullWidthButton = new FullWidthCompoundButton(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = (float) numCols;
        fullWidthButton.setLayoutParams(params);
        fullWidthButton.setText(entry);
        fullWidthButton.setCompoundType(compoundType);
        fullWidthButton.setLabelOrder(labelOrder);
        fullWidthButton.setListener(this);
        return fullWidthButton;
    }


    @Override
    public void onButtonClicked(View v) {
        if (compoundType == FullWidthCompoundButton.CompoundType.RADIO) {
            for (FullWidthCompoundButton button : buttons) {
                button.setChecked(false);
            }
        }

        if (onButtonSelectedListener != null) {
            boolean isChecked   = !((FullWidthCompoundButton) v).isChecked();
            int position        = buttons.indexOf(v);
            onButtonSelectedListener.onButtonSelected(position, isChecked);
        }
    }

    public List<Integer> getSelectedPositions() {
        ArrayList<Integer> checked = new ArrayList<>();
        for (int i=0; i<buttons.size(); i++) {
            FullWidthCompoundButton button = buttons.get(i);
            if (button.isChecked()) {
                checked.add(i);
            }
        }
        return checked;
    }
}
