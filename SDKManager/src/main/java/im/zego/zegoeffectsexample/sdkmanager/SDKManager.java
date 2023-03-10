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
     * ??????????????????????????????
     * @return
     */
    public static String getAuthInfo(Context context) {

        Log.e("dd", "getAuthInfo: "+ZegoEffects.getAuthInfo(ZegoLicense.APP_SIGN,context));
        return ZegoEffects.getAuthInfo( ZegoLicense.APP_SIGN,context);
    }


    /**
     * ??????????????????????????????create???????????????
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
     * ???????????????
     * @param context
     * @param resourceInfoList    ??????????????????????????????????????????????????????????????????????????????
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
     * ????????????????????????
     * @param faceDetectionCallback
     */
    public void setFaceDetectionCallback(FaceDetectionCallback faceDetectionCallback) {
        this.faceDetectionCallback = faceDetectionCallback;
    }

    int bFront = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * ????????????
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
     *  ????????????
     * @param enable
     */
    public void enableSmooth(boolean enable) {
        Log.v(TAG, "enableSmooth :enable" + enable);
        zegoEffects.enableSmooth(enable);
    }

    /**
     * ??????????????????
     * @param smoothEffectStrength
     */
    public void setSmoothParam(int smoothEffectStrength) {
        Log.v(TAG, "setSmoothParam :smoothEffectStrength" + smoothEffectStrength);
        ZegoEffectsSmoothParam zegoEffectsSmoothParam = new ZegoEffectsSmoothParam();
        zegoEffectsSmoothParam.intensity = smoothEffectStrength;
        zegoEffects.setSmoothParam(zegoEffectsSmoothParam);
    }

    /**
     * ??????????????????
     * @param enable
     */
    public void enableWhiten(boolean enable) {
        Log.v(TAG, "enableWhiten :enable" + enable);
        zegoEffects.enableWhiten(enable);
    }

    /**
     * ??????????????????
     * @param whitenEffectStrength
     */
    public void setWhitenParam(int whitenEffectStrength) {
        Log.v(TAG, "setWhitenParam :whitenEffectStrength" + whitenEffectStrength);
        ZegoEffectsWhitenParam zegoEffectsWhitenParam = new ZegoEffectsWhitenParam();
        zegoEffectsWhitenParam.intensity = whitenEffectStrength;
        zegoEffects.setWhitenParam(zegoEffectsWhitenParam);
    }

    /**
     * ??????????????????
     * @param enable
     */
    public void enableRosy(boolean enable){
        Log.v(TAG, "enableRosy :enable" + enable);
        zegoEffects.enableRosy(enable);
    }

    /**
     * ??????????????????
     * @param rosyEffectStrength
     */
    public void setRosyParam(int rosyEffectStrength){
        Log.v(TAG, "setRosyParam :rosyEffectStrength" + rosyEffectStrength);
        ZegoEffectsRosyParam zegoEffectsRosyParam = new ZegoEffectsRosyParam();
        zegoEffectsRosyParam.intensity =rosyEffectStrength;
        zegoEffects.setRosyParam(zegoEffectsRosyParam);
    }

    /**
     * ??????????????????
     * @param enable
     */
    public void enableSharpen(boolean enable){
        Log.v(TAG, "enableSharpen :enable" + enable);
        zegoEffects.enableSharpen(enable);
    }

    /**
     * ??????????????????
     * @param sharpenEffectStrength
     */
    public void setSharpenParam(int sharpenEffectStrength){
        Log.v(TAG, "setSharpenParam :sharpenEffectStrength" + sharpenEffectStrength);
        ZegoEffectsSharpenParam zegoEffectsSharpenParam = new ZegoEffectsSharpenParam();
        zegoEffectsSharpenParam.intensity =sharpenEffectStrength;
        zegoEffects.setSharpenParam(zegoEffectsSharpenParam);
    }

    /**
     * ???????????????????????????
     * @param enable
     */
    public void enableWrinklesRemoving(boolean enable){
        Log.v(TAG, "enableWrinklesRemoving :enable" + enable);
        zegoEffects.enableWrinklesRemoving(enable);
    }

    /**
     * ???????????????????????????
     * @param wrinklesRemovingEffectStrength
     */
    public void setWrinklesRemovingParam(int wrinklesRemovingEffectStrength){
        Log.v(TAG, "setWrinklesRemovingParam :wrinklesRemovingEffectStrength" + wrinklesRemovingEffectStrength);
        ZegoEffectsWrinklesRemovingParam zegoEffectsWrinklesRemovingParam = new ZegoEffectsWrinklesRemovingParam();
        zegoEffectsWrinklesRemovingParam.intensity =wrinklesRemovingEffectStrength;
        zegoEffects.setWrinklesRemovingParam(zegoEffectsWrinklesRemovingParam);
    }

    /**
     * ???????????????????????????
     * @param enable
     */
    public void enableDarkCirclesRemoving(boolean enable){
        Log.v(TAG, "enableDarkCirclesRemoving :enable" + enable);
        zegoEffects.enableDarkCirclesRemoving(enable);
    }

    /**
     * ???????????????????????????
     * @param darkCirclesRemovingEffectStrength
     */
    public void setDarkCirclesRemovingParam(int darkCirclesRemovingEffectStrength){
        Log.v(TAG, "setDarkCirclesRemovingParam :darkCirclesRemovingEffectStrength" + darkCirclesRemovingEffectStrength);
        ZegoEffectsDarkCirclesRemovingParam zegoEffectsDarkCirclesRemovingParam = new ZegoEffectsDarkCirclesRemovingParam();
        zegoEffectsDarkCirclesRemovingParam.intensity =darkCirclesRemovingEffectStrength;
        zegoEffects.setDarkCirclesRemovingParam(zegoEffectsDarkCirclesRemovingParam);
    }


    /**
     * ????????????
     * @param enable
     */
    public void enableFaceLifting(boolean enable) {
        Log.v(TAG, "enableFaceLifting :enable" + enable);
        zegoEffects.enableFaceLifting(enable);
    }

    /**
     * ??????????????????
     * @param faceLiftingEffectStrength
     */
    public void setFaceLiftingParam(int faceLiftingEffectStrength) {
        Log.v(TAG, "setFaceLiftingParam :faceLiftingEffectStrength" + faceLiftingEffectStrength);
        ZegoEffectsFaceLiftingParam zegoEffectsFaceLiftingParam = new ZegoEffectsFaceLiftingParam();
        zegoEffectsFaceLiftingParam.intensity = faceLiftingEffectStrength;
        zegoEffects.setFaceLiftingParam(zegoEffectsFaceLiftingParam);
    }

    /**
     * ????????????
     * @param enable
     */
    public void enableBigEye(boolean enable) {
        Log.v(TAG, "enableBigEye :enable" + enable);
        zegoEffects.enableBigEyes(enable);
    }

    /**
     * ??????????????????
     * @param bigEyeEffectStrength
     */
    public void setBigEyeParam(int bigEyeEffectStrength) {
        Log.v(TAG, "setBigEyeParam :bigEyeEffectStrength" + bigEyeEffectStrength);
        ZegoEffectsBigEyesParam zegoEffectsBigEyeParam = new ZegoEffectsBigEyesParam();
        zegoEffectsBigEyeParam.intensity = bigEyeEffectStrength;
        zegoEffects.setBigEyesParam(zegoEffectsBigEyeParam);
    }

    /**
     * ????????????
     * @param enable
     */
    public void enableEyesBrightening(boolean enable) {
        Log.v(TAG, "enableEyesBrightening :enable" + enable);
        zegoEffects.enableEyesBrightening(enable);
    }

    /**
     * ??????????????????
     * @param eyesBrighteningEffectStrength
     */
    public void setEyesBrighteningParam(int eyesBrighteningEffectStrength) {
        Log.v(TAG, "setEyesBrighteningParam :eyesBrighteningEffectStrength" + eyesBrighteningEffectStrength);
        ZegoEffectsEyesBrighteningParam zegoEffectsEyesBrighteningParam = new ZegoEffectsEyesBrighteningParam();
        zegoEffectsEyesBrighteningParam.intensity = eyesBrighteningEffectStrength;
        zegoEffects.setEyesBrighteningParam(zegoEffectsEyesBrighteningParam);
    }

    /**
     * ???????????????
     * @param enable
     */
    public void enableLongChin(boolean enable) {
        Log.v(TAG, "enableLongChin :enable" + enable);
        zegoEffects.enableLongChin(enable);
    }

    /**
     * ?????????????????????
     * @param longChinEffectStrength
     */
    public void setLongChinParam(int longChinEffectStrength) {
        Log.v(TAG, "setLongChinParam :longChinEffectStrength" + longChinEffectStrength);
        ZegoEffectsLongChinParam zegoEffectsLongChinParam = new ZegoEffectsLongChinParam();
        zegoEffectsLongChinParam.intensity = longChinEffectStrength;
        zegoEffects.setLongChinParam(zegoEffectsLongChinParam);
    }

    /**
     * ????????????
     * @param enable
     */
    public void enableSmallMouth(boolean enable) {
        Log.v(TAG, "enableSmallMouth :enable" + enable);
        zegoEffects.enableSmallMouth(enable);
    }

    /**
     * ??????????????????
     * @param smallMouthEffectStrength
     */
    public void setSmallMouthParam(int smallMouthEffectStrength) {
        Log.v(TAG, "setSmallMouthParam :smallMouthEffectStrength" + smallMouthEffectStrength);
        ZegoEffectsSmallMouthParam zegoEffectsSmallMouthParam = new ZegoEffectsSmallMouthParam();
        zegoEffectsSmallMouthParam.intensity = smallMouthEffectStrength;
        zegoEffects.setSmallMouthParam(zegoEffectsSmallMouthParam);
    }

    /**
     * ????????????
     * @param enable
     */
    public void enableTeethWhitening(boolean enable) {
        Log.v(TAG, "enableTeethWhitening :enable" + enable);
        zegoEffects.enableTeethWhitening(enable);
    }

    /**
     * ??????????????????
     * @param teethWhiteningEffectStrength
     */
    public void setTeethWhiteningParam(int teethWhiteningEffectStrength) {
        Log.v(TAG, "setTeethWhiteningParam :teethWhiteningEffectStrength" + teethWhiteningEffectStrength);
        ZegoEffectsTeethWhiteningParam zegoEffectsTeethWhiteningParam = new ZegoEffectsTeethWhiteningParam();
        zegoEffectsTeethWhiteningParam.intensity = teethWhiteningEffectStrength;
        zegoEffects.setTeethWhiteningParam(zegoEffectsTeethWhiteningParam);
    }

    /**
     * ????????????
     * @param enable
     */
    public void enableNoseNarrowing(boolean enable) {
        Log.v(TAG, "enableNoseNarrowing :enable" + enable);
        zegoEffects.enableNoseNarrowing(enable);
    }

    /**
     * ??????????????????
     * @param noseNarrowingEffectStrength
     */
    public void setNoseNarrowingParam(int noseNarrowingEffectStrength) {
        Log.v(TAG, "setNoseNarrowingParam :noseNarrowingEffectStrength" + noseNarrowingEffectStrength);
        ZegoEffectsNoseNarrowingParam zegoEffectsNoseNarrowingParam = new ZegoEffectsNoseNarrowingParam();
        zegoEffectsNoseNarrowingParam.intensity = noseNarrowingEffectStrength;
        zegoEffects.setNoseNarrowingParam(zegoEffectsNoseNarrowingParam);
    }

    /**
     * ????????????
     * @param enable
     */
    public void enableNoseLengthening(boolean enable) {
        Log.v(TAG, "enableNoseLengthening :enable" + enable);
        zegoEffects.enableNoseLengthening(enable);
    }

    /**
     * ??????????????????
     * @param noseLengtheningEffectStrength
     */
    public void setNoseLengtheningParam(int noseLengtheningEffectStrength) {
        Log.v(TAG, "setNoseLengtheningParam :noseLengtheningEffectStrength" + noseLengtheningEffectStrength);
        ZegoEffectsNoseLengtheningParam zegoEffectsNoseLengtheningParam = new ZegoEffectsNoseLengtheningParam();
        zegoEffectsNoseLengtheningParam.intensity = noseLengtheningEffectStrength;
        zegoEffects.setNoseLengtheningParam(zegoEffectsNoseLengtheningParam);
    }

    /**
     * ????????????
     * @param enable
     */
    public void enableFaceShortening(boolean enable) {
        Log.v(TAG, "enableFaceShortening :enable" + enable);
        zegoEffects.enableFaceShortening(enable);
    }

    /**
     * ??????????????????
     * @param faceShorteningEffectStrength
     */
    public void setFaceShorteningParam(int faceShorteningEffectStrength) {
        Log.v(TAG, "setFaceShorteningParam :faceShorteningEffectStrength" + faceShorteningEffectStrength);
        ZegoEffectsFaceShorteningParam zegoEffectsFaceShorteningParam = new ZegoEffectsFaceShorteningParam();
        zegoEffectsFaceShorteningParam.intensity = faceShorteningEffectStrength;
        zegoEffects.setFaceShorteningParam(zegoEffectsFaceShorteningParam);
    }

    /**
     * ??????????????????
     * @param enable
     */
    public void enableMandibleSlimming(boolean enable) {
        Log.v(TAG, "enableMandibleSlimming :enable" + enable);
        zegoEffects.enableMandibleSlimming(enable);
    }

    /**
     * ????????????????????????
     * @param mandibleSlimmingEffectStrength
     */
    public void setMandibleSlimmingParam(int mandibleSlimmingEffectStrength) {
        Log.v(TAG, "setMandibleSlimmingParam :mandibleSlimmingEffectStrength" + mandibleSlimmingEffectStrength);
        ZegoEffectsMandibleSlimmingParam zegoEffectsMandibleSlimmingParam = new ZegoEffectsMandibleSlimmingParam();
        zegoEffectsMandibleSlimmingParam.intensity = mandibleSlimmingEffectStrength;
        zegoEffects.setMandibleSlimmingParam(zegoEffectsMandibleSlimmingParam);
    }

    /**
     * ???????????????
     * @param enable
     */
    public void enableCheekboneSlimming(boolean enable) {
        Log.v(TAG, "enableCheekboneSlimming :enable" + enable);
        zegoEffects.enableCheekboneSlimming(enable);
    }

    /**
     * ?????????????????????
     * @param cheekboneSlimmingEffectStrength
     */
    public void setCheekboneSlimmingParam(int cheekboneSlimmingEffectStrength) {
        Log.v(TAG, "setCheekboneSlimmingParam :cheekboneSlimmingEffectStrength" + cheekboneSlimmingEffectStrength);
        ZegoEffectsCheekboneSlimmingParam zegoEffectsCheekboneSlimmingParam = new ZegoEffectsCheekboneSlimmingParam();
        zegoEffectsCheekboneSlimmingParam.intensity = cheekboneSlimmingEffectStrength;
        zegoEffects.setCheekboneSlimmingParam(zegoEffectsCheekboneSlimmingParam);
    }

    /**
     * ??????????????????
     * @param enable
     */
    public void enableForeheadShortening(boolean enable) {
        Log.v(TAG, "enableForeheadShortening :enable" + enable);
        zegoEffects.enableForeheadShortening(enable);
    }

    /**
     * ????????????????????????
     * @param foreheadShorteningEffectStrength
     */
    public void setForeheadShorteningParam(int foreheadShorteningEffectStrength) {
        Log.v(TAG, "setForeheadShorteningParam :foreheadShorteningEffectStrength" + foreheadShorteningEffectStrength);
        ZegoEffectsForeheadShorteningParam zegoEffectsForeheadShorteningParam = new ZegoEffectsForeheadShorteningParam();
        zegoEffectsForeheadShorteningParam.intensity = foreheadShorteningEffectStrength;
        zegoEffects.setForeheadShorteningParam(zegoEffectsForeheadShorteningParam);
    }


    /**
     * ????????????
     * @param lookupTablePath
     */
    public void setLipstick(String lookupTablePath) {
        Log.v(TAG, "setLipstick :lookupTablePath" + lookupTablePath);
        zegoEffects.setLipstick(lookupTablePath);
    }

    /**
     * ??????????????????
     * @param lipstickEffectStrength
     */
    public void setLipstickParam(int lipstickEffectStrength) {
        Log.v(TAG, "setLipstickParam :lipstickEffectStrength" + lipstickEffectStrength);
        ZegoEffectsLipstickParam zegoEffectsLipstickParam = new ZegoEffectsLipstickParam();
        zegoEffectsLipstickParam.intensity = lipstickEffectStrength;
        zegoEffects.setLipstickParam(zegoEffectsLipstickParam);
    }

    /**
     * ????????????
     * @param lookupTablePath
     */
    public void setCheek(String lookupTablePath) {
        Log.v(TAG, "setCheek :lookupTablePath" + lookupTablePath);
        zegoEffects.setBlusher(lookupTablePath);
    }

    /**
     * ??????????????????
     * @param cheekEffectStrength
     */
    public void setCheekParam(int cheekEffectStrength) {
        Log.v(TAG, "setCheekParam :cheekEffectStrength" + cheekEffectStrength);
        ZegoEffectsBlusherParam zegoEffectsBlusherParam = new ZegoEffectsBlusherParam();
        zegoEffectsBlusherParam.intensity = cheekEffectStrength;
        zegoEffects.setBlusherParam(zegoEffectsBlusherParam);
    }

    /**
     * ????????????
     * @param lookupTablePath
     */
    public void setEyeliner(String lookupTablePath) {
        Log.v(TAG, "setEyeliner :lookupTablePath" + lookupTablePath);
        zegoEffects.setEyeliner(lookupTablePath);
    }

    /**
     * ??????????????????
     * @param eyelinerEffectStrength
     */
    public void setEyelinerParam(int eyelinerEffectStrength) {
        Log.v(TAG, "setEyelinerParam :eyelinerEffectStrength" + eyelinerEffectStrength);
        ZegoEffectsEyelinerParam zegoEffectsEyelinerParam = new ZegoEffectsEyelinerParam();
        zegoEffectsEyelinerParam.intensity = eyelinerEffectStrength;
        zegoEffects.setEyelinerParam(zegoEffectsEyelinerParam);
    }

    /**
     * ????????????
     * @param lookupTablePath
     */
    public void setEyeshadow(String lookupTablePath) {
        Log.v(TAG, "setEyeshadow :lookupTablePath" + lookupTablePath);
        zegoEffects.setEyeshadow(lookupTablePath);
    }

    /**
     * ??????????????????
     * @param eyeshadowEffectStrength
     */
    public void setEyeshadowParam(int eyeshadowEffectStrength) {
        Log.v(TAG, "setEyeshadowParam :eyeshadowEffectStrength" + eyeshadowEffectStrength);
        ZegoEffectsEyeshadowParam zegoEffectsEyeshadowParam = new ZegoEffectsEyeshadowParam();
        zegoEffectsEyeshadowParam.intensity = eyeshadowEffectStrength;
        zegoEffects.setEyeshadowParam(zegoEffectsEyeshadowParam);
    }

    /**
     * ???????????????
     * @param lookupTablePath
     */
    public void setEyelash(String lookupTablePath) {
        Log.v(TAG, "setEyelash :lookupTablePath" + lookupTablePath);
        zegoEffects.setEyelashes(lookupTablePath);
    }

    /**
     * ?????????????????????
     * @param eyelashEffectStrength
     */
    public void setEyelashParam(int eyelashEffectStrength) {
        Log.v(TAG, "setEyelashParam :eyelashEffectStrength" + eyelashEffectStrength);
        ZegoEffectsEyelashesParam zegoEffectsEyelashParam = new ZegoEffectsEyelashesParam();
        zegoEffectsEyelashParam.intensity = eyelashEffectStrength;
        zegoEffects.setEyelashesParam(zegoEffectsEyelashParam);
    }

    /**
     * ????????????
     * @param path
     */
    public void setEyesColored(String path) {
        Log.v(TAG, "setEyesColored :path" + path);
        zegoEffects.setColoredcontacts(path);
    }

    /**
     * ??????????????????
     * @param effectStrength
     */
    public void setEyesColoredParam(int effectStrength) {
        Log.v(TAG, "setEyesColoredParam :effectStrength" + effectStrength);
        ZegoEffectsColoredcontactsParam zegoEffectsColoredcontactsParam = new ZegoEffectsColoredcontactsParam();
        zegoEffectsColoredcontactsParam.intensity = effectStrength;
        zegoEffects.setColoredcontactsParam(zegoEffectsColoredcontactsParam);
    }

    /**
     * ???????????????
     * @param path
     */
    public void setBeautifyStyle(String path) {
        Log.v(TAG, "setBeautifyStyle :path" + path);
        zegoEffects.setMakeup(path);
    }

    /**
     * ?????????????????????
     * @param effectStrength
     */
    public void setBeautifyStyleParam(int effectStrength) {
        Log.v(TAG, "setBeautifyStyleParam :effectStrength" + effectStrength);
        ZegoEffectsMakeupParam param = new ZegoEffectsMakeupParam();
        param.intensity = effectStrength;
        zegoEffects.setMakeupParam(param);
    }

    /**
     * ??????????????????
     * @param lookupTablePath
     */
    public void setFilter(String lookupTablePath) {
        Log.v(TAG, "setFilter :lookupTablePath" + lookupTablePath);
        zegoEffects.setFilter(lookupTablePath);
    }

    /**
     * ????????????????????????
     * @param filterEffectStrength
     */
    public void setFilterParam(int filterEffectStrength) {
        Log.v(TAG, "setFilterParam :filterEffectStrength" + filterEffectStrength);
        ZegoEffectsFilterParam zegoEffectsFilterParam = new ZegoEffectsFilterParam();
        zegoEffectsFilterParam.intensity = filterEffectStrength;
        zegoEffects.setFilterParam(zegoEffectsFilterParam);
    }

    /**
     * ??????????????????????????????
     * @param enable
     */
    public void enableAISegment(boolean enable) {
        Log.v(TAG, "enableAISegment :enable" + enable);
        zegoEffects.enableChromaKey(false);
        zegoEffects.enablePortraitSegmentation(enable);

    }

    /**
     * ?????????????????????????????????
     * @param enable
     */
    public void enableAISegmentBackground(boolean enable) {
        Log.v(TAG, "enableAISegmentBackground :enable" + enable);
        zegoEffects.enablePortraitSegmentationBackground(enable);
    }

    /**
     * ????????????????????????
     * @param imageUrl
     */
    public void setPortraitSegmentationBackgroundPath(String imageUrl)
    {
        Log.v(TAG, "setPortraitSegmentationBackgroundPath :imageUrl" + imageUrl);
        zegoEffects.setPortraitSegmentationBackgroundPath(imageUrl, ZegoEffectsScaleMode.SCALE_TO_FILL);
    }

    /**
     * ??????????????????????????????
     */
    public void setPortraitSegmentationBackgroundTexture(int textureID, int width ,int height) {
//        Log.v(TAG, "setPortraitSegmentationBackgroundTexture :width = " + width +
//                "???height = " + height);
        ZegoEffectsVideoFrameParam videoFrameParam = new ZegoEffectsVideoFrameParam();
        videoFrameParam.width = width;
        videoFrameParam.height = height;
        videoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;
        zegoEffects.setPortraitSegmentationBackgroundTexture(textureID, videoFrameParam, ZegoEffectsScaleMode.SCALE_TO_FILL);

    }

    /**
     * ??????????????????????????????
     */
    public void setPortraitSegmentationBackgroundTextureOES(int textureID, int width ,int height) {
//        Log.v(TAG, "setPortraitSegmentationBackgroundTexture :width = " + width +
//                "???height = " + height);
        ZegoEffectsVideoFrameParam videoFrameParam = new ZegoEffectsVideoFrameParam();
        videoFrameParam.width = width;
        videoFrameParam.height = height;
        videoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;
        videoFrameParam.textureFormat = ZegoEffectsTextureFormat.TEXTURE_OES;
        zegoEffects.setPortraitSegmentationBackgroundTexture(textureID, videoFrameParam, ZegoEffectsScaleMode.ASPECT_FILL);

    }

    /**
     * ??????????????????????????????
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
     * ??????????????????????????????
     * @param enable
     */
    public void enablePortraitSegmentationBackgroundBlur(boolean enable) {
        Log.v(TAG, "enablePortraitSegmentationBackgroundBlur :enable" + enable);
        zegoEffects.enablePortraitSegmentationBackgroundBlur(enable);
    }

    /**
     * ????????????????????????????????????
     * @param backgroundBlurEffectStrength
     */
    public void setPortraitSegmentationBackgroundBlurParam(int backgroundBlurEffectStrength) {
        Log.v(TAG, "setPortraitSegmentationBackgroundBlurParam :backgroundBlurEffectStrength" + backgroundBlurEffectStrength);
        ZegoEffectsBlurParam zegoEffectsBlurParam = new ZegoEffectsBlurParam();
        zegoEffectsBlurParam.intensity = backgroundBlurEffectStrength;
        zegoEffects.setPortraitSegmentationBackgroundBlurParam(zegoEffectsBlurParam);
    }

    /**
     * ?????????????????????????????????
     * @param enable
     */
    public void enablePortraitSegmentationBackgroundMosaic(boolean enable)
    {
        Log.v(TAG, "enablePortraitSegmentationBackgroundMosaic :enable" + enable);
        zegoEffects.enablePortraitSegmentationBackgroundMosaic(enable);
    }

    /**
     * ???????????????????????????????????????
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
     * ????????????
     * @param enable
     */
    public void enableChromaKey(boolean enable) {
        Log.v(TAG, "enableChromaKey :enable" + enable);
        zegoEffects.enablePortraitSegmentation(false);
        zegoEffects.enableChromaKey(enable);

    }

    /**
     * ?????????????????????????????????
     * @param isChecked
     */
    public void enableChromaKeyBackground(boolean isChecked) {
        zegoEffects.enableChromaKeyBackground(isChecked);
    }

    /**
     * ????????????????????????
     * @param imageUrl
     */
    public void setChromaKeyBackgroundPath(String imageUrl)
    {
        zegoEffects.setChromaKeyBackgroundPath(imageUrl, ZegoEffectsScaleMode.SCALE_TO_FILL);
    }

    /**
     * ??????????????????????????????
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
     * ????????????????????????
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
     * ??????????????????????????????
     * @param enable
     */
    public void enableChromaKeyBackgroundBlur(boolean enable) {
        Log.v(TAG, "enableChromaKeyBackgroundBlur :enable" + enable);
        zegoEffects.enableChromaKeyBackgroundBlur(enable);
    }

    /**
     * ????????????????????????????????????
     * @param backgroundBlurEffectStrength
     */
    public void setChromaKeyBackgroundBlurParam(int backgroundBlurEffectStrength) {
        Log.v(TAG, "setChromaKeyBackgroundBlurParam :backgroundBlurEffectStrength" + backgroundBlurEffectStrength);
        ZegoEffectsBlurParam zegoEffectsBlurParam = new ZegoEffectsBlurParam();
        zegoEffectsBlurParam.intensity = backgroundBlurEffectStrength;
        zegoEffects.setChromaKeyBackgroundBlurParam(zegoEffectsBlurParam);
    }

    /**
     * ?????????????????????????????????
     * @param enable
     */
    public void enableChromaKeyBackgroundMosaic(boolean enable)
    {
        Log.v(TAG, "enableChromaKeyBackgroundMosaic :enable" + enable);
        zegoEffects.enableChromaKeyBackgroundMosaic(enable);
    }

    /**
     * ???????????????????????????????????????
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
     *  ??????????????????
     * @param enable
     */
    public void enableFaceDetection(boolean enable) {
        Log.v(TAG, "enableFaceDetection :enable" + enable);
        zegoEffects.enableFaceDetection(enable);
    }

    /**
     * ??????????????????
     * @param path ???????????????????????????
     */
    public void setPendant(String path){
        zegoEffects.setPendant(path);
    }


    /**
     * ????????????
     */
    public void startCamera() {
        Log.v(TAG, "startCamera ");
        // TODO ??????ai????????????;
        videoCaptureFromCamera2.onStart();
    }

    /**
     * ????????????
     */
    public void stopCamera() {
        Log.v(TAG, "stopCamera ");
        videoCaptureFromCamera2.onStop();
    }

    /**
     * ??????????????????
     */
    public void setIntervalTime(long time){
        Log.v(TAG, "setIntervalTime: " + time);
        videoCaptureFromCamera2.setIntervalTime(time);
    }


    /**
     * ??????????????????
     */
    public void stopRenderAllEffects() {
        Log.v(TAG, "stopRenderAllEffects ");
        if (videoCaptureFromCamera2 == null)
            return;

        videoCaptureFromCamera2.stopRenderAllEffects();
    }

    /**
     * ??????????????????
     */
    public void startRenderAllEffects() {
        Log.v(TAG, "startRenderAllEffects ");
        if (videoCaptureFromCamera2 == null)
            return;

        videoCaptureFromCamera2.startRenderAllEffects();
    }


    /**
     * ???????????????
     * @param size
     */
    public void setPreviewSize(PreviewSize size) {
        Log.v(TAG, "setPreviewSize ");
        videoCaptureFromCamera2.setResolution(size.getWidth(), size.getHeight());
    }

    /**
     * ?????????????????????
     * @return
     */
    public PreviewSize getPreviewDefault() {
        PreviewSize previewSize = new PreviewSize();
        previewSize.setWidth(videoCaptureFromCamera2.mCameraWidth);
        previewSize.setHeight(videoCaptureFromCamera2.mCameraHeight);
        return previewSize;
    }

    /**
     * ????????????????????????
     * @return
     */
    public PreviewSize getViewSize() {
        PreviewSize previewSize = new PreviewSize();
        previewSize.setWidth(videoCaptureFromCamera2.mViewWidth);
        previewSize.setHeight(videoCaptureFromCamera2.mViewHeight);
        return previewSize;
    }

    /**
     * ????????????
     * @param view
     */
    public void setView(TextureView view) {
        Log.v(TAG, "setView ");
        videoCaptureFromCamera2.setView(view);
    }

    /**
     * ????????????
     */
    public void setKiwiCallBack(KiwiCallback callback) {
        Log.v(TAG, "setKiwiCallBack ");
        videoCaptureFromCamera2.setKiwiCallback(callback);
    }

    /**
     * ??????????????????
     */
    public String getVersion() {
        return ZegoEffects.getVersion();
    }


    //jkhkjhkjhkjhjk
}
