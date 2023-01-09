package im.zego.effectsexample.effectsonly.widgets;

import android.content.Context;
import android.util.AttributeSet;

import im.zego.effectsexample.effectsonly.util.PreferenceUtil;

/**
 *
 */
public class CustomSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    String key;

    public CustomSeekBar(Context context) {
       this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        @SuppressLint({"CustomViewStyleable", "Recycle"}) TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customSeekBar);
//        key = typedArray.getString(R.styleable.customSeekBar_key);
//        // 如果当前存在key，则查询本地是否存在数据
//        if (key != null && key.length() > 0) {
//            String value = PreferenceUtil.getInstance(context).getStringValue(key, "0");
//            setProgress(Integer.parseInt(value));
//        }
//
//        super.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                listener.onProgressChanged(seekBar, progress, fromUser);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                listener.onStartTrackingTouch(seekBar);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                listener.onStopTrackingTouch(seekBar);
//                saveCurrentParameter(seekBar.getContext());
//            }
//        });
    }

    OnSeekBarChangeListener listener;

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        this.listener = l;
    }

    private void saveCurrentParameter(Context context){
        String value = String.valueOf(getProgress());
        if (!"".equals(value)) {
            PreferenceUtil.getInstance(context).setStringValue(key, value);
        }
    }


}
