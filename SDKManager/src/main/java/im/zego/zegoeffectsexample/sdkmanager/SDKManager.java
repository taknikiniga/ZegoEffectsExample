package im.zego.zegoeffectsexample.sdkmanager;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.TextureView;

import java.util.ArrayList;
import java.util.HashMap;

import im.zego.effects.ZegoEffects;
import im.zego.effects.callback.ZegoEffectsEventHandler;
import im.zego.effects.entity.ZegoEffectsAdvancedConfig;
import im.zego.effects.entity.ZegoEffectsBigEyesParam;
import im.zego.effects.entity.ZegoEffectsBlurParam;
import im.zego.effects.entity.ZegoEffectsBlusherParam;
import im.zego.effects.entity.ZegoEffectsCheekboneSlimmingParam;
import im.zego.effects.entity.ZegoEffectsChromaKeyParam;
import im.zego.effects.entity.ZegoEffectsColoredcontactsParam;
import im.zego.effects.entity.ZegoEffectsDarkCirclesRemovingParam;
import im.zego.effects.entity.ZegoEffectsEyelashesParam;
import im.zego.effects.entity.ZegoEffectsEyelinerParam;
import im.zego.effects.entity.ZegoEffectsEyesBrighteningParam;
import im.zego.effects.entity.ZegoEffectsEyeshadowParam;
import im.zego.effects.entity.ZegoEffectsFaceDetectionResult;
import im.zego.effects.entity.ZegoEffectsFaceLiftingParam;
import im.zego.effects.entity.ZegoEffectsFaceShorteningParam;
import im.zego.effects.entity.ZegoEffectsFilterParam;
import im.zego.effects.entity.ZegoEffectsForeheadShorteningParam;
import im.zego.effects.entity.ZegoEffectsLipstickParam;
import im.zego.effects.entity.ZegoEffectsLongChinParam;
import im.zego.effects.entity.ZegoEffectsMakeupParam;
import im.zego.effects.entity.ZegoEffectsMandibleSlimmingParam;
import im.zego.effects.entity.ZegoEffectsMosaicParam;
import im.zego.effects.entity.ZegoEffectsNoseLengtheningParam;
import im.zego.effects.entity.ZegoEffectsNoseNarrowingParam;
import im.zego.effects.entity.ZegoEffectsRect;
import im.zego.effects.entity.ZegoEffectsRosyParam;
import im.zego.effects.entity.ZegoEffectsSharpenParam;
import im.zego.effects.entity.ZegoEffectsSmallMouthParam;
import im.zego.effects.entity.ZegoEffectsSmoothParam;
import im.zego.effects.entity.ZegoEffectsTeethWhiteningParam;
import im.zego.effects.entity.ZegoEffectsVideoFrameParam;
import im.zego.effects.entity.ZegoEffectsWhitenParam;
import im.zego.effects.entity.ZegoEffectsWrinklesRemovingParam;
import im.zego.effects.enums.ZegoEffectsMosaicType;
import im.zego.effects.enums.ZegoEffectsScaleMode;
import im.zego.effects.enums.ZegoEffectsTextureFormat;
import im.zego.effects.enums.ZegoEffectsVideoFrameFormat;
import im.zego.zegoeffectsexample.sdkmanager.callback.FaceDetectionCallback;
import im.zego.zegoeffectsexample.sdkmanager.callback.KiwiCallback;
import im.zego.zegoeffectsexample.sdkmanager.capture.VideoCaptureFromCamera2;
import im.zego.zegoeffectsexample.sdkmanager.entity.FaceDetection;
import im.zego.zegoeffectsexample.sdkmanager.entity.MosaicType;
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize;

public class SDKManager {

    private static SDKManager sdkManage;
    VideoCaptureFromCamera2 videoCaptureFromCamera2;
    public static ZegoEffects zegoEffects;
    public String TAG = "SDKManage";
    public FaceDetectionCallback faceDetectionCallback;

    Context context;

    public static SDKManager sharedInstance() {
        if (sdkManage == null) {
            synchronized (SDKManager.class) {
                if (sdkManage == null) {
                    sdkManage = new SDKManager();
                }
            }
        }
        return sdkManage;
    }

    /**
     * 在线鉴权获取授权信息
     * @return
     */
    public static String getAuthInfo(Context context) {

        Log.e("dd", "getAuthInfo: "+ZegoEffects.getAuthInfo(ZegoLicense.APP_SIGN,context));
        return ZegoEffects.getAuthInfo( ZegoLicense.APP_SIGN,context);
    }


    /**
     * 开启高性能配置，需在create引擎前调用
     */
    public void enableHighPerformance(boolean enable){
        HashMap<String,String> map = new HashMap<String,String>();
        if(enable) {
            map.put("performance", "true");
        }else{
            map.put("performance", "false");
        }
        ZegoEffectsAdvancedConfig config = new ZegoEffectsAdvancedConfig();
        config.setAdvancedConfig(map);
        ZegoEffects.setAdvancedConfig(config);
    }

    private volatile static Object object = new Object();
    private volatile boolean isInitSDK = false;
    private volatile boolean isInitEnv = false;

    public void initEnv(int width,int height) {
        synchronized (object) {
            if(isInitSDK && !isInitEnv) {
                zegoEffects.initEnv(width, height);
                isInitEnv = true;
            }

        }
    }

