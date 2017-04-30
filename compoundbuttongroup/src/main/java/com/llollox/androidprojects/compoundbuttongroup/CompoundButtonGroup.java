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

    // *********************************************************
    // LISTENERS
    // *********************************************************

    public interface OnButtonSelectedListener {
        void onButtonSelected(int position, boolean isChecked);
    }



    // *********************************************************
    // ENUMS
    // *********************************************************


    public enum CompoundType {
        CHECK_BOX, RADIO
    }

    public enum LabelOrder {
        BEFORE, AFTER
    }



    // *********************************************************
    // INSTANCE VARIABLES
    // *********************************************************

    private CompoundType compoundType                               = CompoundType.CHECK_BOX;
    private LabelOrder labelOrder                                   = LabelOrder.BEFORE;
    private ArrayList<FullWidthCompoundButton> buttons              = new ArrayList<>();
    private int numCols                                             = 1;

    private CharSequence[] entries;
    private OnButtonSelectedListener onButtonSelectedListener;
    private Context context;
    private LinearLayout containerLayout;





    // *********************************************************
    // CONSTRUCTOR
    // *********************************************************

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
            int compoundTypeInt = a.getInteger(R.styleable.CompoundButtonGroup_compoundType, 0);
            this.compoundType = getCompoundType(compoundTypeInt);

            int labelOrderInt = a.getInteger(R.styleable.CompoundButtonGroup_labelOrder, 0);
            this.labelOrder = getLabelOrder(labelOrderInt);

            numCols = a.getInteger(R.styleable.CompoundButtonGroup_numCols, 1);

            this.entries = a.getTextArray(R.styleable.CompoundButtonGroup_entries);
            if (entries != null) {
                reDraw();
            }
        }
        finally {
            a.recycle();
        }

        addView(containerLayout);
    }






    // *********************************************************
    // PUBLIC GETTERS AND SETTERS
    // *********************************************************

    public List<Integer> getCheckedPositions() {
        ArrayList<Integer> checked = new ArrayList<>();
        for (int i=0; i<buttons.size(); i++) {
            FullWidthCompoundButton button = buttons.get(i);
            if (button.isChecked()) {
                checked.add(i);
            }
        }
        return checked;
    }

    public CompoundType getCompoundType() {
        return compoundType;
    }

    public LabelOrder getLabelOrder() {
        return labelOrder;
    }

    public int getNumCols() {
        return numCols;
    }


    public void reDraw() {
        containerLayout.removeAllViews();
        buttons.clear();

        if (numCols == 1) {
            addEntriesInOneColumn(entries, containerLayout);
        }
        else if (numCols > 1) {
            addEntriesInGrid(entries, containerLayout, numCols);
        }
    }

    public void setCheckedPosition(final int position) {
        setCheckedPositions(new ArrayList<Integer>(){{add(position);}});
    }

    public void setCheckedPositions(List<Integer> checkedPositions) {
        for (int i=0; i<buttons.size(); i++) {
            buttons.get(i).setChecked(checkedPositions.contains(i));
        }
    }

    public void setCompoundType(CompoundType compoundType) {
        this.compoundType = compoundType;
    }

    public void setEntries (List<String> entries) {
        setEntries(entries.toArray(new CharSequence[entries.size()]));
    }

    public void setEntries(CharSequence[] entries) {
        this.entries = entries;
    }

    public void setLabelOrder(LabelOrder labelOrder) {
        this.labelOrder = labelOrder;
    }

    public void setNumCols(int numCols) {
        if (numCols > 0) {
            this.numCols = numCols;
        }
        else {
            throw new RuntimeException("Cannot set a number of cols that isn't greater than zero");
        }
    }

    public void setOnButtonSelectedListener(OnButtonSelectedListener onButtonSelectedListener) {
        this.onButtonSelectedListener = onButtonSelectedListener;
    }






    // *********************************************************
    // PRIVATE METHODS
    // *********************************************************

    private void addEntriesInOneColumn(CharSequence[] entries, LinearLayout containerLayout) {
        for (CharSequence entry : entries) {
            FullWidthCompoundButton button = buildEntry(entry.toString());
            containerLayout.addView(button);
            buttons.add(button);
        }
    }

    private void addEntriesInGrid(CharSequence[] entries, LinearLayout containerLayout, int numCols) {
        LinearLayout colContainer = null;

        for (int i=0; i<entries.length; i++) {

            if (i % numCols == 0) {
                colContainer = new LinearLayout(context);
                colContainer.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                colContainer.setOrientation(LinearLayout.HORIZONTAL);
                containerLayout.addView(colContainer);
            }

            String entry = entries[i].toString();
            FullWidthCompoundButton button = buildEntry(entry);
            colContainer.addView(button);

            buttons.add(button);
        }

        // Ugly fix to force all cells to be equally distributed on parent's width
        for (int i=0; i<entries.length % numCols; i++) {
            FullWidthCompoundButton hiddenBtn = buildEntry("hidden");
            hiddenBtn.setVisibility(INVISIBLE);
            hiddenBtn.setClickable(false);
            colContainer.addView(hiddenBtn);
        }
    }

    private FullWidthCompoundButton buildEntry(String entry) {
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

    private CompoundType getCompoundType (int compoundTypeInt) {
        switch (compoundTypeInt) {
            case 0: return CompoundType.CHECK_BOX;
            case 1: return CompoundType.RADIO;
            default: throw new RuntimeException("Unrecognized view type");
        }
    }

    private LabelOrder getLabelOrder (int labelOrder) {
        switch (labelOrder) {
            case 0: return LabelOrder.BEFORE;
            case 1: return LabelOrder.AFTER;
            default: throw new RuntimeException("Unrecognized label order");
        }
    }


    @Override
    public void onButtonClicked(View v) {
        if (compoundType == CompoundType.RADIO) {
            for (FullWidthCompoundButton button : buttons) {
                button.setChecked(false);
            }
        }

        if (onButtonSelectedListener != null) {
            boolean isChecked = !((FullWidthCompoundButton) v).isChecked();
            int position = buttons.indexOf(v);
            onButtonSelectedListener.onButtonSelected(position, isChecked);
        }
    }
}
