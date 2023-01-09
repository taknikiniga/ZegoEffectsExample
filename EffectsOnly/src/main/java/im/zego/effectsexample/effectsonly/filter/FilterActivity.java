package im.zego.effectsexample.effectsonly.filter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;

import im.zego.effectsexample.effectsonly.adapter.ResourceAdapter;
import im.zego.effectsexample.effectsonly.bean.Resource;
import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.effectsexample.effectsonly.util.ZegoUtil;
import im.zego.effectsexample.effectsonly.widgets.CustomMinSeekBar2;
import im.zego.zegoeffectsexample.sdkmanager.SDKManager;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

public class FilterActivity extends Activity {

    private TextureView mCamera;
    private RecyclerView mRvFilter;
    private TextView mTvPendant;
    private ResourceAdapter mAdapter;
    private Switch mStartCamera;
    private CustomMinSeekBar2 mCMSFilterEffectStrength;

    private Resource mResource = new Resource(0,"","",0);

    private HashMap<Resource,Integer> mEffectStrengthMap = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_filter);
        initView();
        initRecycleView();
        initListener();
        initCamera();
    }

    private void initView() {
        mCamera = findViewById(R.id.camera);
        mRvFilter = findViewById(R.id.rv_filter);
        mTvPendant = findViewById(R.id.tv_filter);
        mStartCamera = findViewById(R.id.start_camera);
        mCMSFilterEffectStrength = findViewById(R.id.cmsFilterEffectStrength);
    }
    private void initListener()
    {
        mStartCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DemoSDKHelp.getSDK(FilterActivity.this).setView(mCamera);
                    DemoSDKHelp.getSDK(FilterActivity.this).startCamera();
                }else {
                    DemoSDKHelp.getSDK(FilterActivity.this).stopCamera();
                }
            }
        });

        mAdapter.setOnItemClickListener(new ResourceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Resource resource) {
                String path = FilterActivity.this.getExternalCacheDir().getPath();
                ZegoUtil.copyFileFromAssets(FilterActivity.this,resource.getPath(),path + File.separator + resource.getPath());

                mResource = resource;
                mTvPendant.setText("当前滤镜：" + resource.getName());
                mCMSFilterEffectStrength.setCurrentValue(mEffectStrengthMap.get(resource));
                DemoSDKHelp.getSDK(FilterActivity.this).setFilterParam(mEffectStrengthMap.get(resource));
                if(TextUtils.isEmpty(resource.getPath()))
                {
                    DemoSDKHelp.getSDK(FilterActivity.this).setFilter("");
                }else {
                    DemoSDKHelp.getSDK(FilterActivity.this).setFilter(path + File.separator + resource.getPath());
                }
            }
        });

        mCMSFilterEffectStrength.setOnSeekBarChangeListener(new CustomMinSeekBar2.OnSeekBarChangeListener(){

            @Override
            public void onStopTrackingTouch(View view, int progress) {
                mEffectStrengthMap.put(mResource,progress);
                DemoSDKHelp.getSDK(FilterActivity.this).setFilterParam(mEffectStrengthMap.get(mResource));
            }

        });
    }


    private void initRecycleView() {
        mRvFilter.setLayoutManager(new GridLayoutManager(this, 5));
        mAdapter = new ResourceAdapter();
        mRvFilter.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(new Resource(R.mipmap.icon_no_effect,"取消","",0));
        resourceList.add(new Resource(R.mipmap.icon_nature_cream,"奶油","Resources/ColorfulStyleResources/Creamy.bundle",70));
        resourceList.add(new Resource(R.mipmap.icon_nature_youth,"青春","Resources/ColorfulStyleResources/Brighten.bundle",70));
        resourceList.add(new Resource(R.mipmap.icon_nature_fresh,"清新","Resources/ColorfulStyleResources/Fresh.bundle",80));
        resourceList.add(new Resource(R.mipmap.icon_nature_akita,"秋田","Resources/ColorfulStyleResources/Autumn.bundle",60));
        resourceList.add(new Resource(R.mipmap.icon_gray_monet,"莫奈","Resources/ColorfulStyleResources/Cool.bundle",80));
        resourceList.add(new Resource(R.mipmap.icon_gray_night,"暗夜","Resources/ColorfulStyleResources/Night.bundle",70));
        resourceList.add(new Resource(R.mipmap.icon_gray_film,"胶片","Resources/ColorfulStyleResources/Film-like.bundle",65));
        resourceList.add(new Resource(R.mipmap.icon_dream_sunset,"落日","Resources/ColorfulStyleResources/Sunset.bundle",70));
        resourceList.add(new Resource(R.mipmap.icon_dream_glaze,"琉璃","Resources/ColorfulStyleResources/Cozily.bundle",70));
        resourceList.add(new Resource(R.mipmap.icon_dream_nebula,"星云","Resources/ColorfulStyleResources/Sweet.bundle",70));

        mAdapter.setData(resourceList);

        for(Resource res : resourceList)
        {
            mEffectStrengthMap.put(res,res.getEffectStrength());
        }
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
        DemoSDKHelp.getSDK(FilterActivity.this).setFilter("");
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
