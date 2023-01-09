package im.zego.effectsexample.effectsonly.sdk;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

import im.zego.effectsexample.effectsonly.util.AppUtils;
import im.zego.effectsexample.effectsonly.util.ZegoUtil;
import im.zego.zegoeffectsexample.sdkmanager.SDKManager;

/**
 *
 */
public class DemoSDKHelp {

    public volatile static boolean isInit = false;

    public synchronized static SDKManager getSDK(Context context)
    {
        if(!isInit)
        {
            init(context);
            isInit = true;
        }

        return SDKManager.sharedInstance();
    }


    private static void init(Context context) {

        String path = context.getExternalCacheDir().getPath();
        if(AppUtils.getApp(context) != AppUtils.getVersionCode(context))
        {
            File file = new File(path);
            ZegoUtil.deleteDir(file);
            file.mkdirs();
            AppUtils.setApp(context, AppUtils.getVersionCode(context));
        }


        ArrayList<String> aiModeInfoList = copyAiModeInfoList(context);
        ArrayList<String> resourcesInfoList = copyResourcesInfoList(context);
        aiModeInfoList.addAll(resourcesInfoList);


        SDKManager.sharedInstance().initSDK(context,aiModeInfoList);
    }

    public synchronized static void uninit() {

        isInit = false;
        SDKManager.sharedInstance().uninitSDK();
    }

    private static ArrayList<String> copyAiModeInfoList(Context context)
    {
        String path = context.getExternalCacheDir().getPath();
        String faceDetection = "Models/FaceDetectionModel.model";
        String segmentation = "Models/SegmentationModel.model";
        ZegoUtil.copyFileFromAssets(context,faceDetection , path + File.separator + faceDetection);

        ZegoUtil.copyFileFromAssets(context, segmentation, path + File.separator + segmentation );
        ArrayList<String> aiModeInfoList = new ArrayList<>();
        aiModeInfoList.add(path + File.separator + faceDetection);
        aiModeInfoList.add(path + File.separator + segmentation);

        return aiModeInfoList;
    }

    private static ArrayList<String> copyResourcesInfoList(Context context) {
        String path = context.getExternalCacheDir().getPath();
        String faceWhitening = "Resources/FaceWhiteningResources.bundle";
        String pendantResources = "Resources/PendantResources.bundle";
        String rosyResources = "Resources/RosyResources.bundle";
        String teethWhiteningResources = "Resources/TeethWhiteningResources.bundle";
        String commonResources = "Resources/CommonResources.bundle";

        ZegoUtil.copyFileFromAssets(context,faceWhitening , path + File.separator + faceWhitening);
        ZegoUtil.copyFileFromAssets(context,pendantResources , path + File.separator + pendantResources);
        ZegoUtil.copyFileFromAssets(context,rosyResources , path + File.separator + rosyResources);
        ZegoUtil.copyFileFromAssets(context, teethWhiteningResources, path + File.separator + teethWhiteningResources );
        ZegoUtil.copyFileFromAssets(context, commonResources, path + File.separator + commonResources );

        ArrayList<String> resourcesInfoList = new ArrayList<>();
        resourcesInfoList.add(path + File.separator + faceWhitening);
        resourcesInfoList.add(path + File.separator + pendantResources);
        resourcesInfoList.add(path + File.separator + rosyResources);
        resourcesInfoList.add(path + File.separator + teethWhiteningResources);
        resourcesInfoList.add(path + File.separator + commonResources);

        return resourcesInfoList;
    }

    /**
     * 一键关闭所有美白功能,版本更新可能增加新功能接口，注意版本更新
     */
    public static void closeAllBeautifyEffects() {
        SDKManager.sharedInstance().enableSmooth(false);
        SDKManager.sharedInstance().enableWhiten(false);
        SDKManager.sharedInstance().enableRosy(false);
        SDKManager.sharedInstance().enableSharpen(false);
        SDKManager.sharedInstance().enableFaceLifting(false);
        SDKManager.sharedInstance().enableBigEye(false);
        SDKManager.sharedInstance().enableEyesBrightening(false);
        SDKManager.sharedInstance().enableLongChin(false);
        SDKManager.sharedInstance().enableSmallMouth(false);
        SDKManager.sharedInstance().enableTeethWhitening(false);
        SDKManager.sharedInstance().enableNoseNarrowing(false);


    }

}
