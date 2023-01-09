package im.zego.effectsexample.effectsonly.basicBeauty;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import com.midsizemango.effectsonly.R;

import java.io.File;

import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;

import im.zego.effectsexample.effectsonly.util.ZegoUtil;
import im.zego.effectsexample.effectsonly.widgets.CustomMinSeekBar2;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

/**
 * 美颜页面
 */
public class BasicBeautyActivity extends Activity {

    private boolean showBasicBeauty = true;

    private TextureView mCamera;
    private CustomMinSeekBar2 mSmooth;
    private CustomMinSeekBar2 mWhitenSkin;
    private CustomMinSeekBar2 mRosy;
    private CustomMinSeekBar2 mSharpen;
    private CustomMinSeekBar2 mWrinklesRemoving;
    private CustomMinSeekBar2 mDarkCirclesRemoving;
    private CustomMinSeekBar2 mFaceLifting;
    private CustomMinSeekBar2 mBigEye;
    private CustomMinSeekBar2 mBrightEye;
    private CustomMinSeekBar2 mLongChin;
    private CustomMinSeekBar2 mSmallMouth;
    private CustomMinSeekBar2 mWhiteTeeth;
    private CustomMinSeekBar2 mNoseNarrowing;
    private CustomMinSeekBar2 mNoseLengthening;
    private CustomMinSeekBar2 mFaceShortening;
    private CustomMinSeekBar2 mMandibleSlimming;
    private CustomMinSeekBar2 mCheekboneSlimming;
    private CustomMinSeekBar2 mForeheadShortening;

    private CustomMinSeekBar2 mLipstick;
    private CustomMinSeekBar2 mBlush;
    private CustomMinSeekBar2 mEyelash;
    private CustomMinSeekBar2 mEyeliner;
    private CustomMinSeekBar2 mEyeshadow;
    private CustomMinSeekBar2 mEyecolor;

    private Button mSwitchBeauty;


    private ScrollView mSvMakeup;
    private ScrollView mSvBasicBeauty;

