package com.yilei.ownerdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 易磊 on 2018/1/24.
 * 五子棋
 */

public class FiveChessView extends View{
    private static final int SIZE = 120;//棋子的尺寸
    private static final int OFFSET = 10;//发光点偏移的位置
    private Paint paint;
    private Paint chessPaint;


    public FiveChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chessPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        int rows = height / SIZE;
        int cols = width / SIZE;

        //画棋盘
        drawChessBoard(canvas, rows, cols);

        //画棋子
        drawChess(canvas, 4, 4, ChessType.BLACK);
        drawChess(canvas, 4, 5, ChessType.BLACK);
        drawChess(canvas, 5, 4, ChessType.WHITE);
        drawChess(canvas, 3, 5, ChessType.WHITE);
//        Rect rect = new Rect(100, 100, 500, 500);
//        RadialGradient rg = new RadialGradient(300, 300, 200, Color.RED, Color.GREEN, Shader.TileMode.MIRROR);
//
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setShader(rg);
//
//        canvas.drawRect(rect, paint);
//
//        canvas.translate(510, 0);
//        canvas.drawOval(new RectF(rect), paint);
    }

    /**
     * 画棋盘
     * @param canvas
     * @param rows
     * @param cols
     */
    private void drawChessBoard(Canvas canvas, int rows, int cols){
        paint.setColor(Color.GRAY);
        paint.setShadowLayer(0, 0, 0, Color.GRAY);//取消阴影

        for(int i = 0; i < rows+1; i++){
            canvas.drawLine(0, i * SIZE, cols * SIZE, i * SIZE, paint);
        }

        for(int i = 0; i < cols+1; i++){
            canvas.drawLine(i * SIZE, 0, i * SIZE, rows * SIZE, paint);
        }
    }

    /**
     * 画棋子
     * @param canvas
     * @param x 行
     * @param y 列
     * @param chessType 棋子类型
     */
    private void drawChess(Canvas canvas, int x, int y, ChessType chessType){
        //定义棋子颜色
        int colorOuter = chessType == ChessType.BLACK ? Color.BLACK : Color.GRAY;
        int colorInner = Color.WHITE;
        //定义径向渐变，发光点向右下角偏移OFFSET
        RadialGradient rg = new RadialGradient(x * SIZE + OFFSET, y * SIZE + OFFSET, SIZE / 1.5f, colorInner, colorOuter, Shader.TileMode.CLAMP);
        chessPaint.setShader(rg);

        //定义阴影
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, chessPaint);
        chessPaint.setShadowLayer(6, 4, 4, Color.parseColor("#AACCCCCC"));
        canvas.drawCircle(x * SIZE, y * SIZE, SIZE / 2, chessPaint);
    }

    enum ChessType{
        BLACK, WHITE
    }
}
