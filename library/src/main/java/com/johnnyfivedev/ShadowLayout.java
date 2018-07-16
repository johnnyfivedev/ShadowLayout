package com.johnnyfivedev;

import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.johnnyfivedev.library.R;

/**
 * Note: ShadowLayout should have only one child
 */

public class ShadowLayout extends FrameLayout {

    @ColorInt
    private int shadowColor;
    private float shadowRadius;
    private float cornerRadius;
    private float dx;
    private float dy;

    private Context context;

    private boolean invalidateShadowOnSizeChanged = true;

    private boolean forceInvalidateShadow = false;

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private StateListAnimator childStateListAnimator;

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private float childElevation;


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

    /**
     * Disables child elevation.
     * If elevation and ShadowLayout are used at the same time actual shadow looks ugly
     * so the solution is to disable elevation
     * This is relevant only for API > 21
     * <p>
     * Note: it work to disable first time and then enable first time by again doesn't work
     * Is there a meaning to this feature?
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public void setElevationEnabled(boolean enabled) {
        if (enabled) {
            getChild().setStateListAnimator(childStateListAnimator);
            getChild().setElevation(childElevation);
        } else {
            // todo doesn't fork after onFinishInflate()
            getChild().setStateListAnimator(null);
            //invalidate();
        }
    }

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
    protected void onFinishInflate() {
        super.onFinishInflate();
        disableElevationIfNeeded();
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

    private void initAttributes(AttributeSet attrs) {
        TypedArray typedArray = getTypedArray(attrs, R.styleable.ShadowLayout);
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

    private TypedArray getTypedArray(AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private void disableElevationIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            childStateListAnimator = getChildStateListAnimator();
            childElevation = getChildElevation();
            setElevationEnabled(false);
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private StateListAnimator getChildStateListAnimator() {
        return getChild().getStateListAnimator();
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private float getChildElevation() {
        return getChild().getElevation();
    }

    private void setBackgroundCompat(int w, int h) {
        Bitmap bitmap = createShadowBitmap(w, h, cornerRadius, shadowRadius, dx, dy, shadowColor, Color.TRANSPARENT);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        setBackground(bitmapDrawable);
    }

    private Bitmap createShadowBitmap(int shadowWidth,
                                      int shadowHeight,
                                      float cornerRadius,
                                      float shadowRadius,
                                      float dx,
                                      float dy,
                                      int shadowColor,
                                      int fillColor) {
        // Be careful with Bitmap.Config enum. It might mess with a shadow color
        Bitmap bitmap = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        RectF rectF = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (dy > 0) {
            rectF.top += dy;
            rectF.bottom -= dy;
        } else if (dy < 0) {
            rectF.top += Math.abs(dy);
            rectF.bottom -= Math.abs(dy);
        }

        if (dx > 0) {
            rectF.left += dx;
            rectF.right -= dx;
        } else if (dx < 0) {
            rectF.left += Math.abs(dx);
            rectF.right -= Math.abs(dx);
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setDither(true);
        paint.setColor(fillColor);
        paint.setStyle(Paint.Style.FILL);

        if (!isInEditMode()) {
            paint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }

        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        return bitmap;
    }

    private View getChild() {
        if (getChildCount() == 0) {
            throw new RuntimeException("ShadowLayout should have one child. Did you forget to specify it?");
        } else if (getChildCount() > 1) {
            throw new RuntimeException("ShadowLayout should have only one child. Did you place several views in ShadowLayout?");
        } else {
            return getChildAt(0);
        }
    }

    //endregion
}