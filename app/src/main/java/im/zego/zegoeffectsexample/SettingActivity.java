package im.zego.zegoeffectsexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.zegoeffectsexample.sdkmanager.SDKManager;
import im.zego.zegoeffectsexample.sdkmanager.ZegoLicense;

public class SettingActivity extends Activity {

    private TextView mTvSdkVersion;
    private TextView mTvLicense;
    private TextView mTvSdkInit;
    private TextView mTvSdkAppID;
    private EditText mEtLicense;
    private Button mBtLicense;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
    }

    private void initView() {
        mTvSdkVersion = findViewById(R.id.tv_sdk_version);
        mTvLicense = findViewById(R.id.tv_license);
        mEtLicense = findViewById(R.id.et_license);
        mBtLicense = findViewById(R.id.bt_license);
        mTvSdkInit = findViewById(R.id.tv_sdk_init);
        mTvSdkAppID = findViewById(R.id.tv_sdk_app_id);
    }

    private void initListener() {
        if(DemoSDKHelp.isInit)
        {
            mTvSdkInit.setText("SDK已经初始化");
        }else {
            mTvSdkInit.setText("SDK未初始化");
        }
        mTvSdkAppID.setText("APP_ID:"+String.valueOf(ZegoLicense.APP_ID));
        mTvSdkVersion.setText(SDKManager.sharedInstance().getVersion());
        mTvLicense.setText(getLicense());
        mBtLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZegoLicense.effectsLicense = mEtLicense.getText().toString().trim();
                mTvLicense.setText(getLicense());
                mEtLicense.setText("");
                if(DemoSDKHelp.isInit)
                {
                    Toast.makeText(SettingActivity.this,"SDK已经初始化，设置不会生效",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String getLicense() {
        if(ZegoLicense.effectsLicense.length() >= 16)
        {
            return ZegoLicense.effectsLicense.substring(0,16);
        }else {
            return ZegoLicense.effectsLicense;
        }
    }
}
