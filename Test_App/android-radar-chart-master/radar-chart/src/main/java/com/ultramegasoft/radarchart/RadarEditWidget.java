/*
 * The MIT License (MIT)
 * Copyright © 2016 Steve Guidetti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ultramegasoft.radarchart;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A widget for interacting with a RadarView.
 * <p>
 * The widget presents the user with buttons to select the previous and next item in the targeted
 * {@link RadarView} and a slider to modify the value of the selected item.
 * <p>
 * The widget also optionally displays a button bar with a Cancel and Save button.
 *
 * @author Steve Guidetti
 */
public class RadarEditWidget extends LinearLayout {
    /**
     * The target RadarView to interact with
     */
    private RadarView mRadarView;

    /**
     * The listener for button clicks
     */
    private OnButtonClickListener mListener;

    /**
     * The listener used to be notified of changes to the target RadarView
     */
    private RadarView.RadarViewListener mRadarViewListener;

    /**
     * Views from the widget's layout
     */
    private final TextView mTxtItemName;
    private final SeekBar mSeekBar;
    private final RelativeLayout mButtonBar;

    /**
     * Interface for listeners for {@link RadarEditWidget} button bar clicks.
     */
    public interface OnButtonClickListener {
        /**
         * Called when the Save button is clicked.
         */
        void onSave();

        /**
         * Called when the Cancel button is clicked.
         */
        void onCancel();
    }

    public RadarEditWidget(Context context) {
        this(context, null);
    }

    public RadarEditWidget(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.widget_radar_edit, this);

        final Resources res = getResources();
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        final int padding = (int)res.getDimension(R.dimen.rew_padding);
        setPadding(padding, padding, padding, padding);
        setBackgroundColor(0xaa000000);

        mTxtItemName = (TextView)findViewById(R.id.rew_current_item);
        mSeekBar = (SeekBar)findViewById(R.id.rew_slider);
        mButtonBar = (RelativeLayout)findViewById(R.id.rew_button_bar);

        applyAttrs(attrs);

        findViewById(R.id.rew_button_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        findViewById(R.id.rew_button_forward).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onForward();
            }
        });

        findViewById(R.id.rew_button_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
        findViewById(R.id.rew_button_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }

    public RadarEditWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public RadarEditWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }

    /**
     * Apply the XML attributes from the layout.
     *
     * @param attrs The AttributeSet from the constructor
     */
    private void applyAttrs(AttributeSet attrs) {
        final TypedArray a =
                getContext().obtainStyledAttributes(attrs, R.styleable.RadarEditWidget);

        setShowButtonBar(a.getBoolean(R.styleable.RadarEditWidget_showButtonBar, false));

        final float textSize = a.getDimension(R.styleable.RadarEditWidget_textSize, 0);
        if(textSize > 0) {
            mTxtItemName.setTextSize(textSize);
        }

        mTxtItemName.setTextColor(a.getColor(R.styleable.RadarEditWidget_textColor, 0xffffffff));

        a.recycle();
    }

    /**
     * Set up the SeekBar widget. This sets the current value and scale of the SeekBar, and creates
     * the listener for value changes.
     */
    private void setupSeekBar() {
        if(mRadarView != null) {
            final int scale = 100 / mRadarView.getMaxValue();

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) {
                        final int value = Math.round(progress / scale);
                        mRadarView.setSelectedValue(value);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            mSeekBar.setKeyProgressIncrement(scale);
            mSeekBar.setProgress(mRadarView.getSelectedValue() * scale);
            mSeekBar.setEnabled(mRadarView.isInteractive());
        } else {
            mSeekBar.setOnSeekBarChangeListener(null);
        }
    }

    /**
     * Create a listener for changes to the target RadarView.
     */
    private void setupRadarViewListener() {
        mRadarViewListener = new RadarView.RadarViewListener() {
            @Override
            public void onDataChanged(ArrayList<RadarHolder> newData) {
                if(newData == null || newData.isEmpty()) {
                    onSelectedItemChanged(0, null, 0);
                    return;
                }
                final int index = mRadarView.getSelectedIndex();
                final RadarHolder item = newData.get(index);
                onSelectedItemChanged(index, item.name, item.value);
            }

            @Override
            public void onSelectedItemChanged(int index, String name, int value) {
                setName(name);
                setValue(value);
            }

            @Override
            public void onSelectedValueChanged(int newValue) {
            }

            @Override
            public void onMaxValueChanged(int maxValue) {
                setupSeekBar();
            }

            @Override
            public void onInteractiveModeChanged(boolean interactive) {
                mSeekBar.setEnabled(interactive);
            }
        };
    }

    /**
     * Set the listener for button bar clicks.
     *
     * @param listener An {@link OnButtonClickListener} object
     */
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        mListener = listener;
    }

    /**
     * Set the target {@link RadarView} to interact with.
     *
     * @param radarView A RadarView to interact with
     */
    public void setTarget(RadarView radarView) {
        if(mRadarView != null) {
            mRadarView.removeRadarViewListener(mRadarViewListener);
        }

        mRadarView = radarView;
        setupSeekBar();

        if(radarView != null) {
            if(mRadarViewListener == null) {
                setupRadarViewListener();
            }
            radarView.addRadarViewListener(mRadarViewListener);
            setValue(radarView.getSelectedValue());
            setName(radarView.getSelectedName());
        }
    }

    /**
     * Show or hide the button bar.
     *
     * @param showButtonBar Whether to display the button bar
     */
    public void setShowButtonBar(boolean showButtonBar) {
        mButtonBar.setVisibility(showButtonBar ? VISIBLE : GONE);
    }

    /**
     * Get the current status of the button bar.
     *
     * @return Whether the button bar is showing
     */
    public boolean getShowButtonBar() {
        return mButtonBar.getVisibility() == VISIBLE;
    }

    /**
     * Set the value of the SeekBar.
     *
     * @param value The new value
     */
    private void setValue(int value) {
        if(mRadarView != null) {
            mSeekBar.setProgress((100 / mRadarView.getMaxValue()) * value);
        }
    }

    /**
     * Set the value of the name TextView.
     *
     * @param name The new value
     */
    private void setName(String name) {
        mTxtItemName.setText(name);
    }

    /**
     * Called when the back button is clicked. Tells the target RadarView to rotate clockwise.
     */
    private void onBack() {
        if(mRadarView != null) {
            mRadarView.turnCW();
        }
    }

    /**
     * Called when the forward button is clicked. Tells the target RadarView to rotate
     * counter-clockwise.
     */
    private void onForward() {
        if(mRadarView != null) {
            mRadarView.turnCCW();
        }
    }

    /**
     * Notify the listener that the Save button was clicked.
     */
    private void onSave() {
        if(mListener != null) {
            mListener.onSave();
        }
    }

    /**
     * Notify the listener that the Cancel button was clicked.
     */
    private void onCancel() {
        if(mListener != null) {
            mListener.onCancel();
        }
    }
}
