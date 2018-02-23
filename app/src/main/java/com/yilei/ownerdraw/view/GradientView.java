package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/23.
 */

public class GradientView extends View{
    private static final int OFFSET = 100;
    private Paint paint;
    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(100, 100, 500, 400);
        LinearGradient lg = new LinearGradient(rect.left, rect.bottom/2, rect.right, rect.bottom/2, Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
        paint.setShader(lg);
        canvas.drawRect(rect, paint);

        canvas.translate(0, rect.height()+OFFSET);

        Rect rect1 = new Rect(rect);
        rect1.inset(-100, -100);
        lg = new LinearGradient(rect1.left, rect1.bottom/2, rect1.right, rect1.bottom/2, Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
        paint.setShader(lg);
        canvas.drawRect(rect, paint);

        canvas.translate(0, rect.height()+OFFSET);
        Rect rect2 = new Rect(rect);
        rect2.inset(100, 100);
        lg = new LinearGradient(rect2.left, rect2.bottom/2, rect2.right, rect2.bottom/2, Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
        paint.setShader(lg);
        canvas.drawRect(rect, paint);
    }
}
