package com.helsy.effectcalling

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.helsy.effectcalling.databinding.ActivityVideoCallingBinding
import com.helsy.effectcalling.sdk.DemoSDKHelp
import im.zego.effects.ZegoEffects
import im.zego.effects.entity.ZegoEffectsVideoFrameParam
import im.zego.effects.enums.ZegoEffectsVideoFrameFormat
import im.zego.zegoeffectsexample.sdkmanager.ZegoLicense
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler
import im.zego.zegoexpress.constants.*
import im.zego.zegoexpress.entity.*
import org.json.JSONObject


class VideoCallingActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoCallingBinding

    private var engine: ZegoExpressEngine?=null
    var effectsVideoFrameParam: ZegoEffectsVideoFrameParam? = null
    var effects: ZegoEffects? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)



        initVideoCalling()

        effectsVideoFrameParam = ZegoEffectsVideoFrameParam()

        DemoSDKHelp.getSDK(this)
            .enableSmooth(true)

        initCamera()


        // Video Calling Effect

        effects = ZegoEffects.create(ZegoLicense.effectsLicense,this)
        effects?.initEnv(1280,720)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

                // param.smoothIntensity = progress
                DemoSDKHelp.getSDK(this@VideoCallingActivity)
                    .setSmoothParam(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })


        initEffect()


    }

    private fun initVideoCalling() {
         createZegoEngine()

        loginRoom()
//
      startPublish()
    }

    private fun createZegoEngine() {
//        engine = ZegoExpressEngine.getEngine()
        try {
            val profile = ZegoEngineProfile()
//            profile.appID =
//            //profile.appSign =
//            profile.application = application
//            profile.scenario = ZegoScenario.GENERAL

            // Init ZegoExpress SDK
            if (engine!=null){
                engine = ZegoExpressEngine.createEngine(
                    ZegoLicense.APP_ID, ZegoLicense.APP_SIGN, true, ZegoScenario.GENERAL,
                    application, null
                )
            }

        }catch (e:Exception){
            e.printStackTrace()
        }


//        engine = ZegoExpressEngine.createEngine(profile, object : IZegoEventHandler() {
//            // When another user in the same room publishes or stops publishing streams, you will receive a notification of stream increase or decrease of the user.
//            override fun onRoomStreamUpdate(
//                roomID: String?,
//                updateType: ZegoUpdateType,
//                streamList: ArrayList<ZegoStream>,
//                extendedData: JSONObject?
//            ) {
//                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
//                // When `updateType` is set to `ZegoUpdateType.ADD`, an audio and video stream is added, and you can call the `startPlayingStream` method to play the stream.
//                if (updateType === ZegoUpdateType.ADD) {
//                    // Start to play streams. Set the view for rendering the remote streams. The default view mode of the SDK is used and the entire view is filled through proportional scaling.
//                    val stream: ZegoStream = streamList[0]
//                    val playStreamID: String = stream.streamID
//                    // In the following code, the value of `remoteUserView` is the same as that of `TextureView` of the UI. For the conciseness of the sample code, only the first stream in the list of newly added audio and video streams is played here. In a real service, it is recommended that you traverse the stream list to play each stream.
//                    val playCanvas = ZegoCanvas(binding.remoteView)
//                    engine?.startPlayingStream(playStreamID, playCanvas)
//                }
//            }
//        })
    }

    fun initEffect() {

        effectsVideoFrameParam?.format = ZegoEffectsVideoFrameFormat.RGBA32

        val zegoVideoConfig = ZegoVideoConfig(ZegoVideoConfigPreset.PRESET_720P)
        engine?.videoConfig = zegoVideoConfig
        val config = ZegoCustomVideoProcessConfig()
        config.bufferType = ZegoVideoBufferType.GL_TEXTURE_2D

        engine?.enableCustomVideoProcessing(true, config, ZegoPublishChannel.MAIN)

        engine?.setCustomVideoProcessHandler(object : IZegoCustomVideoProcessHandler() {
            override fun onStart(channel: ZegoPublishChannel) {
                Log.i("ZEGO", "[Express] [onStart]")
                effects?.initEnv(720, 1280)
            }

            override fun onStop(channel: ZegoPublishChannel) {
                Log.i("ZEGO", "[Express] [onStop]")
                effects?.uninitEnv()
            }

            override fun onCapturedUnprocessedTextureData(
                textureID: Int,
                width: Int,
                height: Int,
                referenceTimeMillisecond: Long,
                channel: ZegoPublishChannel
            ) {
                Log.i(
                    "ZEGO",
                    "[Express] [onCapturedUnprocessedTextureData] textureID: $textureID, width: $width, height: $height, ts: $referenceTimeMillisecond"
                )
                // Receive texture from ZegoExpressEngine
//                super.onCapturedUnprocessedTextureData(textureID, width, height, referenceTimeMillisecond, channel);
                effectsVideoFrameParam!!.width = width
                effectsVideoFrameParam!!.height = height

                // Process buffer by ZegoEffects
                val processedTextureID: Int =
                    effects!!.processTexture(textureID, effectsVideoFrameParam)

                // Send processed texture to ZegoExpressEngine
                engine?.sendCustomVideoProcessedTextureData(
                    processedTextureID,
                    width,
                    height,
                    referenceTimeMillisecond
                )
            }
        })

    }


    fun startPublish() {
        // Set the local preview view and start the preview. The default view mode of the SDK is used and the entire view is filled through proportional scaling.
        val previewCanvas = ZegoCanvas(binding.textureView)
        engine!!.startPreview(previewCanvas)

        // Start to publish streams.
        // After calling the `loginRoom` method, call this method to publish streams.
        // Ensure that the value of `streamID` is globally unique under the same AppID. If different streams are published with the same `streamID`, the ones that are published after the first one will fail.
        engine!!.startPublishingStream("stream2")
    }

    fun loginRoom() {
        // The `ZegoUser` constructor `public ZegoUser(String userID)` will set `userName` to the value of `userID`. The `userID` and `userName` parameters cannot be set to `null`. Otherwise, logging in to a room will fail.
        val user = ZegoUser("user2")
        val roomConfig = ZegoRoomConfig()
        // If you use the AppSign for authentication, you do not need to set the `token` parameter. If you want to use the Token for authentication, which is securer, see [Guide for upgrading the authentication mode from using the AppSign to Token](https://docs.zegocloud.com/faq/token_upgrade).
        //roomConfig.token = ;
        // The `onRoomUserUpdate` callback can be received only when `ZegoRoomConfig` in which the `isUserStatusNotify` parameter is set to `true` is passed.
        roomConfig.isUserStatusNotify = true

        // The value of `roomID` is generated locally and must be globally unique. Users must log in to the same room to call each other.
        val roomID = "room1"

        // Log in to a room.

       // engine?.loginRoom(roomID, user, roomConfig)

        engine?.loginRoom(roomID, user, roomConfig) { error: Int, extendedData: JSONObject? ->
            // Room login result. This callback is sufficient if you only need to check the login result.
            if (error == 0) {
                // Login successful.
                Toast.makeText(this, "Login successful.", Toast.LENGTH_LONG).show()
            } else {
                // Login failed. For details, see Error codes doc.
                Toast.makeText(
                    this,
                    "Login failed. For details, see [Error codes](https://docs.zegocloud.com/article/5548).",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initCamera() {
        DemoSDKHelp.getSDK(this)
            .setView(binding.textureView)
        binding.textureView.post(Runnable {
            val size = PreviewSize()
            size.width = binding.textureView.width
            size.height = binding.textureView.height
            DemoSDKHelp.getSDK(this)
                .setPreviewSize(size)
            DemoSDKHelp.getSDK(this)
                .startCamera()
        })
    }


}