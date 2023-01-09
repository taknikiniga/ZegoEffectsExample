package im.zego.effectsexample.effectsonly.pendant;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midsizemango.effectsonly.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.zego.effectsexample.effectsonly.adapter.PendantAdapter;
import im.zego.effectsexample.effectsonly.bean.Pendant;
import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.effectsexample.effectsonly.util.ZegoUtil;
import im.zego.zegoeffectsexample.sdkmanager.SDKManager;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

public class PendantActivity extends Activity {

    private TextureView mCamera;
    private RecyclerView mRvSticker;
    private TextView mTvPendant;
    private PendantAdapter mAdapter;
    private Switch mStartCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendant);
        initView();
        initRecycleView();
        initListener();
        initCamera();

    }

    private void initView() {
        mCamera = findViewById(R.id.camera);
        mRvSticker = findViewById(R.id.rv_sticker);
        mTvPendant = findViewById(R.id.tv_pendant);
        mStartCamera = findViewById(R.id.start_camera);
    }

    private void initRecycleView() {
        mRvSticker.setLayoutManager(new GridLayoutManager(this, 5));
        mAdapter = new PendantAdapter();
        mRvSticker.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        List<Pendant> pendantList = new ArrayList<>();
        pendantList.add(new Pendant(R.mipmap.icon_no_effect,"取消",""));
        pendantList.add(new Pendant(R.mipmap.pic_thereeanmials,"三只动物","Pendants/pendantAnimal.bundle"));
        pendantList.add(new Pendant(R.mipmap.qianshui,"潜水镜","Pendants/pendantDive.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_maotou,"猫头","Pendants/pendantCat.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_watermelon,"西瓜","Pendants/pendantWatermelon.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_fawn,"小鹿","Pendants/pendantDeer.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_lianmo,"炫酷脸膜","Pendants/pendantFacefilm.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_xiaochou,"小丑","Pendants/pendantClown.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_wawaji,"娃娃机","Pendants/pendantBaby.bundle"));
        pendantList.add(new Pendant(R.mipmap.pic_meishaoniu,"美少女战士","Pendants/pendantGirl.bundle"));
        mAdapter.setData(pendantList);

    }

    private void initListener()
    {
        mStartCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DemoSDKHelp.getSDK(PendantActivity.this).setView(mCamera);
                    DemoSDKHelp.getSDK(PendantActivity.this).startCamera();
                }else {
                    DemoSDKHelp.getSDK(PendantActivity.this).stopCamera();
                }
            }
        });

        mAdapter.setOnItemClickListener(new PendantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Pendant pendant) {
                String path = PendantActivity.this.getExternalCacheDir().getPath();
                ZegoUtil.copyFileFromAssets(PendantActivity.this,pendant.getPath(),path + File.separator + pendant.getPath());

                mTvPendant.setText("当前贴纸：" + pendant.getName());

                if(TextUtils.isEmpty(pendant.getPath()))
                {
                    DemoSDKHelp.getSDK(PendantActivity.this).setPendant("");
                }else {
                    DemoSDKHelp.getSDK(PendantActivity.this).setPendant(path + File.separator + pendant.getPath());
                }
            }
        });
    }

    private void initCamera(){
        SDKManager.sharedInstance().setView(mCamera);
        mCamera.post(() -> {
            PreviewSize previewSize = new PreviewSize();
            previewSize.setHeight(mCamera.getHeight());
            previewSize.setWidth(mCamera.getWidth());
            SDKManager.sharedInstance().setPreviewSize(previewSize);
        });

        mCamera.setOpaque(false);
    }

        @Override
    protected void onDestroy() {
        DemoSDKHelp.uninit();
        DemoSDKHelp.getSDK(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mStartCamera.isChecked()) {
            SDKManager.sharedInstance().setView(mCamera);
            SDKManager.sharedInstance().startCamera();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SDKManager.sharedInstance().stopCamera();
    }
}
