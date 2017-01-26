package ezy.ui.veiw;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import ezy.library.subtextview.R;


public class SubTextView extends TextView {

    private Layout mSubLayout;
    private final TextPaint mSubPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private CharSequence mSubText;
    private boolean mIsSubtitle = false;
    private int mSubGap;
    private int mSubOffset = 0;

    public SubTextView(Context context) {
        this(context, null, 0);
    }

    public SubTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SubTextView);

        int subColor = a.getColor(R.styleable.SubTextView_stvSubTextColor, 0xff666666);
        float subSize = a.getDimension(R.styleable.SubTextView_stvSubTextSize, getTextSize() * 0.8f);

        mSubText = a.getString(R.styleable.SubTextView_stvSubText);
        mSubGap = a.getDimensionPixelSize(R.styleable.SubTextView_stvSubTextGap, 0);
        mIsSubtitle = a.getInteger(R.styleable.SubTextView_stvSubTextMode, 0) == 0;

        a.recycle();

        mSubPaint.setTextAlign(Paint.Align.LEFT);
        mSubPaint.setColor(subColor);
        mSubPaint.setTextSize(subSize);

        super.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        super.setSingleLine();
    }


    @Override
    public void setGravity(int gravity) {
    }


    @Override
    public int getCompoundPaddingRight() {
        if (!mIsSubtitle && mSubLayout != null) {
            return super.getCompoundPaddingRight() + (int) mSubLayout.getLineWidth(0) + mSubGap;
        }
        return super.getCompoundPaddingRight();
    }

    private Layout obtainLayout(CharSequence source, TextPaint paint, int widthMeasureSpec, boolean isSubtitle) {
        int width = MeasureSpec.getSize(widthMeasureSpec) - super.getCompoundPaddingLeft() - super.getCompoundPaddingRight();
        BoringLayout.Metrics metrics = BoringLayout.isBoring(source, paint);
        Layout.Alignment alignment = isSubtitle ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
        if (metrics != null) {
            return BoringLayout.make(source, paint, width, alignment, 1.0f, 0, metrics, false);
        } else {
            return new StaticLayout(source, paint, width, alignment, 1.0f, 0, false);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (TextUtils.isEmpty(mSubText)) {
            mSubLayout = null;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mSubLayout = obtainLayout(mSubText, mSubPaint, widthMeasureSpec, mIsSubtitle);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mIsSubtitle) {
            offsetTo((mSubLayout.getHeight() + mSubGap) >> 1);

            int hm = MeasureSpec.getMode(heightMeasureSpec);
            if (hm != MeasureSpec.EXACTLY) {
                int height = getCompoundPaddingTop() + getCompoundPaddingBottom() + getLayout().getHeight() + mSubLayout.getHeight();
                setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Math.max(getMeasuredHeight(), height), hm));
            }
        }
    }

    void offsetTo(int y) {
        mSubOffset = y;
        Drawable dl = getCompoundDrawables()[0];
        Drawable dr = getCompoundDrawables()[2];
        if (dl != null) {
            dl.setBounds(0, y, dl.getIntrinsicWidth(), y + dl.getIntrinsicHeight());
        }
        if (dr != null) {
            dr.setBounds(0, y, dr.getIntrinsicWidth(), y + dr.getIntrinsicHeight());
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        if (mSubLayout != null && mIsSubtitle) {
            canvas.translate(0, -mSubOffset);
        }
        super.onDraw(canvas);

        if (mSubLayout != null) {
            if (mIsSubtitle) {
                canvas.translate(getCompoundPaddingLeft(), getTotalPaddingTop() + getLayout().getHeight() + mSubGap);
            } else {
                canvas.translate(getCompoundPaddingLeft(), (getMeasuredHeight() + getCompoundPaddingTop() - getCompoundPaddingBottom() - mSubLayout
                        .getHeight()) / 2);
            }
            mSubLayout.draw(canvas);
        }
        canvas.restore();
    }

    public SubTextView setSubTextSize(float size) {
        mSubPaint.setTextSize(size);
        requestLayout();
        return this;
    }
    public SubTextView setSubTextGap(int gap) {
        mSubGap = gap;
        requestLayout();
        return this;
    }

    public SubTextView setSubTextColor(int color) {
        mSubPaint.setColor(color);
        invalidate();
        return this;
    }

    public SubTextView setSubtitle(boolean isSubtitle) {
        mIsSubtitle = isSubtitle;
        mSubLayout = null;
        if (mIsSubtitle) {
            requestLayout();
        } else {
            Drawable[] arr = getCompoundDrawables();
            setCompoundDrawablesWithIntrinsicBounds(arr[0], arr[1], arr[2], arr[3]);
        }
        return this;
    }

    public SubTextView setSubText(CharSequence value) {
        mSubText = value;
        requestLayout();
        return this;
    }

}