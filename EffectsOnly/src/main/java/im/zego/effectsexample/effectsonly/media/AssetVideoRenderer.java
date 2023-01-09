package im.zego.effectsexample.effectsonly.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;

public class AssetVideoRenderer extends MediaRenderer {
    String mAssetFile;
    MediaPlayer mMediaPlayer;

    public AssetVideoRenderer(Context context, String assetFile) {
        super(context);
        mAssetFile = assetFile;
    }

    @Override
    public void start() {
        super.start();
        mMediaPlayer = new MediaPlayer();
        AssetFileDescriptor afd = null;

        try {
            afd = mContext.getAssets().openFd(mAssetFile);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(true);

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    Log.i("westTest", "on prepare");
                }
            });

            mMediaPlayer.prepareAsync();

            afd.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setSurface(SurfaceTexture surfaceTexture, Surface surface) {
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void destroy() {
        super.destroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    @Override
    public int getMediaWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    @Override
    public int getMediaHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    @Override
    public void pause() {
        super.pause();
        mMediaPlayer.pause();
    }

    @Override
    public void resume() {
        super.resume();
        mMediaPlayer.start();
    }
}
