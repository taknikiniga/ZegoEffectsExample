package im.zego.zegoeffectsexample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helsy.effectcalling.VideoCallingActivity;

import im.zego.effectsexample.effectsforexpress.EffectsForExpressListActivity;
import im.zego.effectsexample.effectsonly.EffectsOnlyListActivity;
import im.zego.effectsexample.effectsonly.sdk.DemoSDKHelp;
import im.zego.zegoeffectsexample.adapter.MainAdapter;
import im.zego.zegoeffectsexample.entity.ModuleInfo;
import im.zego.zegoeffectsexample.sdkmanager.ZegoLicense;
import im.zego.zegoeffectsexample.sdkmanager.net.IGetLicenseCallback;
import im.zego.zegoeffectsexample.sdkmanager.net.License;
import im.zego.zegoeffectsexample.sdkmanager.net.LicenseAPI;
import im.zego.zegoeffectsexample.view.ProcessView;

public class MainActivity extends Activity {

    private MainAdapter mainAdapter = new MainAdapter();
    private RecyclerView mRvModuleList;


    private ProcessView processView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            /* If this is not the root activity */
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }
        setContentView(R.layout.activity_main);
        initView();

        mRvModuleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvModuleList.setAdapter(mainAdapter);
        mRvModuleList.setItemAnimator(new DefaultItemAnimator());

        mainAdapter.setOnItemClickListener((view, position) -> {
            DemoSDKHelp.getSDK(this);
            boolean orRequestPermission = this.checkOrRequestPermission(100);
            ModuleInfo moduleInfo = (ModuleInfo) view.getTag();
            if (orRequestPermission) {
                String module = moduleInfo.getModule();
                if (module.equals(getString(R.string.tx_title_effects_only))) {
                    EffectsOnlyListActivity.actionStart(MainActivity.this);
                } else if (module.equals(getString(R.string.tx_title_effects_for_express))) {
                    EffectsForExpressListActivity.actionStart(MainActivity.this);
                }
            }
        });
        mainAdapter.addModuleInfo(new ModuleInfo().moduleName(getString(R.string.tx_title_effects_only)));
        mainAdapter.addModuleInfo(new ModuleInfo().moduleName(getString(R.string.tx_title_effects_for_express)));

        processView.setIsShowShade(false).setCancelable(false).show((ViewGroup) mRvModuleList.getRootView());

        LicenseAPI.getLicense(MainActivity.this.getApplicationContext(),new IGetLicenseCallback() {
            @Override
            public void onGetLicense(int code, String message, License license) {
                processView.dismiss();
                    if(code == 0){
                        ZegoLicense.effectsLicense = license.getLicense();
                        startActivity(new Intent(MainActivity.this, VideoCallingActivity.class));
                    }else {
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                    }
            }
        });

    }

    private void initView()
    {
        mRvModuleList = findViewById(R.id.module_list);
        processView = new ProcessView(MainActivity.this);
    }


    // 需要申请 麦克风权限-读写sd卡权限-摄像头权限
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    /**
     * 校验并请求权限
     */
    public boolean checkOrRequestPermission(int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS_STORAGE, code);
                return false;
            }
        }
        return true;
    }

    public void setting(View view) {
        startActivity(new Intent(this, VideoCallingActivity.class));
    }
}
