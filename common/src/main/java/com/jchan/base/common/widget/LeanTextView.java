package com.jchan.base.common.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jchan.base.common.R;

/**
 * @create time: 2021/7/26
 * @author: JChan
 * @description:
 */
public class LeanTextView extends View {
    private TextPaint mTextPaint;
    private String mText;
    Rect text_bounds = new Rect();
    final static int DEFAULT_TEXT_SIZE = 15;
    final static int DEFAULT_TEXT_COLOR = 0xFF000000;
    private int direction;
    public LeanTextView(Context context) {
        super(context);
        init();
    }

    public LeanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeanTextView);
        CharSequence s = a.getString(R.styleable.LeanTextView_text);
        if (s != null) {
            mText = s.toString();
        }
        int textSize = a.getDimensionPixelOffset(R.styleable.LeanTextView_textSize, DEFAULT_TEXT_SIZE);
        if (textSize > 0) {
            mTextPaint.setTextSize(textSize);
        }

        mTextPaint.setColor(a.getColor(R.styleable.LeanTextView_textColor, DEFAULT_TEXT_COLOR));
        direction = a.getInt(R.styleable.LeanTextView_lean_direction,0);
        a.recycle();

        requestLayout();
        invalidate();
    }

    private final void init() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DEFAULT_TEXT_SIZE);
        mTextPaint.setColor(DEFAULT_TEXT_COLOR);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setText(String text) {
        mText = text;
        requestLayout();
        invalidate();
    }

    public void setTextSize(int size) {
        mTextPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTextPaint.getTextBounds(mText, 0, mText.length(), text_bounds);
        setMeasuredDimension(
                measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
//            result = text_bounds.height() + getPaddingLeft() + getPaddingRight();
            result = text_bounds.height();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
//            result = text_bounds.width() + getPaddingTop() + getPaddingBottom();
            result = text_bounds.width();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX=0;
        int startY=0;
        int stopY=getHeight();
        Path path=new Path();
        Log.e("JChan","getWidth "+getWidth()+" after "+(getWidth() >> 1));
        Log.e("JChan","text_bounds.height "+text_bounds.height()+" after "+(text_bounds.height() >> 1));
        if(direction==0){
            startX=(getWidth()>>1)-(text_bounds.height()>>1);
            path.moveTo(startX, startY);
            path.lineTo(startX, stopY);
        }else{
            startX=(getWidth()>>1)+(text_bounds.height()>>1);
            path.moveTo(startX, stopY);
            path.lineTo(startX, startY);
        }
        canvas.drawTextOnPath(mText, path, 0, 0, mTextPaint);
    }

}

