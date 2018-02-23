package com.yilei.ownerdraw.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yilei.ownerdraw.R;

/**
 * Created by 易磊 on 2018/1/23.
 * 带阴影的文字
 */

public class ShaderView extends View{
    private String text;
    private int textColor;
    private int textSize;
    private int shaderColor;

    public ShaderView(Context context) {
        this(context, null);
    }

    public ShaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShaderView);
        text = typedArray.getString(R.styleable.ShaderView_text);
        textColor = typedArray.getColor(R.styleable.ShaderView_text_color, Color.BLACK);
        textSize = typedArray.getInt(R.styleable.ShaderView_text_size, 0);
        shaderColor = typedArray.getColor(R.styleable.ShaderView_shader_color, 0);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        paint.setShadowLayer(10, 2, 2, shaderColor);

        canvas.drawText(text, 0 , 100, paint);
    }



    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void setTextColor(int color){
        this.textSize = color;
    }

    public int getTextColor(){
        return textColor;
    }

    public void setTextSize(int size){
        this.textSize = size;
    }

    public int getTextSize(){
        return textSize;
    }

    public void setShaderColor(int color){
        this.shaderColor = color;
    }

    public int getShaderColor(){
        return shaderColor;
    }
}
