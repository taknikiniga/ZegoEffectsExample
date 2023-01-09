package im.zego.effectsexample.effectsforexpress.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import im.zego.zegoeffectsexample.sdkmanager.entity.FaceDetection;


public class FaceDetectionView extends View {


    int numberOfFaceDetected = 4;       //实际检测到的人脸数
    Bitmap myBitmap;
    private double scale;

    public FaceDetectionView(Context context) {     //view类的构造函数，必须有
        super(context);
    }

    public FaceDetectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FaceDetectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint myPaint = new Paint();


    protected void onDraw(Canvas canvas) {

        myPaint.setColor(Color.GREEN);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3);

        if (faceDetections != null) {
            for (int i = 0; i < faceDetections.length; i++) {
                if (scale != 1.0) {
                    canvas.drawRect((float) (faceDetections[i].left * scale),
                            (float) (faceDetections[i].top * scale),
                            (float) (faceDetections[i].right * scale),
                            (float) (faceDetections[i].bottom * scale),
                            myPaint);
                } else {
                    canvas.drawRect(faceDetections[i].left,
                            faceDetections[i].top,
                            faceDetections[i].right,
                            faceDetections[i].bottom,
                            myPaint);
                }
            }
        }

    }

    FaceDetection[] faceDetections;

    public void setFaceDetectionRefresh(FaceDetection[] faceDetections, double scale) {
        this.faceDetections = faceDetections;
        this.scale = scale;
        this.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
                requestLayout();
            }
        });
    }
}
