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
import im.zego.zegoexpress.callback.IZegoEventHandler
import im.zego.zegoexpress.constants.*
import im.zego.zegoexpress.entity.*
import im.zego.zegoexpress.internal.ZegoExpressEngineInternalImpl
import org.json.JSONObject


class VideoCallingActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoCallingBinding

    private var engine: ZegoExpressEngine? = null
    var effectsVideoFrameParam: ZegoEffectsVideoFrameParam? = null
    var effects: ZegoEffects? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        effectsVideoFrameParam = ZegoEffectsVideoFrameParam()

      //  DemoSDKHelp.getSDK(this).enableSmooth(true)

        // Video Calling Effect

//        effects = ZegoEffects.create(ZegoLicense.effectsLicense,this)
//        effects?.initEnv(1280,720)

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

        initVideoCalling()
      //  initCamera()
       // initEffect()


    }

    private fun initVideoCalling() {
        createEngine();
        // Listen for common events.
        setEventHandler();
        loginRoom();
        // Log in to a room.
        // Start the preview and stream publishing.
        startPublish();
    }
    fun createEngine() {

        val profile = ZegoEngineProfile()
        profile.appID =
            ZegoLicense.APP_ID
        profile.appSign =
            ZegoLicense.APP_SIGN
        profile.scenario =
            ZegoScenario.GENERAL
        profile.application = application

      //  ZegoExpressEngineInternalImpl.createEngine()
        engine = ZegoExpressEngine.createEngine(profile, null)

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
        val previewCanvas = ZegoCanvas(binding.textureView)
        engine!!.startPreview(previewCanvas)
        engine!!.startPublishingStream("stream2")
    }

    fun setEventHandler() {
        engine!!.setEventHandler(object : IZegoEventHandler() {
            // When another user in the same room publishes or stops publishing streams, you will receive a notification of stream increase or decrease of the user.
            override fun onRoomStreamUpdate(
                roomID: String?,
                updateType: ZegoUpdateType,
                streamList: ArrayList<ZegoStream>,
                extendedData: JSONObject?
            ) {
                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
                // When `updateType` is set to `ZegoUpdateType.ADD`, an audio and video stream is added, and you can call the `startPlayingStream` method to play the stream.
                if (updateType == ZegoUpdateType.ADD) {
                    // Start to play streams. Set the view for rendering the remote streams. The default view mode of the SDK is used and the entire view is filled through proportional scaling.
                    val stream = streamList[0]
                    val playStreamID = stream.streamID
                    // In the following code, the value of `remoteUserView` is the same as that of `TextureView` of the UI. For the conciseness of the sample code, only the first stream in the list of newly added audio and video streams is played here. In a real service, it is recommended that you traverse the stream list to play each stream.
                    val playCanvas = ZegoCanvas(binding.remoteView)
                    engine!!.startPlayingStream(playStreamID, playCanvas)
                }
            }
        })
    }

    fun loginRoom() {
        // The `ZegoUser` constructor `public ZegoUser(String userID)` will set `userName` to the value of `userID`. The `userID` and `userName` parameters cannot be set to `null`. Otherwise, logging in to a room will fail.
        val user = ZegoUser("user2")
        val roomConfig = ZegoRoomConfig()
        roomConfig.isUserStatusNotify = true
        val roomID = "room1"
        engine!!.loginRoom(
            roomID, user, roomConfig
        ) { error: Int, extendedData: JSONObject? ->
            if (error == 0) {
                Toast.makeText(
                    this,
                    "Login successful.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
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