package im.zego.zegoeffectsexample.sdkmanager.entity;

public class FaceDetection {
    public int bottom;
    public int left;
    public int right;
    public int top;
    public float score;

    @Override
    public String toString() {
        return "FaceDetection{" +
                "bottom=" + bottom +
                ", left=" + left +
                ", right=" + right +
                ", top=" + top +
                ", score=" + score +
                '}';
    }
}