    public void uninitEnv() {
        synchronized (object) {
            if(isInitSDK && isInitEnv) {
                zegoEffects.uninitEnv();
                isInitEnv = false;
            }

        }
    }

    public void initSDK(Context context,ArrayList<String> resourceInfoList) {
        synchronized (object) {
            if(!isInitSDK) {
                initEvn( context, resourceInfoList);
                isInitSDK = true;
            }
        }
    }

    public void uninitSDK() {
        synchronized (object) {
            if(isInitSDK) {
                uninitEnv();
                uninit();
                isInitSDK = false;
            }

        }
    }


    /**
     * 初始化环境
     * @param context
     * @param resourceInfoList    资源绝对路径列表，美白，红润，白牙，挂件等都需要使用
     */
    private void initEvn(Context context,ArrayList<String> resourceInfoList) {
        Log.v(TAG, "initEvn");
        this.context = context;

        ZegoEffects.setResources(resourceInfoList);

        zegoEffects = ZegoEffects.create(ZegoLicense.effectsLicense, context);

        videoCaptureFromCamera2 = new VideoCaptureFromCamera2(zegoEffects, context);
        zegoEffects.setEventHandler(new ZegoEffectsEventHandler() {
            @Override
            public void onError(ZegoEffects handle, int errorCode, String desc) {
                Log.e(TAG,"errorCode:" + errorCode);
                Log.e(TAG,"desc:" + desc);
            }

            @Override
            public void onFaceDetectionResult(ZegoEffectsFaceDetectionResult[] results, ZegoEffects handle) {

                if (faceDetectionCallback != null) {
                    FaceDetection[] faceDetections = new FaceDetection[results.length];
                    for (int i = 0; i < results.length; i++) {
                        FaceDetection faceDetection = new FaceDetection();
                        faceDetection.score = results[i].score;
                        faceDetection.bottom = results[i].rect.y + results[i].rect.height;
                        faceDetection.left = results[i].rect.x + results[i].rect.width;
                        faceDetection.right = results[i].rect.x;
                        faceDetection.top = results[i].rect.y;
                        faceDetections[i] = faceDetection;
                    }
                    faceDetectionCallback.faceDetection(faceDetections);
                }
            }
        });
    }

    private void uninit(){
        Log.v(TAG, "destroy");
        zegoEffects.destroy();
        faceDetectionCallback = null;
    }


    /**
     * 设置人脸检测回调
     * @param faceDetectionCallback
     */
    public void setFaceDetectionCallback(FaceDetectionCallback faceDetectionCallback) {
        this.faceDetectionCallback = faceDetectionCallback;
    }