    private CustomMinSeekBar2.OnSeekBarChangeListener listener = new CustomMinSeekBar2.OnSeekBarChangeListener(){

        @Override
        public void onStopTrackingTouch(View view, int progress) {

        }

        @Override
        public void onProgressChanged(View view, int progress, boolean fromUser) {
            int id = view.getId();
            if (id == R.id.smooth) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setSmoothParam(progress);
            } else if (id == R.id.whiten_skin) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setWhitenParam(progress);
            } else if (id == R.id.rosy) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setRosyParam(progress);
            } else if (id == R.id.sharpen) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setSharpenParam(progress);
            } else if (id == R.id.wrinkles_removing) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setWrinklesRemovingParam(progress);
            } else if (id == R.id.darkCircles_removing) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setDarkCirclesRemovingParam(progress);
            } else if (id == R.id.face_lifting) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setFaceLiftingParam(progress);
            } else if (id == R.id.big_eye) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setBigEyeParam(progress);
            } else if (id == R.id.bright_eye) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyesBrighteningParam(progress);
            } else if (id == R.id.long_chin) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setLongChinParam(progress);
            } else if (id == R.id.small_mouth) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setSmallMouthParam(progress);
            } else if (id == R.id.white_teeth) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setTeethWhiteningParam(progress);
            } else if (id == R.id.nose_narrowing) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setNoseNarrowingParam(progress);
            } else if (id == R.id.nose_lengthening) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setNoseLengtheningParam(progress);
            } else if (id == R.id.face_shortening) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setFaceShorteningParam(progress);
            } else if (id == R.id.mandible_slimming) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setMandibleSlimmingParam(progress);
            } else if (id == R.id.cheekbone_slimming) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setCheekboneSlimmingParam(progress);
            } else if (id == R.id.forehead_shortening) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setForeheadShorteningParam(progress);
            } else if (id == R.id.lipstick) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setLipstickParam(progress);
            } else if (id == R.id.blush) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setCheekParam(progress);
            } else if (id == R.id.eyelash) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyelashParam(progress);
            } else if (id == R.id.eyeliner) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyelinerParam(progress);
            } else if (id == R.id.eyeshadow) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyeshadowParam(progress);
            } else if (id == R.id.eyecolor) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyesColoredParam(progress);
            }
        }

        @Override
        public void onCheckedChanged(View view, boolean isChecked) {
            int id = view.getId();
            if (id == R.id.smooth) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableSmooth(isChecked);
            } else if (id == R.id.whiten_skin) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableWhiten(isChecked);
            } else if (id == R.id.rosy) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableRosy(isChecked);
            } else if (id == R.id.sharpen) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableSharpen(isChecked);
            } else if (id == R.id.wrinkles_removing) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableWrinklesRemoving(isChecked);
            } else if (id == R.id.darkCircles_removing) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableDarkCirclesRemoving(isChecked);
            } else if (id == R.id.face_lifting) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableFaceLifting(isChecked);
            } else if (id == R.id.big_eye) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableBigEye(isChecked);
            } else if (id == R.id.bright_eye) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableEyesBrightening(isChecked);
            } else if (id == R.id.long_chin) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableLongChin(isChecked);
            } else if (id == R.id.small_mouth) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableSmallMouth(isChecked);
            } else if (id == R.id.white_teeth) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableTeethWhitening(isChecked);
            } else if (id == R.id.nose_narrowing) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableNoseNarrowing(isChecked);
            } else if (id == R.id.nose_lengthening) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableNoseLengthening(isChecked);
            } else if (id == R.id.face_shortening) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableFaceShortening(isChecked);
            } else if (id == R.id.mandible_slimming) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableMandibleSlimming(isChecked);
            } else if (id == R.id.cheekbone_slimming) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableCheekboneSlimming(isChecked);
            } else if (id == R.id.forehead_shortening) {
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).enableForeheadShortening(isChecked);
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_beauty);

        initView();
        initListener();
        initCamera();
    }

    private void initView()
    {
        mCamera = findViewById(R.id.camera);
        mSmooth = findViewById(R.id.smooth);
        mWhitenSkin = findViewById(R.id.whiten_skin);
        mRosy = findViewById(R.id.rosy);
        mSharpen = findViewById(R.id.sharpen);
        mWrinklesRemoving = findViewById(R.id.wrinkles_removing);
        mDarkCirclesRemoving = findViewById(R.id.darkCircles_removing);
        mFaceLifting = findViewById(R.id.face_lifting);
        mBigEye = findViewById(R.id.big_eye);
        mBrightEye = findViewById(R.id.bright_eye);
        mLongChin = findViewById(R.id.long_chin);
        mSmallMouth = findViewById(R.id.small_mouth);
        mWhiteTeeth = findViewById(R.id.white_teeth);
        mNoseNarrowing = findViewById(R.id.nose_narrowing);
        mNoseLengthening = findViewById(R.id.nose_lengthening);
        mFaceShortening = findViewById(R.id.face_shortening);
        mMandibleSlimming = findViewById(R.id.mandible_slimming);
        mCheekboneSlimming = findViewById(R.id.cheekbone_slimming);
        mForeheadShortening = findViewById(R.id.forehead_shortening);

        mLipstick = findViewById(R.id.lipstick);
        mBlush = findViewById(R.id.blush);
        mEyelash = findViewById(R.id.eyelash);
        mEyeliner = findViewById(R.id.eyeliner);
        mEyeshadow = findViewById(R.id.eyeshadow);
        mEyecolor = findViewById(R.id.eyecolor);

        mSvBasicBeauty = findViewById(R.id.sv_basic_beauty);
        mSvMakeup = findViewById(R.id.sv_makeup);


        mSwitchBeauty = findViewById(R.id.switch_beauty);
    }

    private void initListener()
    {

        mSmooth.setOnSeekBarChangeListener(listener);
        mWhitenSkin.setOnSeekBarChangeListener(listener);
        mRosy.setOnSeekBarChangeListener(listener);
        mSharpen.setOnSeekBarChangeListener(listener);
        mWrinklesRemoving.setOnSeekBarChangeListener(listener);
        mDarkCirclesRemoving.setOnSeekBarChangeListener(listener);
        mFaceLifting.setOnSeekBarChangeListener(listener);
        mBigEye.setOnSeekBarChangeListener(listener);
        mBrightEye.setOnSeekBarChangeListener(listener);
        mLongChin.setOnSeekBarChangeListener(listener);
        mSmallMouth.setOnSeekBarChangeListener(listener);
        mWhiteTeeth.setOnSeekBarChangeListener(listener);
        mNoseNarrowing.setOnSeekBarChangeListener(listener);
        mNoseLengthening.setOnSeekBarChangeListener(listener);
        mFaceShortening.setOnSeekBarChangeListener(listener);
        mMandibleSlimming.setOnSeekBarChangeListener(listener);
        mCheekboneSlimming.setOnSeekBarChangeListener(listener);
        mForeheadShortening.setOnSeekBarChangeListener(listener);

        mLipstick.setOnSeekBarChangeListener(listener);
        mBlush.setOnSeekBarChangeListener(listener);
        mEyelash.setOnSeekBarChangeListener(listener);
        mEyeliner.setOnSeekBarChangeListener(listener);
        mEyeshadow.setOnSeekBarChangeListener(listener);
        mEyecolor.setOnSeekBarChangeListener(listener);

        mSwitchBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showBasicBeauty)
                {
                    showBasicBeauty = false;
                    mSvBasicBeauty.setVisibility(View.GONE);
                    mSvMakeup.setVisibility(View.VISIBLE);
                    mSwitchBeauty.setText("切换到美肤美型");

                }else {
                    showBasicBeauty = true;
                    mSvBasicBeauty.setVisibility(View.VISIBLE);
                    mSvMakeup.setVisibility(View.GONE);
                    mSwitchBeauty.setText("切换到美妆");
                }
            }
        });

        ((RadioGroup) findViewById(R.id.lipstick_list)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton view = group.findViewById(checkedId);
                if (view.isChecked()) {
                    DemoSDKHelp.getSDK(BasicBeautyActivity.this).setLipstick(getResourcePath((String) view.getTag()));
                }
            }
        });

        ((RadioGroup) findViewById(R.id.blush_list)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton view = group.findViewById(checkedId);
                if (view.isChecked()) {
                    DemoSDKHelp.getSDK(BasicBeautyActivity.this).setCheek(getResourcePath((String) view.getTag()));
                }
            }
        });

        ((RadioGroup) findViewById(R.id.eyelash_list)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton view = group.findViewById(checkedId);
                if (view.isChecked()) {
                    DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyelash(getResourcePath((String) view.getTag()));
                }
            }
        });

        ((RadioGroup) findViewById(R.id.eyeliner_list)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton view = group.findViewById(checkedId);
                if (view.isChecked()) {
                    DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyeliner(getResourcePath((String) view.getTag()));
                }
            }
        });

        ((RadioGroup) findViewById(R.id.eyeshadow_list)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton view = group.findViewById(checkedId);
                if (view.isChecked()) {
                    DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyeshadow(getResourcePath((String) view.getTag()));
                }
            }
        });

        ((RadioGroup) findViewById(R.id.eyecolor_list)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton view = group.findViewById(checkedId);
                if (view.isChecked()) {
                    DemoSDKHelp.getSDK(BasicBeautyActivity.this).setEyesColored(getResourcePath((String) view.getTag()));
                }
            }
        });
    }

    private void initCamera(){
        DemoSDKHelp.getSDK(BasicBeautyActivity.this).setView(mCamera);
        mCamera.post(new Runnable() {
            @Override
            public void run() {
                PreviewSize size = new PreviewSize();
                size.setWidth(mCamera.getWidth());
                size.setHeight(mCamera.getHeight());
                DemoSDKHelp.getSDK(BasicBeautyActivity.this).setPreviewSize(size);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DemoSDKHelp.closeAllBeautifyEffects();
        DemoSDKHelp.uninit();
        DemoSDKHelp.getSDK(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DemoSDKHelp.getSDK(BasicBeautyActivity.this).setView(mCamera);
        DemoSDKHelp.getSDK(BasicBeautyActivity.this).startCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DemoSDKHelp.getSDK(BasicBeautyActivity.this).stopCamera();
    }

    private String getResourcePath(String path){

        if(TextUtils.isEmpty(path))
        {
            return "";
        }

        String basePath = this.getExternalCacheDir().getPath();
        ZegoUtil.copyFileFromAssets(
                this,path, basePath + File.separator + path);

        return basePath + File.separator + path;
    }


}
