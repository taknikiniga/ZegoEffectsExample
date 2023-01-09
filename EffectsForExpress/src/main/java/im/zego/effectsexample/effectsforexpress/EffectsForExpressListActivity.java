package im.zego.effectsexample.effectsforexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import im.zego.effectsexample.effectsforexpress.custom_video_capture.CustomVideoCaptureActivity;
import im.zego.effectsexample.effectsforexpress.custom_video_processing.CustomVideoProcessingActivity;

/**
 * 效果选择列表页面
 */
public class EffectsForExpressListActivity extends Activity {



    public static void actionStart(Activity mainActivity) {
        Intent intent = new Intent(mainActivity, EffectsForExpressListActivity.class);
        mainActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_effects_for_express);
    }

    public void customVideoCapture(View view) {
        startActivity(CustomVideoCaptureActivity.class);
    }

    public void customVideoProcessing(View view) {
        startActivity(CustomVideoProcessingActivity.class);
    }

    private void startActivity( Class<?> cls ) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
