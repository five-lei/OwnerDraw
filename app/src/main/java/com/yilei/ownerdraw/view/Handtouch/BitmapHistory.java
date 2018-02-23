package com.yilei.ownerdraw.view.Handtouch;

import android.graphics.Bitmap;

import java.util.Stack;

/**
 * Created by 易磊 on 2018/1/22.
 */

public class BitmapHistory {
    private static Stack<Bitmap> stack;
    private static BitmapHistory self;

    private BitmapHistory(){
        if(stack == null){
            stack = new Stack<>();
        }
    }

    public static BitmapHistory getInstance(){
        if(self == null){
            self = new BitmapHistory();
        }
        return self;
    }

    /**
     * 将当前的历史绘图结果压栈
     * @param bitmap
     */
    public void pushBitmap(Bitmap bitmap){
        int size = stack.size();
        //如果栈内元素超过5，则移除栈顶元素并回收
        if(size >= 5){
            Bitmap bmp = stack.remove(0);
            if(!bmp.isRecycled()){
                bmp.recycle();
                System.gc();
                bmp = null;
            }
        }

        stack.push(bitmap);
    }

    /**
     * 撤消
     * @return
     */
    public Bitmap reDo(){
        Bitmap bitmap = stack.pop();//删除栈顶元素，并返回被删除的元素，pop方法表示出栈，
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }

        if(stack.empty()) return null;
        //返回撤销后的位图对象  peek()方法是直接读出栈顶元素，但并不删除
        return stack.peek();
    }

    /**
     * 判断是否还能撤消
     *如果栈内元素为空则不能撤消，反之可以
     * @return true表示还能撤消，false表示不能撤消
     */
    public boolean isRedo(){
        return !stack.empty();
    }


}
