package im.zego.zegoeffectsexample.sdkmanager.callback;

public interface KiwiCallback {
    /**
     * kiwi初始化后回调
     */
    void onKiwiInited();

    /**
     * 开始渲染前, 可以对输入的纹理做处理, 返回个新的纹理
     *
     * @param textureId
     * @param width
     * @param height
     * @return
     */
    int onKiwiBeforeRender(int textureId, int width, int height);


    /**
     * 单帧渲染结束, 且渲染结果已经上屏
     */
    void onKiwiRenderFinish(long timeCost);
}
