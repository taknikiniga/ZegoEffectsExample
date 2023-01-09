package im.zego.effectsexample.effectsonly.media;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import im.zego.zegoeffectsexample.sdkmanager.egl.GlUtil;


public class GLMediaOESImpl {

    protected int mProgramObject = -1;
    protected int mWidth;
    protected int mHeight;
    protected FloatBuffer mVertices;
    protected FloatBuffer mTexCoords;
    protected Context mContext;
    protected int mTexID;
    protected static String TAG = "GLRendererImpl";
    protected float[] texMatrix;
    protected int outputTextureID = -1;

    protected final float[] mVerticesData = {
            -1.0f, -1.0f, 0,
            1.0f, -1.0f, 0,
            -1.0f, 1.0f, 0,
            1.0f, 1.0f, 0
    };
    protected float[] mTexCoordsData = new float[]{
            0, 1,
            1, 1,
            0, 0,
            1, 0
    };

    protected int fboId = -1;

    public enum FitType {
        scale,//拉伸填充满
        centerFill, //将目标纹理填充满（高度一致，水平居中，裁剪掉左右两边；宽度一致，垂直居中，裁剪掉上下）
    }

    public GLMediaOESImpl(){}

    public GLMediaOESImpl(Context ctx,
                          int inputTextureID,
                          int inputWidth,
                          int inputHeight,
                          int outputTextureID,
                          int outputWidth,
                          int outputHeight,
                          FitType fitType) {
        if (fitType == FitType.scale) {
            mTexCoordsData = new float[]{
                    0, 1,
                    1, 1,
                    0, 0,
                    1, 0
            };
        } else if (fitType == FitType.centerFill) {
            if (inputWidth / (float) inputHeight > outputWidth / (float) outputHeight) {
                //高度一致，水平居中，裁剪掉左右两边
                float showXLength = inputHeight * outputWidth / (float) outputHeight;//真正在屏幕上显示的x轴长度
                float leftX = (inputWidth - showXLength) / 2.0f;
                float rightX = inputWidth - leftX;
                mTexCoordsData = new float[]{
                        leftX / inputWidth, 1,
                        rightX / inputWidth, 1,
                        leftX / inputWidth, 0,
                        rightX / inputWidth, 0
                };
            } else {
                //宽度一致，垂直居中，裁剪掉上下
                float showYLength = inputWidth * outputHeight / (float) outputWidth;
                float topY = (inputHeight - showYLength) / 2.0f;
                float bottomY = inputHeight - topY;
                mTexCoordsData = new float[]{
                        0, bottomY / (float) inputHeight,
                        1, bottomY / (float) inputHeight,
                        0, topY / (float) inputHeight,
                        1, topY / (float) inputHeight
                };
            }
        }
        mTexID = inputTextureID;
        this.outputTextureID = outputTextureID;

        mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);

        mTexCoords = ByteBuffer.allocateDirect(mTexCoordsData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexCoords.put(mTexCoordsData).position(0);

        mContext = ctx;

    }

    public void setViewport(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void initGL() {
        comipleAndLinkProgram();

//        loadTexture();
        fboId = GlUtil.createFrameBuffer();

        GLES20.glClearColor(0, 0, 0, 0);
    }

    public void resize(int width, int height) {
        mWidth = width;
        mHeight = height;

    }

    public void setTexMatrix(float[] texMatrix) {
        this.texMatrix = texMatrix;
    }

    public void drawFrame() {
        bindOutput(this.outputTextureID);

        // TODO Auto-generated method stub
        GLES20.glViewport(0, 0, mWidth, mHeight);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgramObject);

        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, mTexCoords);
        GLES20.glEnableVertexAttribArray(1);

        int muTexMatrixLoc = GLES20.glGetUniformLocation(mProgramObject, "uTexMatrix");
        GLES20.glUniformMatrix4fv(muTexMatrixLoc, 1, false, texMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTexID);
        int loc = GLES20.glGetUniformLocation(mProgramObject, "u_Texture");
        GLES20.glUniform1i(loc, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

    }

    protected void bindOutput(int texID) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texID, 0);
    }


//    private void loadTexture() {
//        Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
//        if (b != null) {
//            int []texID = new int[1];
//            GLES20.glGenTextures(1, texID, 0);
//            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texID[0]);
//            mTexID = texID[0];
//
//            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
//                    GLES20.GL_LINEAR);
//            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
//                    GLES20.GL_LINEAR);
//
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
//                    GLES20.GL_REPEAT);
//            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
//                    GLES20.GL_REPEAT);
//
//            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
//            b.recycle();
//        }
//    }

    protected int loadShader(int shaderType, String shaderSource) {
        int shader;
        int[] compiled = new int[1];

        // Create the shader object
        shader = GLES20.glCreateShader(shaderType);

        if (shader == 0)
            return 0;

        // Load the shader source
        GLES20.glShaderSource(shader, shaderSource);

        // Compile the shader
        GLES20.glCompileShader(shader);

        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    protected void comipleAndLinkProgram() {
        String vShaderStr = "attribute vec4 a_position;    \n"
                + "attribute vec2 a_texCoords; \n"
                + "uniform mat4 uTexMatrix;\n"
                + "varying vec2 v_texCoords; \n"
                + "void main()                  \n"
                + "{                            \n"
                + "   gl_Position = a_position;  \n"
                + "    v_texCoords = (uTexMatrix * vec4(a_texCoords, 0.0, 1.0)).xy; \n"
                + "}                            \n";

        String fShaderStr = "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;					  \n"
                + "uniform samplerExternalOES u_Texture; \n"
                + "varying vec2 v_texCoords; \n"
                + "void main()                                  \n"
                + "{                                            \n"
                + "  gl_FragColor = texture2D(u_Texture, v_texCoords) ;\n"
                + "}                                            \n";

        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];

        // Load the vertex/fragment shaders
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vShaderStr);
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderStr);

        // Create the program object
        programObject = GLES20.glCreateProgram();

        if (programObject == 0)
            return;

        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);

        // Bind vPosition to attribute 0
        GLES20.glBindAttribLocation(programObject, 0, "a_position");
        GLES20.glBindAttribLocation(programObject, 1, "a_texCoords");

        // Link the program
        GLES20.glLinkProgram(programObject);

        // Check the link status
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0) {
            Log.e(TAG, "Error linking program:");
            Log.e(TAG, GLES20.glGetProgramInfoLog(programObject));
            GLES20.glDeleteProgram(programObject);
            return;
        }

        mProgramObject = programObject;
    }

    public void destroy() {
        if (mProgramObject >= 0) {
            GLES20.glDeleteFramebuffers(1, new int[]{fboId}, 0);
            GLES20.glDeleteProgram(mProgramObject);
        }

    }
}