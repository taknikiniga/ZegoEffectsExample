package im.zego.effectsexample.effectsforexpress.widgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import im.zego.effectsexample.effectsforexpress.R;


public class CustomMinSeekBar2 extends RelativeLayout {


    private int min = 0;
    private int max = 100;
    private String title = "";
    private TextView titleView, minView, maxView, value;
    private SeekBar seekBar;
    private int currentProgress = 0;
    private Context context;
    private Switch sSwitch;

    public CustomMinSeekBar2(Context context) {
        this(context, null);
    }

    public CustomMinSeekBar2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMinSeekBar2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        this.context = context;
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customSeekBar);
        View view = inflate(context, R.layout.custom_seek_bar_layout, this);
        String title = typedArray.getString(R.styleable.customSeekBar_title);
        String minStr = typedArray.getString(R.styleable.customSeekBar_minStr);
        String maxStr = typedArray.getString(R.styleable.customSeekBar_maxStr);
        final String describe = typedArray.getString(R.styleable.customSeekBar_describe);
        final String currentProgressStr = typedArray.getString(R.styleable.customSeekBar_currentStr);
        final boolean switchVisibility = typedArray.getBoolean(R.styleable.customSeekBar_switchVisibility,true);

        seekBar = view.findViewById(R.id.seek_bar);
        titleView = view.findViewById(R.id.title);
        value = view.findViewById(R.id.value);
        sSwitch = view.findViewById(R.id.sSwitch);

        if (title != null && !"".equals(title)) {
            titleView.setText(title);
        }

        if ("".equals(describe) || describe == null) {

        }


        minView = view.findViewById(R.id.min);
        if (minStr != null && !"".equals(minStr)) {
            min = Integer.parseInt(minStr);
        }

        maxView = view.findViewById(R.id.max);
        if (maxStr != null && !"".equals(maxStr)) {
            max = Integer.parseInt(maxStr);
        }
        setRealSeekBar(min, max);

        sSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (seekBarChangeListener != null) {
                    seekBarChangeListener.onCheckedChanged(CustomMinSeekBar2.this, isChecked);
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress = (progress + min);
                value.setText(String.valueOf(currentProgress));
                if (seekBarChangeListener != null) {
                    seekBarChangeListener.onProgressChanged(CustomMinSeekBar2.this, currentProgress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                ((Activity)getContext()).getWindow().getDecorView().clearFocus();

                if (seekBarChangeListener != null) {
                    seekBarChangeListener.onStartTrackingTouch(CustomMinSeekBar2.this, currentProgress);
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBarChangeListener != null) {
                    seekBarChangeListener.onStopTrackingTouch(CustomMinSeekBar2.this, currentProgress);
                }
            }
        });


        setCurrentValue(Integer.parseInt(currentProgressStr));

        if(!switchVisibility)
        {
            setSwitchVisibility(View.GONE);
        }

    }

    public void setRealSeekBar(int min, int max) {
        minView.setText(min + "");
        maxView.setText(max + "");
        seekBar.setMax(max - min);

    }

    public static class OnSeekBarChangeListener {
        public void onProgressChanged(View view, int progress, boolean fromUser) {
        }

        public void onStartTrackingTouch(View view, int progress) {
        }


        public void onStopTrackingTouch(View view, int progress) {
        }

        public void onCheckedChanged(View view, boolean isChecked) {
        }
    }

    private OnSeekBarChangeListener seekBarChangeListener;

    public synchronized void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        this.seekBarChangeListener = listener;
    }


    public void setCurrentValue(int currentProgress) {
        this.currentProgress = currentProgress;
        seekBar.setProgress(currentProgress - min);

    }

    public void setMin(int min) {
        this.min = min;
        setRealSeekBar(this.min, max);
        setCurrentValue(this.currentProgress);
    }

    public void setMax(int max) {
        this.max = max;
        setRealSeekBar(this.min, max);
        setCurrentValue(this.currentProgress);
    }

    public int getCurrentValue() {
        return currentProgress;
    }


    public void setSwitch(boolean isCheck)
    {
        sSwitch.setChecked(isCheck);
    }


    public void setSwitchVisibility(int visibility)
    {
        sSwitch.setVisibility(visibility);
    }


}