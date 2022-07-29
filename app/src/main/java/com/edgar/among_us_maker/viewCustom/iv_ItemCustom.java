package com.edgar.among_us_maker.viewCustom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class iv_ItemCustom extends androidx.appcompat.widget.AppCompatImageView {

    public iv_ItemCustom(Context context) {
        super(context);
    }

    public iv_ItemCustom(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public iv_ItemCustom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int a = this.getMeasuredWidth();
        this.setMeasuredDimension(a, a);
    }
}
