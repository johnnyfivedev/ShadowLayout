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
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.johnnyfivedev.library.R;

public class ShadowLayout extends FrameLayout {

    @ColorInt
    private int shadowColor;
    private float shadowRadius;
    private float cornerRadius;
    private float shadowOffsetX;
    private float shadowOffsetY;

    private Context context;
    private int currentVisibility = View.VISIBLE;

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

    //region ===================== Shadow radius ======================

    /**
     * Sets shadow radius.
     *
     * @param shadowRadius - radius of shadow in px
     */
    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        redrawUI();
    }

    /**
     * Sets shadow radius.
     *
     * @param shadowRadius - radius of shadow in px
     */
    public void setShadowRadiusDp(float shadowRadius) {
        this.shadowRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, shadowRadius, getResources().getDisplayMetrics());
        redrawUI();
    }

    /**
     * Sets shadow radius.
     *
     * @param shadowRadiusResId - id of radius resource for shadow
     */
    public void setShadowRadiusResource(@DimenRes int shadowRadiusResId) {
        this.shadowRadius = getResources().getDimensionPixelSize(shadowRadiusResId);
        redrawUI();
    }

    //endregion

    //region ===================== Shadow corner radius ======================

    /**
     * Sets shadow corner radius.
     *
     * @param cornerRadius - radius of shadow corners in px
     */
    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        redrawUI();
    }

    /**
     * Sets shadow corner radius.
     *
     * @param cornerRadius - radius of shadow corners in dp
     */
    public void setCornerRadiusDp(float cornerRadius) {
        this.cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadius, getResources().getDisplayMetrics());
        redrawUI();
    }

    /**
     * Sets shadow corner radius.
     *
     * @param cornerRadiusResId - id of corner radius resource for shadow
     */
    public void setCornerRadiusResource(@DimenRes int cornerRadiusResId) {
        this.cornerRadius = getResources().getDimensionPixelSize(cornerRadiusResId);
        redrawUI();
    }

    //endregion

    //region ===================== Shadow offset ======================

    /**
     * Sets shadow offset from view on X axis in px.
     *
     * @param shadowOffsetX -  offset X in px
     */
    public void setShadowOffsetX(float shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
        redrawUI();
    }

    /**
     * Sets shadow offset from view on X axis in dp.
     *
     * @param shadowOffsetX -  offset X in dp
     */
    public void setShadowOffsetXDp(float shadowOffsetX) {
        this.shadowOffsetX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, shadowOffsetX, getResources().getDisplayMetrics());
        redrawUI();
    }

    /**
     * Sets shadow offset from view on X axis in px.
     *
     * @param shadowOffsetXResId - id of corner radius resource for shadow
     */
    public void setShadowOffsetXResource(@DimenRes int shadowOffsetXResId) {
        this.shadowOffsetX = getResources().getDimensionPixelSize(shadowOffsetXResId);
        redrawUI();
    }

    /**
     * Sets shadow offset from view on Y axis in px.
     *
     * @param shadowOffsetY -  offset Y in px
     */
    public void setShadowOffsetY(float shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
        redrawUI();
    }

    /**
     * Sets shadow offset from view on Y axis in dp.
     *
     * @param shadowOffsetY -  offset Y in dp
     */
    public void setShadowOffsetYDp(float shadowOffsetY) {
        this.shadowOffsetY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, shadowOffsetY, getResources().getDisplayMetrics());
        redrawUI();
    }

    /**
     * Sets shadow offset from view on Y axis in px.
     *
     * @param shadowOffsetYResId - id of corner radius resource for shadow
     */
    public void setShadowOffsetYResource(@DimenRes int shadowOffsetYResId) {
        this.shadowOffsetY = getResources().getDimensionPixelSize(shadowOffsetYResId);
        redrawUI();
    }

    //endregion

    //region ===================== Shadow color ======================

    public void setShadowColor(@ColorInt int shadowColor) {
        this.shadowColor = shadowColor;
        redrawUI();
    }

    /**
     * Sets a shadow color.
     * Color should always have an alpha channel, otherwise shadow will be transparent
     */
    public void setShadowColorRes(@ColorRes int shadowColorId) {
        this.shadowColor = ContextCompat.getColor(context, shadowColorId);
        redrawUI();
    }

    //endregion

    /**
     * Sets shadow visibility.
     * Works the same way as {@link View} visibility.
     */
    public void setShadowVisibility(int visibility) {
        if (currentVisibility != visibility) {
            switch (visibility) {
                case VISIBLE:
                    setPaddings();
                    break;
                case INVISIBLE:
                    setBackground(null);
                    if (currentVisibility == GONE) {
                        setPaddings();
                    }
                    break;
                case GONE:
                    setPadding(0, 0, 0, 0);
                    break;
            }
            currentVisibility = visibility;
            redrawUI();
        }
    }

    /**
     * Disables child elevation.
     * If elevation and ShadowLayout are used at the same time actual shadow looks ugly
     * so the solution is to disable elevation
     * This is relevant only for API > 21
     * <p>
     * Make public in future
     * <p>
     * Note: it work to disable first time and then enable first time by again doesn't work
     * Is there a meaning to this in feature?
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void setElevationEnabled(boolean enabled) {
        if (enabled) {
            getChild().setStateListAnimator(childStateListAnimator);
            getChild().setElevation(childElevation);
        } else {
            // todo doesn't fork after onFinishInflate()
            getChild().setStateListAnimator(null);
            redrawUI();
        }
    }

    //endregion

    //region ===================== Callbacks ======================

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        disableElevationIfNeeded();
    }

    // todo is there a reason this logic should be also here? Try to animate width and transition in debug
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*if (w > 0 && h > 0) {
            setBackgroundCompat(w, h);
        }*/
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (currentVisibility != GONE) {
            setBackgroundCompat(right - left, bottom - top);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 0;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 0;
    }

    //endregion

    //region ===================== Internal ======================

    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        initAttributes(attrs);

        setPaddings();
    }

    private void setPaddings() {
        int xPadding = (int) (shadowRadius + Math.abs(shadowOffsetX));
        int yPadding = (int) (shadowRadius + Math.abs(shadowOffsetY));
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray typedArray = getTypedArray(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            try {
                cornerRadius = typedArray.getDimension(R.styleable.ShadowLayout_sl_cornerRadius, getResources().getDimension(R.dimen.default_corner_radius));
                shadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_sl_shadowRadius, getResources().getDimension(R.dimen.default_shadow_radius));
                shadowOffsetX = typedArray.getDimension(R.styleable.ShadowLayout_sl_shadowOffsetX, 0);
                shadowOffsetY = typedArray.getDimension(R.styleable.ShadowLayout_sl_shadowOffsetY, 0);
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
        Bitmap bitmap = createShadowBitmap(w, h);
        setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private Bitmap createShadowBitmap(int shadowWidth,
                                      int shadowHeight) {
        // Be careful with Bitmap.Config enum. It might mess with a shadow color
        Bitmap bitmap = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawRoundRect(
                createRectF(shadowWidth, shadowHeight),
                cornerRadius,
                cornerRadius,
                createPaint());

        return bitmap;
    }

    private RectF createRectF(int shadowWidth, int shadowHeight) {
        RectF rectF = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (shadowOffsetY > 0) {
            rectF.top += shadowOffsetY;
            rectF.bottom -= shadowOffsetY;
        } else if (shadowOffsetY < 0) {
            rectF.top += Math.abs(shadowOffsetY);
            rectF.bottom -= Math.abs(shadowOffsetY);
        }

        if (shadowOffsetX > 0) {
            rectF.left += shadowOffsetX;
            rectF.right -= shadowOffsetX;
        } else if (shadowOffsetX < 0) {
            rectF.left += Math.abs(shadowOffsetX);
            rectF.right -= Math.abs(shadowOffsetX);
        }
        return rectF;
    }

    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setDither(true);
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL);

        if (!isInEditMode()) {
            if (currentVisibility == INVISIBLE) {
                paint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetY, Color.TRANSPARENT);
            } else {
                paint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetY, shadowColor);
            }
        }
        return paint;
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

    // see https://stackoverflow.com/questions/13856180/usage-of-forcelayout-requestlayout-and-invalidate
    private void redrawUI() {
        invalidate();
        requestLayout();
    }

    //endregion
}