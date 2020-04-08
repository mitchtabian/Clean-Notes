package com.codingwithmitch.cleannotes.core.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.yydcdut.markdown.MarkdownEditText;


/**
 * This is not used in the application. I just left it here in case you
 * were interesting in using it in other projects.
 */
public class LinedEditText extends AppCompatEditText {

    private static final String TAG = "LinedEditText";

    private Rect mRect;
    private Paint mPaint;


    // we need this constructor for LayoutInflater
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0x80808066); // Color of the lines on paper

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // get the height of the view
        int height = ((View)this.getParent()).getHeight();

        int lineHeight = getLineHeight();
        int numberOfLines = height / lineHeight;

        Rect r = mRect;
        Paint paint = mPaint;

        int baseline = getLineBounds(0, r);

        for (int i = 0; i < numberOfLines; i++) {

            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);

            baseline += lineHeight;
        }

        super.onDraw(canvas);
    }

}