package com.llollox.androidprojects.compoundbuttongroup;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
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

public class FullWidthCompoundButton extends LinearLayout {

    public interface Listener {
        void onButtonClicked(View v);
    }

    public enum LabelOrder {
        BEFORE, AFTER
    }

    public enum CompoundType {
        CHECK_BOX, RADIO
    }

    private TextView textView;
    private CompoundButton button;
    private LabelOrder labelOrder   = LabelOrder.BEFORE;
    private CompoundType viewType   = CompoundType.CHECK_BOX;
    private Context context;
    private Listener listener;

    public FullWidthCompoundButton(Context context) {
        super(context);
        init(context, null);
    }

    public FullWidthCompoundButton(Context context, @Nullable AttributeSet attrs) {
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

    public void setLabelOrder (LabelOrder labelOrder) {
        this.labelOrder = labelOrder;
        refresh();
    }

    public void setCompoundType (CompoundType viewType) {
        this.viewType = viewType;
        refresh();
    }

    public void setListener (Listener listener) {
        this.listener = listener;
    }

    public void setChecked(boolean isChecked) {
        this.button.setChecked(isChecked);
    }

    public boolean isChecked() {
        return this.button.isChecked();
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

    private void addOrderedViews(LabelOrder labelOrder) {
        switch (labelOrder) {
            case BEFORE:
                LinearLayout.LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
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
                textView.setLayoutParams(paramss);
                addView(textView);
                break;
        }
    }

    private void activateRippleEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            setBackgroundResource(outValue.resourceId);
        }
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
