package im.zego.effectsexample.effectsonly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.midsizemango.effectsonly.R;

import im.zego.effectsexample.effectsonly.basicBeauty.BasicBeautyActivity;
import im.zego.effectsexample.effectsonly.beautifyStyle.BeautifyStyleActivity;
import im.zego.effectsexample.effectsonly.chromakey.ChromaKeyActivity;
import im.zego.effectsexample.effectsonly.facedetection.FaceDetectionActivity;
import im.zego.effectsexample.effectsonly.facedetection2.FaceDetectionActivity2;
import im.zego.effectsexample.effectsonly.filter.FilterActivity;
import im.zego.effectsexample.effectsonly.segment.SegmentActivity;
import im.zego.effectsexample.effectsonly.pendant.PendantActivity;

/**
 * 效果选择列表页面
 */
public class EffectsOnlyListActivity extends Activity {



    public static void actionStart(Activity mainActivity) {
        Intent intent = new Intent(mainActivity, EffectsOnlyListActivity.class);
        mainActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView( R.layout.activity_effectsonly);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // 基础美颜
    public void basicBeauty(View view) {
        startActivity(BasicBeautyActivity.class);
    }
    // 人脸检测
    public void faceDetection(View view) {
        startActivity(FaceDetectionActivity.class);

    }
    // 高性能人脸检测
    public void faceDetection2(View view) {
        startActivity(FaceDetectionActivity2.class);

    }
    // 绿幕分割
    public void chromaKey(View view){
       startActivity(ChromaKeyActivity.class);
    }
    // 人像分割
    public void segment(View view) {
        startActivity(SegmentActivity.class);
    }
    //贴纸挂件
    public void sticker(View view) {
        startActivity(PendantActivity.class);
    }

    public void filter(View view) {
        startActivity(FilterActivity.class);
    }

    //风格妆
    public void beautifyStyle(View view) {
        startActivity(BeautifyStyleActivity.class);
    }

    private void startActivity( Class<?> cls )
    {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