    int bFront = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * 切换相机
     */
    public void setFrontCam() {
        Log.v(TAG, "setFrontCam");
        if (bFront == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            bFront = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            bFront = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        videoCaptureFromCamera2.setFrontCam(bFront);
    }

    /**
     *  开启磨皮
     * @param enable
     */
    public void enableSmooth(boolean enable) {
        Log.v(TAG, "enableSmooth :enable" + enable);
        zegoEffects.enableSmooth(enable);
    }

    /**
     * 设置磨皮参数
     * @param smoothEffectStrength
     */
    public void setSmoothParam(int smoothEffectStrength) {
        Log.v(TAG, "setSmoothParam :smoothEffectStrength" + smoothEffectStrength);
        ZegoEffectsSmoothParam zegoEffectsSmoothParam = new ZegoEffectsSmoothParam();
        zegoEffectsSmoothParam.intensity = smoothEffectStrength;
        zegoEffects.setSmoothParam(zegoEffectsSmoothParam);
    }

    /**
     * 是否开启美白
     * @param enable
     */
    public void enableWhiten(boolean enable) {
        Log.v(TAG, "enableWhiten :enable" + enable);
        zegoEffects.enableWhiten(enable);
    }

    /**
     * 设置美白参数
     * @param whitenEffectStrength
     */
    public void setWhitenParam(int whitenEffectStrength) {
        Log.v(TAG, "setWhitenParam :whitenEffectStrength" + whitenEffectStrength);
        ZegoEffectsWhitenParam zegoEffectsWhitenParam = new ZegoEffectsWhitenParam();
        zegoEffectsWhitenParam.intensity = whitenEffectStrength;
        zegoEffects.setWhitenParam(zegoEffectsWhitenParam);
    }

    /**
     * 是否开启红润
     * @param enable
     */
    public void enableRosy(boolean enable){
        Log.v(TAG, "enableRosy :enable" + enable);
        zegoEffects.enableRosy(enable);
    }

    /**
     * 设置红润参数
     * @param rosyEffectStrength
     */
    public void setRosyParam(int rosyEffectStrength){
        Log.v(TAG, "setRosyParam :rosyEffectStrength" + rosyEffectStrength);
        ZegoEffectsRosyParam zegoEffectsRosyParam = new ZegoEffectsRosyParam();
        zegoEffectsRosyParam.intensity =rosyEffectStrength;
        zegoEffects.setRosyParam(zegoEffectsRosyParam);
    }

    /**
     * 是否开启锐化
     * @param enable
     */
    public void enableSharpen(boolean enable){
        Log.v(TAG, "enableSharpen :enable" + enable);
        zegoEffects.enableSharpen(enable);
    }

    /**
     * 设置锐化参数
     * @param sharpenEffectStrength
     */
    public void setSharpenParam(int sharpenEffectStrength){
        Log.v(TAG, "setSharpenParam :sharpenEffectStrength" + sharpenEffectStrength);
        ZegoEffectsSharpenParam zegoEffectsSharpenParam = new ZegoEffectsSharpenParam();
        zegoEffectsSharpenParam.intensity =sharpenEffectStrength;
        zegoEffects.setSharpenParam(zegoEffectsSharpenParam);
    }

    /**
     * 是否开启去除法令纹
     * @param enable
     */
    public void enableWrinklesRemoving(boolean enable){
        Log.v(TAG, "enableWrinklesRemoving :enable" + enable);
        zegoEffects.enableWrinklesRemoving(enable);
    }

    /**
     * 设置去除法令纹参数
     * @param wrinklesRemovingEffectStrength
     */
    public void setWrinklesRemovingParam(int wrinklesRemovingEffectStrength){
        Log.v(TAG, "setWrinklesRemovingParam :wrinklesRemovingEffectStrength" + wrinklesRemovingEffectStrength);
        ZegoEffectsWrinklesRemovingParam zegoEffectsWrinklesRemovingParam = new ZegoEffectsWrinklesRemovingParam();
        zegoEffectsWrinklesRemovingParam.intensity =wrinklesRemovingEffectStrength;
        zegoEffects.setWrinklesRemovingParam(zegoEffectsWrinklesRemovingParam);
    }

    /**
     * 是否开启去除黑眼圈
     * @param enable
     */
    public void enableDarkCirclesRemoving(boolean enable){
        Log.v(TAG, "enableDarkCirclesRemoving :enable" + enable);
        zegoEffects.enableDarkCirclesRemoving(enable);
    }

    /**
     * 设置去除黑眼圈参数
     * @param darkCirclesRemovingEffectStrength
     */
    public void setDarkCirclesRemovingParam(int darkCirclesRemovingEffectStrength){
        Log.v(TAG, "setDarkCirclesRemovingParam :darkCirclesRemovingEffectStrength" + darkCirclesRemovingEffectStrength);
        ZegoEffectsDarkCirclesRemovingParam zegoEffectsDarkCirclesRemovingParam = new ZegoEffectsDarkCirclesRemovingParam();
        zegoEffectsDarkCirclesRemovingParam.intensity =darkCirclesRemovingEffectStrength;
        zegoEffects.setDarkCirclesRemovingParam(zegoEffectsDarkCirclesRemovingParam);
    }


    /**
     * 开启瘦脸
     * @param enable
     */
    public void enableFaceLifting(boolean enable) {
        Log.v(TAG, "enableFaceLifting :enable" + enable);
        zegoEffects.enableFaceLifting(enable);
    }

    /**
     * 设置瘦脸参数
     * @param faceLiftingEffectStrength
     */
    public void setFaceLiftingParam(int faceLiftingEffectStrength) {
        Log.v(TAG, "setFaceLiftingParam :faceLiftingEffectStrength" + faceLiftingEffectStrength);
        ZegoEffectsFaceLiftingParam zegoEffectsFaceLiftingParam = new ZegoEffectsFaceLiftingParam();
        zegoEffectsFaceLiftingParam.intensity = faceLiftingEffectStrength;
        zegoEffects.setFaceLiftingParam(zegoEffectsFaceLiftingParam);
    }

    /**
     * 开启大眼
     * @param enable
     */
    public void enableBigEye(boolean enable) {
        Log.v(TAG, "enableBigEye :enable" + enable);
        zegoEffects.enableBigEyes(enable);
    }

    /**
     * 设置大眼参数
     * @param bigEyeEffectStrength
     */
    public void setBigEyeParam(int bigEyeEffectStrength) {
        Log.v(TAG, "setBigEyeParam :bigEyeEffectStrength" + bigEyeEffectStrength);
        ZegoEffectsBigEyesParam zegoEffectsBigEyeParam = new ZegoEffectsBigEyesParam();
        zegoEffectsBigEyeParam.intensity = bigEyeEffectStrength;
        zegoEffects.setBigEyesParam(zegoEffectsBigEyeParam);
    }

    /**
     * 开启亮眼
     * @param enable
     */
    public void enableEyesBrightening(boolean enable) {
        Log.v(TAG, "enableEyesBrightening :enable" + enable);
        zegoEffects.enableEyesBrightening(enable);
    }

    /**
     * 设置亮眼参数
     * @param eyesBrighteningEffectStrength
     */
    public void setEyesBrighteningParam(int eyesBrighteningEffectStrength) {
        Log.v(TAG, "setEyesBrighteningParam :eyesBrighteningEffectStrength" + eyesBrighteningEffectStrength);
        ZegoEffectsEyesBrighteningParam zegoEffectsEyesBrighteningParam = new ZegoEffectsEyesBrighteningParam();
        zegoEffectsEyesBrighteningParam.intensity = eyesBrighteningEffectStrength;
        zegoEffects.setEyesBrighteningParam(zegoEffectsEyesBrighteningParam);
    }

    /**
     * 开启长下巴
     * @param enable
     */
    public void enableLongChin(boolean enable) {
        Log.v(TAG, "enableLongChin :enable" + enable);
        zegoEffects.enableLongChin(enable);
    }

    /**
     * 设置长下巴参数
     * @param longChinEffectStrength
     */
    public void setLongChinParam(int longChinEffectStrength) {
        Log.v(TAG, "setLongChinParam :longChinEffectStrength" + longChinEffectStrength);
        ZegoEffectsLongChinParam zegoEffectsLongChinParam = new ZegoEffectsLongChinParam();
        zegoEffectsLongChinParam.intensity = longChinEffectStrength;
        zegoEffects.setLongChinParam(zegoEffectsLongChinParam);
    }

    /**
     * 开启小嘴
     * @param enable
     */
    public void enableSmallMouth(boolean enable) {
        Log.v(TAG, "enableSmallMouth :enable" + enable);
        zegoEffects.enableSmallMouth(enable);
    }

    /**
     * 设置小嘴参数
     * @param smallMouthEffectStrength
     */
    public void setSmallMouthParam(int smallMouthEffectStrength) {
        Log.v(TAG, "setSmallMouthParam :smallMouthEffectStrength" + smallMouthEffectStrength);
        ZegoEffectsSmallMouthParam zegoEffectsSmallMouthParam = new ZegoEffectsSmallMouthParam();
        zegoEffectsSmallMouthParam.intensity = smallMouthEffectStrength;
        zegoEffects.setSmallMouthParam(zegoEffectsSmallMouthParam);
    }

    /**
     * 开启白牙
     * @param enable
     */
    public void enableTeethWhitening(boolean enable) {
        Log.v(TAG, "enableTeethWhitening :enable" + enable);
        zegoEffects.enableTeethWhitening(enable);
    }

    /**
     * 设置白牙参数
     * @param teethWhiteningEffectStrength
     */
    public void setTeethWhiteningParam(int teethWhiteningEffectStrength) {
        Log.v(TAG, "setTeethWhiteningParam :teethWhiteningEffectStrength" + teethWhiteningEffectStrength);
        ZegoEffectsTeethWhiteningParam zegoEffectsTeethWhiteningParam = new ZegoEffectsTeethWhiteningParam();
        zegoEffectsTeethWhiteningParam.intensity = teethWhiteningEffectStrength;
        zegoEffects.setTeethWhiteningParam(zegoEffectsTeethWhiteningParam);
    }

    /**
     * 开启瘦鼻
     * @param enable
     */
    public void enableNoseNarrowing(boolean enable) {
        Log.v(TAG, "enableNoseNarrowing :enable" + enable);
        zegoEffects.enableNoseNarrowing(enable);
    }

    /**
     * 设置瘦鼻参数
     * @param noseNarrowingEffectStrength
     */
    public void setNoseNarrowingParam(int noseNarrowingEffectStrength) {
        Log.v(TAG, "setNoseNarrowingParam :noseNarrowingEffectStrength" + noseNarrowingEffectStrength);
        ZegoEffectsNoseNarrowingParam zegoEffectsNoseNarrowingParam = new ZegoEffectsNoseNarrowingParam();
        zegoEffectsNoseNarrowingParam.intensity = noseNarrowingEffectStrength;
        zegoEffects.setNoseNarrowingParam(zegoEffectsNoseNarrowingParam);
    }

    /**
     * 开启长鼻
     * @param enable
     */
    public void enableNoseLengthening(boolean enable) {
        Log.v(TAG, "enableNoseLengthening :enable" + enable);
        zegoEffects.enableNoseLengthening(enable);
    }

    /**
     * 设置长鼻参数
     * @param noseLengtheningEffectStrength
     */
    public void setNoseLengtheningParam(int noseLengtheningEffectStrength) {
        Log.v(TAG, "setNoseLengtheningParam :noseLengtheningEffectStrength" + noseLengtheningEffectStrength);
        ZegoEffectsNoseLengtheningParam zegoEffectsNoseLengtheningParam = new ZegoEffectsNoseLengtheningParam();
        zegoEffectsNoseLengtheningParam.intensity = noseLengtheningEffectStrength;
        zegoEffects.setNoseLengtheningParam(zegoEffectsNoseLengtheningParam);
    }

    /**
     * 开启小脸
     * @param enable
     */
    public void enableFaceShortening(boolean enable) {
        Log.v(TAG, "enableFaceShortening :enable" + enable);
        zegoEffects.enableFaceShortening(enable);
    }

    /**
     * 设置小脸参数
     * @param faceShorteningEffectStrength
     */
    public void setFaceShorteningParam(int faceShorteningEffectStrength) {
        Log.v(TAG, "setFaceShorteningParam :faceShorteningEffectStrength" + faceShorteningEffectStrength);
        ZegoEffectsFaceShorteningParam zegoEffectsFaceShorteningParam = new ZegoEffectsFaceShorteningParam();
        zegoEffectsFaceShorteningParam.intensity = faceShorteningEffectStrength;
        zegoEffects.setFaceShorteningParam(zegoEffectsFaceShorteningParam);
    }

    /**
     * 开启瘦下颌骨
     * @param enable
     */
    public void enableMandibleSlimming(boolean enable) {
        Log.v(TAG, "enableMandibleSlimming :enable" + enable);
        zegoEffects.enableMandibleSlimming(enable);
    }

    /**
     * 设置瘦下颌骨参数
     * @param mandibleSlimmingEffectStrength
     */
    public void setMandibleSlimmingParam(int mandibleSlimmingEffectStrength) {
        Log.v(TAG, "setMandibleSlimmingParam :mandibleSlimmingEffectStrength" + mandibleSlimmingEffectStrength);
        ZegoEffectsMandibleSlimmingParam zegoEffectsMandibleSlimmingParam = new ZegoEffectsMandibleSlimmingParam();
        zegoEffectsMandibleSlimmingParam.intensity = mandibleSlimmingEffectStrength;
        zegoEffects.setMandibleSlimmingParam(zegoEffectsMandibleSlimmingParam);
    }

    /**
     * 开启瘦颧骨
     * @param enable
     */
    public void enableCheekboneSlimming(boolean enable) {
        Log.v(TAG, "enableCheekboneSlimming :enable" + enable);
        zegoEffects.enableCheekboneSlimming(enable);
    }

    /**
     * 设置瘦颧骨参数
     * @param cheekboneSlimmingEffectStrength
     */
    public void setCheekboneSlimmingParam(int cheekboneSlimmingEffectStrength) {
        Log.v(TAG, "setCheekboneSlimmingParam :cheekboneSlimmingEffectStrength" + cheekboneSlimmingEffectStrength);
        ZegoEffectsCheekboneSlimmingParam zegoEffectsCheekboneSlimmingParam = new ZegoEffectsCheekboneSlimmingParam();
        zegoEffectsCheekboneSlimmingParam.intensity = cheekboneSlimmingEffectStrength;
        zegoEffects.setCheekboneSlimmingParam(zegoEffectsCheekboneSlimmingParam);
    }

    /**
     * 开启缩小额头
     * @param enable
     */
    public void enableForeheadShortening(boolean enable) {
        Log.v(TAG, "enableForeheadShortening :enable" + enable);
        zegoEffects.enableForeheadShortening(enable);
    }

    /**
     * 设置缩小额头参数
     * @param foreheadShorteningEffectStrength
     */
    public void setForeheadShorteningParam(int foreheadShorteningEffectStrength) {
        Log.v(TAG, "setForeheadShorteningParam :foreheadShorteningEffectStrength" + foreheadShorteningEffectStrength);
        ZegoEffectsForeheadShorteningParam zegoEffectsForeheadShorteningParam = new ZegoEffectsForeheadShorteningParam();
        zegoEffectsForeheadShorteningParam.intensity = foreheadShorteningEffectStrength;
        zegoEffects.setForeheadShorteningParam(zegoEffectsForeheadShorteningParam);
    }


    /**
     * 设置口红
     * @param lookupTablePath
     */
    public void setLipstick(String lookupTablePath) {
        Log.v(TAG, "setLipstick :lookupTablePath" + lookupTablePath);
        zegoEffects.setLipstick(lookupTablePath);
    }

    /**
     * 设置口红参数
     * @param lipstickEffectStrength
     */
    public void setLipstickParam(int lipstickEffectStrength) {
        Log.v(TAG, "setLipstickParam :lipstickEffectStrength" + lipstickEffectStrength);
        ZegoEffectsLipstickParam zegoEffectsLipstickParam = new ZegoEffectsLipstickParam();
        zegoEffectsLipstickParam.intensity = lipstickEffectStrength;
        zegoEffects.setLipstickParam(zegoEffectsLipstickParam);
    }

    /**
     * 设置腮红
     * @param lookupTablePath
     */
    public void setCheek(String lookupTablePath) {
        Log.v(TAG, "setCheek :lookupTablePath" + lookupTablePath);
        zegoEffects.setBlusher(lookupTablePath);
    }

    /**
     * 设置腮红参数
     * @param cheekEffectStrength
     */
    public void setCheekParam(int cheekEffectStrength) {
        Log.v(TAG, "setCheekParam :cheekEffectStrength" + cheekEffectStrength);
        ZegoEffectsBlusherParam zegoEffectsBlusherParam = new ZegoEffectsBlusherParam();
        zegoEffectsBlusherParam.intensity = cheekEffectStrength;
        zegoEffects.setBlusherParam(zegoEffectsBlusherParam);
    }

    /**
     * 设置眼线
     * @param lookupTablePath
     */
    public void setEyeliner(String lookupTablePath) {
        Log.v(TAG, "setEyeliner :lookupTablePath" + lookupTablePath);
        zegoEffects.setEyeliner(lookupTablePath);
    }

    /**
     * 设置眼线参数
     * @param eyelinerEffectStrength
     */
    public void setEyelinerParam(int eyelinerEffectStrength) {
        Log.v(TAG, "setEyelinerParam :eyelinerEffectStrength" + eyelinerEffectStrength);
        ZegoEffectsEyelinerParam zegoEffectsEyelinerParam = new ZegoEffectsEyelinerParam();
        zegoEffectsEyelinerParam.intensity = eyelinerEffectStrength;
        zegoEffects.setEyelinerParam(zegoEffectsEyelinerParam);
    }

    /**
     * 设置眼影
     * @param lookupTablePath
     */
    public void setEyeshadow(String lookupTablePath) {
        Log.v(TAG, "setEyeshadow :lookupTablePath" + lookupTablePath);
        zegoEffects.setEyeshadow(lookupTablePath);
    }

    /**
     * 设置眼影参数
     * @param eyeshadowEffectStrength
     */
    public void setEyeshadowParam(int eyeshadowEffectStrength) {
        Log.v(TAG, "setEyeshadowParam :eyeshadowEffectStrength" + eyeshadowEffectStrength);
        ZegoEffectsEyeshadowParam zegoEffectsEyeshadowParam = new ZegoEffectsEyeshadowParam();
        zegoEffectsEyeshadowParam.intensity = eyeshadowEffectStrength;
        zegoEffects.setEyeshadowParam(zegoEffectsEyeshadowParam);
    }

    /**
     * 设置眼睫毛
     * @param lookupTablePath
     */
    public void setEyelash(String lookupTablePath) {
        Log.v(TAG, "setEyelash :lookupTablePath" + lookupTablePath);
        zegoEffects.setEyelashes(lookupTablePath);
    }

    /**
     * 设置眼睫毛参数
     * @param eyelashEffectStrength
     */
    public void setEyelashParam(int eyelashEffectStrength) {
        Log.v(TAG, "setEyelashParam :eyelashEffectStrength" + eyelashEffectStrength);
        ZegoEffectsEyelashesParam zegoEffectsEyelashParam = new ZegoEffectsEyelashesParam();
        zegoEffectsEyelashParam.intensity = eyelashEffectStrength;
        zegoEffects.setEyelashesParam(zegoEffectsEyelashParam);
    }

    /**
     * 设置美瞳
     * @param path
     */
    public void setEyesColored(String path) {
        Log.v(TAG, "setEyesColored :path" + path);
        zegoEffects.setColoredcontacts(path);
    }

    /**
     * 设置美瞳参数
     * @param effectStrength
     */
    public void setEyesColoredParam(int effectStrength) {
        Log.v(TAG, "setEyesColoredParam :effectStrength" + effectStrength);
        ZegoEffectsColoredcontactsParam zegoEffectsColoredcontactsParam = new ZegoEffectsColoredcontactsParam();
        zegoEffectsColoredcontactsParam.intensity = effectStrength;
        zegoEffects.setColoredcontactsParam(zegoEffectsColoredcontactsParam);
    }

    /**
     * 设置风格妆
     * @param path
     */
    public void setBeautifyStyle(String path) {
        Log.v(TAG, "setBeautifyStyle :path" + path);
        zegoEffects.setMakeup(path);
    }

    /**
     * 设置风格妆参数
     * @param effectStrength
     */
    public void setBeautifyStyleParam(int effectStrength) {
        Log.v(TAG, "setBeautifyStyleParam :effectStrength" + effectStrength);
        ZegoEffectsMakeupParam param = new ZegoEffectsMakeupParam();
        param.intensity = effectStrength;
        zegoEffects.setMakeupParam(param);
    }

    /**
     * 设置风格滤镜
     * @param lookupTablePath
     */
    public void setFilter(String lookupTablePath) {
        Log.v(TAG, "setFilter :lookupTablePath" + lookupTablePath);
        zegoEffects.setFilter(lookupTablePath);
    }

    /**
     * 设置风格滤镜参数
     * @param filterEffectStrength
     */
    public void setFilterParam(int filterEffectStrength) {
        Log.v(TAG, "setFilterParam :filterEffectStrength" + filterEffectStrength);
        ZegoEffectsFilterParam zegoEffectsFilterParam = new ZegoEffectsFilterParam();
        zegoEffectsFilterParam.intensity = filterEffectStrength;
        zegoEffects.setFilterParam(zegoEffectsFilterParam);
    }

    /**
     * 是否开启人像分割功能
     * @param enable
     */
    public void enableAISegment(boolean enable) {
        Log.v(TAG, "enableAISegment :enable" + enable);
        zegoEffects.enableChromaKey(false);
        zegoEffects.enablePortraitSegmentation(enable);

    }

    /**
     * 是否开启人像分割的背景
     * @param enable
     */
    public void enableAISegmentBackground(boolean enable) {
        Log.v(TAG, "enableAISegmentBackground :enable" + enable);
        zegoEffects.enablePortraitSegmentationBackground(enable);
    }

    /**
     * 设置人像分割背景
     * @param imageUrl
     */
    public void setPortraitSegmentationBackgroundPath(String imageUrl)
    {
        Log.v(TAG, "setPortraitSegmentationBackgroundPath :imageUrl" + imageUrl);
        zegoEffects.setPortraitSegmentationBackgroundPath(imageUrl, ZegoEffectsScaleMode.SCALE_TO_FILL);
    }

    /**
     * 设置动态人像分割背景
     */
    public void setPortraitSegmentationBackgroundTexture(int textureID, int width ,int height) {
//        Log.v(TAG, "setPortraitSegmentationBackgroundTexture :width = " + width +
//                "；height = " + height);
        ZegoEffectsVideoFrameParam videoFrameParam = new ZegoEffectsVideoFrameParam();
        videoFrameParam.width = width;
        videoFrameParam.height = height;
        videoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;
        zegoEffects.setPortraitSegmentationBackgroundTexture(textureID, videoFrameParam, ZegoEffectsScaleMode.SCALE_TO_FILL);

    }

    /**
     * 设置动态人像分割背景
     */
    public void setPortraitSegmentationBackgroundTextureOES(int textureID, int width ,int height) {
//        Log.v(TAG, "setPortraitSegmentationBackgroundTexture :width = " + width +
//                "；height = " + height);
        ZegoEffectsVideoFrameParam videoFrameParam = new ZegoEffectsVideoFrameParam();
        videoFrameParam.width = width;
        videoFrameParam.height = height;
        videoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;
        videoFrameParam.textureFormat = ZegoEffectsTextureFormat.TEXTURE_OES;
        zegoEffects.setPortraitSegmentationBackgroundTexture(textureID, videoFrameParam, ZegoEffectsScaleMode.ASPECT_FILL);

    }

    /**
     * 设置人像分割前景位置
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void setPortraitSegmentationBackgroundForegroundPosition(int x, int y, int w, int h) {
        ZegoEffectsRect zegoEffectsRect = new ZegoEffectsRect();
        zegoEffectsRect.x = x;
        zegoEffectsRect.y = y;
        zegoEffectsRect.width = w;
        zegoEffectsRect.height = h;
        zegoEffects.setPortraitSegmentationForegroundPosition(zegoEffectsRect);
    }

    /**
     * 开启人像分割背景模糊
     * @param enable
     */
    public void enablePortraitSegmentationBackgroundBlur(boolean enable) {
        Log.v(TAG, "enablePortraitSegmentationBackgroundBlur :enable" + enable);
        zegoEffects.enablePortraitSegmentationBackgroundBlur(enable);
    }

    /**
     * 设置人像分割背景模糊强度
     * @param backgroundBlurEffectStrength
     */
    public void setPortraitSegmentationBackgroundBlurParam(int backgroundBlurEffectStrength) {
        Log.v(TAG, "setPortraitSegmentationBackgroundBlurParam :backgroundBlurEffectStrength" + backgroundBlurEffectStrength);
        ZegoEffectsBlurParam zegoEffectsBlurParam = new ZegoEffectsBlurParam();
        zegoEffectsBlurParam.intensity = backgroundBlurEffectStrength;
        zegoEffects.setPortraitSegmentationBackgroundBlurParam(zegoEffectsBlurParam);
    }

    /**
     * 开启人像分割背景马赛克
     * @param enable
     */
    public void enablePortraitSegmentationBackgroundMosaic(boolean enable)
    {
        Log.v(TAG, "enablePortraitSegmentationBackgroundMosaic :enable" + enable);
        zegoEffects.enablePortraitSegmentationBackgroundMosaic(enable);
    }

    /**
     * 设置人像分割背景马赛克参数
     * @param mosaicEffectStrength
     * @param type
     */
    public void setPortraitSegmentationBackgroundMosaicParam(int mosaicEffectStrength,MosaicType type)
    {
        ZegoEffectsMosaicParam zegoEffectsMosaicParam = new ZegoEffectsMosaicParam();
        zegoEffectsMosaicParam.intensity = mosaicEffectStrength;
        zegoEffectsMosaicParam.type = ZegoEffectsMosaicType.getZegoEffectsMosaicType(type.value());
        Log.v(TAG, "setPortraitSegmentationBackgroundMosaicParam :mosaicEffectStrength" + mosaicEffectStrength);
        Log.v(TAG, "setPortraitSegmentationBackgroundMosaicParam :type" + type);
        zegoEffects.setPortraitSegmentationBackgroundMosaicParam(zegoEffectsMosaicParam);
    }

    /**
     * 开启绿幕
     * @param enable
     */
    public void enableChromaKey(boolean enable) {
        Log.v(TAG, "enableChromaKey :enable" + enable);
        zegoEffects.enablePortraitSegmentation(false);
        zegoEffects.enableChromaKey(enable);

    }

    /**
     * 是否开启绿幕分割的背景
     * @param isChecked
     */
    public void enableChromaKeyBackground(boolean isChecked) {
        zegoEffects.enableChromaKeyBackground(isChecked);
    }

    /**
     * 设置绿幕背景路径
     * @param imageUrl
     */
    public void setChromaKeyBackgroundPath(String imageUrl)
    {
        zegoEffects.setChromaKeyBackgroundPath(imageUrl, ZegoEffectsScaleMode.SCALE_TO_FILL);
    }

    /**
     * 设置绿幕抠图前景位置
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void setChromaKeyBackgroundForegroundPosition(int x, int y, int w, int h) {
        ZegoEffectsRect zegoEffectsRect = new ZegoEffectsRect();
        zegoEffectsRect.x = x;
        zegoEffectsRect.y = y;
        zegoEffectsRect.width = w;
        zegoEffectsRect.height = h;
        zegoEffects.setChromaKeyForegroundPosition(zegoEffectsRect);
    }

    /**
     * 设置绿幕分割参数
     * @param similarity
     * @param smoothness
     * @param borderSize
     * @param opacity
     * @param keyColor
     */
    public void setChromaKeyParam(float similarity, float smoothness, float borderSize, float opacity, int keyColor) {
        Log.v(TAG, "greenScreenSplit: " + similarity);

        ZegoEffectsChromaKeyParam chromaKeyParam = new ZegoEffectsChromaKeyParam();
        chromaKeyParam.setSimilarity(similarity);
        chromaKeyParam.setSmoothness(smoothness);
        chromaKeyParam.setBorderSize((int) borderSize);
        chromaKeyParam.setOpacity((int) opacity);
        chromaKeyParam.setKeyColor(keyColor);
        zegoEffects.setChromaKeyParam(chromaKeyParam);
    }

    /**
     * 开启绿幕分割背景模糊
     * @param enable
     */
    public void enableChromaKeyBackgroundBlur(boolean enable) {
        Log.v(TAG, "enableChromaKeyBackgroundBlur :enable" + enable);
        zegoEffects.enableChromaKeyBackgroundBlur(enable);
    }

    /**
     * 设置绿幕分割背景模糊强度
     * @param backgroundBlurEffectStrength
     */
    public void setChromaKeyBackgroundBlurParam(int backgroundBlurEffectStrength) {
        Log.v(TAG, "setChromaKeyBackgroundBlurParam :backgroundBlurEffectStrength" + backgroundBlurEffectStrength);
        ZegoEffectsBlurParam zegoEffectsBlurParam = new ZegoEffectsBlurParam();
        zegoEffectsBlurParam.intensity = backgroundBlurEffectStrength;
        zegoEffects.setChromaKeyBackgroundBlurParam(zegoEffectsBlurParam);
    }

    /**
     * 开启绿幕分割背景马赛克
     * @param enable
     */
    public void enableChromaKeyBackgroundMosaic(boolean enable)
    {
        Log.v(TAG, "enableChromaKeyBackgroundMosaic :enable" + enable);
        zegoEffects.enableChromaKeyBackgroundMosaic(enable);
    }

    /**
     * 设置绿幕分割背景马赛克参数
     * @param type
     */
    public void setChromaKeyBackgroundMosaicParam(int mosaicEffectStrength,MosaicType type)
    {
        ZegoEffectsMosaicParam zegoEffectsMosaicParam = new ZegoEffectsMosaicParam();
        zegoEffectsMosaicParam.intensity = mosaicEffectStrength;
        zegoEffectsMosaicParam.type = ZegoEffectsMosaicType.getZegoEffectsMosaicType(type.value());
        Log.v(TAG, "setChromaKeyBackgroundMosaicType :mosaicEffectStrength" + mosaicEffectStrength);
        Log.v(TAG, "setChromaKeyBackgroundMosaicType :type" + type);
        zegoEffects.setChromaKeyBackgroundMosaicParam(zegoEffectsMosaicParam);
    }



    /**
     *  开启人脸检测
     * @param enable
     */
    public void enableFaceDetection(boolean enable) {
        Log.v(TAG, "enableFaceDetection :enable" + enable);
        zegoEffects.enableFaceDetection(enable);
    }

    /**
     * 设置贴纸挂件
     * @param path 绝对路径，注意权限
     */
    public void setPendant(String path){
        zegoEffects.setPendant(path);
    }


    /**
     * 开始相机
     */
    public void startCamera() {
        Log.v(TAG, "startCamera ");
        // TODO 设置ai模型配置;
        videoCaptureFromCamera2.onStart();
    }

    /**
     * 停止相机
     */
    public void stopCamera() {
        Log.v(TAG, "stopCamera ");
        videoCaptureFromCamera2.onStop();
    }

    /**
     * 设置渲染间隔
     */
    public void setIntervalTime(long time){
        Log.v(TAG, "setIntervalTime: " + time);
        videoCaptureFromCamera2.setIntervalTime(time);
    }


    /**
     * 停止渲染特效
     */
    public void stopRenderAllEffects() {
        Log.v(TAG, "stopRenderAllEffects ");
        if (videoCaptureFromCamera2 == null)
            return;

        videoCaptureFromCamera2.stopRenderAllEffects();
    }

    /**
     * 开始渲染特效
     */
    public void startRenderAllEffects() {
        Log.v(TAG, "startRenderAllEffects ");
        if (videoCaptureFromCamera2 == null)
            return;

        videoCaptureFromCamera2.startRenderAllEffects();
    }


    /**
     * 设置分辨率
     * @param size
     */
    public void setPreviewSize(PreviewSize size) {
        Log.v(TAG, "setPreviewSize ");
        videoCaptureFromCamera2.setResolution(size.getWidth(), size.getHeight());
    }

    /**
     * 获取默认分辨率
     * @return
     */
    public PreviewSize getPreviewDefault() {
        PreviewSize previewSize = new PreviewSize();
        previewSize.setWidth(videoCaptureFromCamera2.mCameraWidth);
        previewSize.setHeight(videoCaptureFromCamera2.mCameraHeight);
        return previewSize;
    }

    /**
     * 得到当前视图大小
     * @return
     */
    public PreviewSize getViewSize() {
        PreviewSize previewSize = new PreviewSize();
        previewSize.setWidth(videoCaptureFromCamera2.mViewWidth);
        previewSize.setHeight(videoCaptureFromCamera2.mViewHeight);
        return previewSize;
    }

    /**
     * 设置视图
     * @param view
     */
    public void setView(TextureView view) {
        Log.v(TAG, "setView ");
        videoCaptureFromCamera2.setView(view);
    }

    /**
     * 设置回调
     */
    public void setKiwiCallBack(KiwiCallback callback) {
        Log.v(TAG, "setKiwiCallBack ");
        videoCaptureFromCamera2.setKiwiCallback(callback);
    }

    /**
     * 获取当前版本
     */
    public String getVersion() {
        return ZegoEffects.getVersion();
    }
}
