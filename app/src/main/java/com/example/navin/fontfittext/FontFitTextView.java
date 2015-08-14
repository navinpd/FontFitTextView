package com.example.navin.fontfittext;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

public class FontFitTextView extends EditText {

    private float textSize;

    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        //max size defaults to the initially specified text size unless it is too small
        textSize = getTextSize();
    }

    /**
     * Re size the font so the specified text fits in the text box
     * assuming the parent of text box is the specified width.
     * You can very the minimum by defining size
     */
    private void refitText(String text, int textWidth)
    {
        if (textWidth <= 0)
            return;
        int targetWidth = ((View)this.getParent()).getWidth() - this.getTotalPaddingLeft() - this.getTotalPaddingRight();
        float hi = textSize;
        float lo = textSize / 3;
        final float threshold = 0.5f; // How close we have to be

        mTestPaint.set(this.getPaint());

        while((hi - lo) > threshold) {
            float size = (hi+lo)/2;
            mTestPaint.setTextSize(size);
            if (mTestPaint.measureText(text) >= targetWidth)
                hi = size; // too big
            else
                lo = size; // too small
        }
        // Use lo so that we under shoot rather than overshoot
        // Also use Pixel, not SP because here we are already working with scaled real pixel values
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        refitText(this.getText().toString(), this.getWidth());
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }


    //Attributes
    private Paint mTestPaint;
}