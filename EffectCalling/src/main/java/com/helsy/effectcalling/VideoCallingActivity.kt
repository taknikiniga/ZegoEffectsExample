package com.helsy.effectcalling

import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.helsy.effectcalling.databinding.ActivityVideoCallingBinding
import com.helsy.effectcalling.sdk.DemoSDKHelp
import com.helsy.effectcalling.util.ZegoUtil
import im.zego.zegoeffectsexample.sdkmanager.SDKManager
import im.zego.zegoeffectsexample.sdkmanager.ZegoLicense
import im.zego.zegoeffectsexample.sdkmanager.entity.PreviewSize
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.callback.IZegoEventHandler
import im.zego.zegoexpress.constants.ZegoScenario
import im.zego.zegoexpress.constants.ZegoUpdateType
import im.zego.zegoexpress.entity.*
import org.json.JSONObject

class VideoCallingActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoCallingBinding

    private var engine : ZegoExpressEngine?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DemoSDKHelp.getSDK(this)
            .enableSmooth(true)

        initCamera()


        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
               // params.smoothIntensity = progress
                DemoSDKHelp.getSDK(this@VideoCallingActivity)
                    .setSmoothParam(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

       // initVideoCalling()
    }

    private fun initVideoCalling() {
       // createZegoEngine()

       loginRoom()

       startPublish()
    }

    private fun createZegoEngine() {
        val profile = ZegoEngineProfile()
        profile.appID = ZegoLicense.APP_ID
        profile.appSign = ZegoLicense.APP_SIGN
        profile.application = application
        profile.scenario = ZegoScenario.BROADCAST

        engine = ZegoExpressEngine.createEngine(profile,object : IZegoEventHandler() {
            // When another user in the same room publishes or stops publishing streams, you will receive a notification of stream increase or decrease of the user.
            override fun onRoomStreamUpdate(
                roomID: String?,
                updateType: ZegoUpdateType,
                streamList: ArrayList<ZegoStream>,
                extendedData: JSONObject?
            ) {
                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
                // When `updateType` is set to `ZegoUpdateType.ADD`, an audio and video stream is added, and you can call the `startPlayingStream` method to play the stream.
                if (updateType === ZegoUpdateType.ADD) {
                    // Start to play streams. Set the view for rendering the remote streams. The default view mode of the SDK is used and the entire view is filled through proportional scaling.
                    val stream: ZegoStream = streamList[0]
                    val playStreamID: String = stream.streamID
                    // In the following code, the value of `remoteUserView` is the same as that of `TextureView` of the UI. For the conciseness of the sample code, only the first stream in the list of newly added audio and video streams is played here. In a real service, it is recommended that you traverse the stream list to play each stream.
                    val playCanvas = ZegoCanvas(binding.remoteView)
                    engine!!.startPlayingStream(playStreamID, playCanvas)
                }
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

        engine?.loginRoom(roomID,user,roomConfig)

//        engine.loginRoom(roomID, user, roomConfig) { error: Int, extendedData: JSONObject? ->
//            // Room login result. This callback is sufficient if you only need to check the login result.
//            if (error == 0) {
//                // Login successful.
//                Toast.makeText(this, "Login successful.", Toast.LENGTH_LONG).show()
//            } else {
//                // Login failed. For details, see Error codes doc.
//                Toast.makeText(
//                    this,
//                    "Login failed. For details, see [Error codes](https://docs.zegocloud.com/article/5548).",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
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