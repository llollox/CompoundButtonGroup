package com.llollox.androidprojects.compoundbuttongroup;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by rigatol on 26/04/2017.
 */

class FullWidthCompoundButton extends LinearLayout {

    // *********************************************************
    // LISTENERS
    // *********************************************************


    interface Listener {
        void onButtonClicked(View v);
    }





    // *********************************************************
    // INSTANCE VARIABLES
    // *********************************************************

    private String value;
    private TextView textView;
    private CompoundButton button;
    private CompoundButtonGroup.LabelOrder labelOrder   = CompoundButtonGroup.LabelOrder.BEFORE;
    private CompoundButtonGroup.CompoundType viewType   = CompoundButtonGroup.CompoundType.CHECK_BOX;
    private Context context;
    private Listener listener;




    // *********************************************************
    // CONSTRUCTOR
    // *********************************************************

    public FullWidthCompoundButton(Context context) {
        super(context);
        init(context, null);
    }

    public FullWidthCompoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        activateRippleEffect();

        setClickable(true);
        setOrientation(HORIZONTAL);
        setPadding(20, 32, 20, 32);

        textView = new TextView(context);

        prepareButton();
        addOrderedViews(labelOrder);
    }






    // *********************************************************
    // GETTERS AND SETTERS
    // *********************************************************

    String getValue () {
        return value;
    }

    boolean isChecked() {
        return this.button.isChecked();
    }

    void setLabelOrder (CompoundButtonGroup.LabelOrder labelOrder) {
        this.labelOrder = labelOrder;
        refresh();
    }

    void setCompoundType (CompoundButtonGroup.CompoundType viewType) {
        this.viewType = viewType;
        refresh();
    }

    void setValue (String value) {
        this.value = value;
    }

    void setText(String text) {
        textView.setText(text);
    }

    void setListener (Listener listener) {
        this.listener = listener;
    }

    void setChecked(boolean isChecked) {
        this.button.setChecked(isChecked);
    }





    // *********************************************************
    // PRIVATE METHODS
    // *********************************************************


    private void activateRippleEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            setBackgroundResource(outValue.resourceId);
        }
    }

    private void addOrderedViews(CompoundButtonGroup.LabelOrder labelOrder) {
        switch (labelOrder) {
            case BEFORE:
                LinearLayout.LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
                params.weight = 1.0f;
                params.gravity = Gravity.FILL;
                textView.setLayoutParams(params);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                addView(textView);
                addView(button);
                break;

            case AFTER:
                addView(button);
                LinearLayout.LayoutParams paramss = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                paramss.gravity = Gravity.FILL;
                textView.setLayoutParams(paramss);
                addView(textView);
                break;
        }
    }

    private void prepareButton() {
        switch (viewType) {
            case CHECK_BOX:     this.button = new CheckBox(context); break;
            case RADIO:         this.button = new RadioButton(context); break;
            default:            throw new RuntimeException("Unknown View Type");
        }

        button.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setClickable(false);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButtonClicked(v);
                }

                button.setChecked(!button.isChecked());
            }
        });
    }

    private void refresh () {
        removeAllViews();
        prepareButton();
        addOrderedViews(labelOrder);
    }
}
