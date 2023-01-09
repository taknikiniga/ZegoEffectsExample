package im.zego.zegoeffectsexample.sdkmanager.net;

import org.jetbrains.annotations.NotNull;

public interface IAsyncGetCallback<T>{
    void onResponse(int errorCode, @NotNull String message, T responseJsonBean);
}
