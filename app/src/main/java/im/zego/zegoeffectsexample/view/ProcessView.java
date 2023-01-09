package im.zego.zegoeffectsexample.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import im.zego.zegoeffectsexample.R;


public class ProcessView extends FrameLayout {

    protected ViewGroup mParentView;

    private View mVPbBg;
    private ProgressBar mPbLoading;
    private boolean mIsShowShade = true;
    private boolean mCancelable = false;
    private float mBgAlpha = 0.5f;

    public ProcessView(@NonNull Context context) {
        super(context);
        initView();
    }

    public ProcessView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProcessView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_process, this, true);
        mVPbBg = view.findViewById(R.id.v_pb_bg);
        mPbLoading = view.findViewById(R.id.pb_loading);
    }

    public ProcessView setShadeAlpha(float alpha)
    {
        mBgAlpha = alpha;
        return this;
    }

    public ProcessView setIsShowShade(boolean isShowShade)
    {
        mIsShowShade = isShowShade;
        return this;
    }

    public ProcessView setCancelable(boolean isShowShade)
    {
        mCancelable = isShowShade;
        return this;
    }

    public ProcessView show(ViewGroup view)
    {
        mParentView = view;

        final ViewParent viewParent = getParent();

        if (viewParent instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) viewParent;
            viewGroup.removeView(this);
        }

        int color = 0;
        if(mIsShowShade)
        {
            color = ((int) (mBgAlpha * 255.0f + 0.5f) << 24) ;
        }

        mVPbBg.setBackgroundColor(color);

        if(mCancelable)
        {
            mVPbBg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();
                }
            });
            mVPbBg.setOnTouchListener(null);
        }else {
            mVPbBg.setOnClickListener(null);
            mVPbBg.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        mParentView.addView(this);

        return this;
    }

    public void dismiss()
    {
        final ViewParent viewParent = getParent();
        if (viewParent instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) viewParent;
            viewGroup.removeView(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY));;
    }
}
