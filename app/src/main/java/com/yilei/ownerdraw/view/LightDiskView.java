package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/24.
 */

public class LightDiskView extends View{
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRotate;
    private Shader mShader;
    private Matrix mMatrix = new Matrix();


    public LightDiskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);

        float x = 160;
        float y = 100;
        //设置扫描渐变
        mShader = new SweepGradient(x, y, new int[]{Color.GREEN, Color.RED, Color.BLUE, Color.GREEN}, null);
        mPaint.setShader(mShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = mPaint;
        float x = 160;
        float y = 100;
        canvas.translate(300, 300);
        canvas.drawColor(Color.WHITE);
        mMatrix.setRotate(mRotate, x, y);
        mShader.setLocalMatrix(mMatrix);
        mRotate += 3;
        if(mRotate >= 360){
            mRotate = 0;
        }

        invalidate();
        canvas.drawCircle(x, y, 380, paint);
    }
}
