package com.johnnyfivedev;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.johnnyfivedev.library.R;

public class ShadowLayout extends FrameLayout {

    private Context context;

    private int shadowColor;
    private float shadowRadius;
    private float cornerRadius;
    private float dx;
    private float dy;

    private boolean invalidateShadowOnSizeChanged = true;
    private boolean forceInvalidateShadow = false;


    //region ===================== Constructors ======================

    public ShadowLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    //endregion

    //region ===================== Public ======================

   /* public void setInvalidateShadowOnSizeChanged(boolean invalidateShadowOnSizeChanged) {
        this.invalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged;
    }*/

    /*public void invalidateShadow() {
        forceInvalidateShadow = true;
        requestLayout();
        invalidate();
    }*/

    /*public void applyShadowColor(int shadowColorId) {
        shadowColor = ContextCompat.getColor(context, shadowColorId);
        invalidateShadow();
    }*/

    //endregion

    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0 && (getBackground() == null || invalidateShadowOnSizeChanged || forceInvalidateShadow)) {
            forceInvalidateShadow = false;
            setBackgroundCompat(w, h);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (forceInvalidateShadow) {
            forceInvalidateShadow = false;
            setBackgroundCompat(right - left, bottom - top);
        }
    }

    //region ===================== Internal ======================

    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        initAttributes(attrs);

        int xPadding = (int) (shadowRadius + Math.abs(dx));
        int yPadding = (int) (shadowRadius + Math.abs(dy));
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, cornerRadius, shadowRadius, dx, dy, shadowColor, Color.TRANSPARENT);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
       /* if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable);
        } else {*/
        setBackground(drawable);
        //}
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray typedArray = getTypedArray(context, attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            try {
                cornerRadius = typedArray.getDimension(R.styleable.ShadowLayout_sl_cornerRadius, getResources().getDimension(R.dimen.default_corner_radius));
                shadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_sl_shadowRadius, getResources().getDimension(R.dimen.default_shadow_radius));
                dx = typedArray.getDimension(R.styleable.ShadowLayout_sl_dx, 0);
                dy = typedArray.getDimension(R.styleable.ShadowLayout_sl_dy, 0);
                shadowColor = typedArray.getColor(R.styleable.ShadowLayout_sl_shadowColor, ContextCompat.getColor(context, R.color.default_shadow_color));
            } finally {
                typedArray.recycle();
            }
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {
        // Be careful with Bitmap.Config enum. It might mess with a shadow color
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (dy > 0) {
            shadowRect.top += dy;
            shadowRect.bottom -= dy;
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy);
            shadowRect.bottom -= Math.abs(dy);
        }

        if (dx > 0) {
            shadowRect.left += dx;
            shadowRect.right -= dx;
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx);
            shadowRect.right -= Math.abs(dx);
        }

        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(fillColor);
        shadowPaint.setStyle(Paint.Style.FILL);

        if (!isInEditMode()) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }

        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);

        return output;
    }

    //endregion
}