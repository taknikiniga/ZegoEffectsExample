package im.zego.effectsexample.effectsonly.media;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;

//import com.zego.renderer.gles.GlUtil;
//import com.zego.renderer.renderer.GLMediaOESImpl;

import javax.microedition.khronos.opengles.GL10;

import im.zego.zegoeffectsexample.sdkmanager.egl.GlUtil;

public abstract class MediaRenderer {
    private final static String TAG = "west:MediaRenderer";
    protected Context mContext;
    protected int mOutWidth, mOutHeight;
    protected GLMediaOESImpl.FitType mFitType;
    protected int mOESTextureId, mOutTextureId = 0;
    protected SurfaceTexture mSurfaceTexture;
    protected Surface mSurface;
    protected GLMediaOESImpl mGLMediaOESImpl;
    protected boolean mIsStarted = false;
    protected boolean mPause = false;
    private float[] mTransformMatrix = new float[16];

    MediaRenderer(Context context) {
        mContext = context;
    }

    public void start() {
        mIsStarted = true;
    }

    public int createOutputSurface(int outWidth, int outHeight,GLMediaOESImpl.FitType fitType) {
        mOutWidth = outWidth;
        mOutHeight = outHeight;
        mFitType = fitType;

        Log.i("westTest", "createOutputSurface");
        mOESTextureId = GlUtil.createOESTextureObject();
        mSurfaceTexture = new SurfaceTexture(mOESTextureId);

        mSurface = new Surface(mSurfaceTexture);
        mSurfaceTexture.setDefaultBufferSize(outWidth, outHeight);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                Log.i(TAG, "onFrameAvailable");
                requestRender();
            }
        });

        setSurface(mSurfaceTexture, mSurface);
        mOutTextureId = GlUtil.createImageTexture(null, mOutWidth, mOutHeight, GLES20.GL_RGBA);
        mGLMediaOESImpl = new GLMediaOESImpl(mContext, mOESTextureId, getMediaWidth(), getMediaHeight(), mOutTextureId, mOutWidth, mOutHeight, mFitType);
        mGLMediaOESImpl.initGL();

        return mOutTextureId;


    }

    public int updateOESTexture(){
        if(frameReady){
            mSurfaceTexture.updateTexImage();
            mSurfaceTexture.getTransformMatrix(mTransformMatrix);
        }
        return mOESTextureId;
    }

    protected abstract void setSurface(SurfaceTexture surfaceTexture, Surface surface);


    public void draw() {
        if (mPause) {
            return;
        }
        updateOESTexture();


        mGLMediaOESImpl.setTexMatrix(mTransformMatrix);
        mGLMediaOESImpl.setViewport(mOutWidth, mOutHeight);
        mGLMediaOESImpl.drawFrame();

//        mDirty = false;
    }

    public void destroy() {
        if(mGLMediaOESImpl != null)
            mGLMediaOESImpl.destroy();
    }

    public abstract int getMediaWidth();

    public abstract int getMediaHeight();

    public int getOutTextureId() {
        return mOutTextureId;
    }

    public boolean isMediaStarted() {
        return mIsStarted;
    }

    public boolean isPause() {
        return mPause;
    }

    public void pause() {
        mPause = true;
    }

    public void resume() {
        mPause = false;
    }

    private boolean frameReady = false;
    private void requestRender() {
        frameReady = true;
    }

}
