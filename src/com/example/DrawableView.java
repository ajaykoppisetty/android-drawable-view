package com.example;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author me@andresteingress.com
 */
public class DrawableView extends View {

    private Drawable drawable;
    private Matrix matrix = new Matrix();

    private int drawableWidth, drawableHeight;
    private int viewWidth, viewHeight;

    public DrawableView(Context context) {
        super(context);
    }

    public DrawableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.com_example_DrawableView, defStyle, 0);
        drawable = typedArray.getDrawable(R.styleable.com_example_DrawableView_src);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        drawableWidth  = drawable.getIntrinsicWidth();
        drawableHeight = drawable.getIntrinsicHeight();

        int width  = Math.max(drawableWidth, getSuggestedMinimumWidth());
        int height = Math.max(drawableHeight, getSuggestedMinimumHeight());

        viewWidth  = resolveSize(width, widthMeasureSpec);
        viewHeight = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(viewWidth, viewHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int saveCount = canvas.save();

        float scale;
        float dx;
        float dy;

        if (drawableWidth <= viewWidth && drawableHeight <= viewHeight) {
            scale = 1.0f;
        } else {
            scale = Math.min((float) viewWidth / (float) drawableWidth,
                    (float) viewHeight / (float) drawableHeight);
        }

        dx = (int) ((viewWidth - drawableWidth * scale) * 0.5f + 0.5f);
        dy = (int) ((viewHeight - drawableHeight * scale) * 0.5f + 0.5f);

        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);

        canvas.concat(matrix);

        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        drawable.draw(canvas);

        canvas.restoreToCount(saveCount);
    }
}
