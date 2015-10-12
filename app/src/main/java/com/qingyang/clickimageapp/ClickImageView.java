package com.qingyang.clickimageapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by on 15/10/12.
 * 转载，大牛分享的源码，在github上没有找到，故上传以便使用
 */
public class ClickImageView extends ImageView {

    private Animator anim1;
    private Animator anim2;
    private int mHeight;
    private int mWidth;
    private float mX, mY;
    private Handler mHandler = new Handler();

    private ClickListener listener;

    public ClickImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mX = getX();
        mY = getY();
    }

    private void init() {

        PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.9f);
        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat(
                "scaleY", 1f, 0.9f);
        anim1 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_1, valuesHolder_2);
        anim1.setDuration(200);
        anim1.setInterpolator(new LinearInterpolator());

        PropertyValuesHolder valueHolder_3 = PropertyValuesHolder.ofFloat(
                "scaleX", 0.9f, 1f);
        PropertyValuesHolder valuesHolder_4 = PropertyValuesHolder.ofFloat(
                "scaleY", 0.9f, 1f);
        anim2 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_3,
                valuesHolder_4);
        anim2.setDuration(200);
        anim2.setInterpolator(new LinearInterpolator());
    }

    public void setClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        anim2.end();
                        anim1.start();
                    }
                });
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        anim1.end();
                        anim2.start();
                    }
                });
                if (listener != null) {

                    listener.onClick();
                }
                //EventBus.getDefault().post(BusEvent.TYPE);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    //按下的点是否在View内
    protected boolean innerImageView(float x, float y) {

        if (x >= mX && y <= mX + mWidth) {
            if (y >= mY && y <= mY + mHeight) {
                return true;
            }
        }
        return false;
    }

    /**
     *  点击事件处理回调　
     * @author renzhiqiang
     *
     */
    public interface ClickListener {

        public void onClick();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
    }
}
