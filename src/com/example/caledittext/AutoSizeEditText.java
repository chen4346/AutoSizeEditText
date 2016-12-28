package com.example.caledittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import android.widget.TextView;

public class AutoSizeEditText extends EditText {
	private float mMaximumTextSize;
	private float mMinimumTextSize;
	private float mStepTextSize;
	private int mWidthConstraint = -1;
	private final Paint mTempPaint = new TextPaint();
	private final Rect mTempRect = new Rect();

	public AutoSizeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.AutoSizeEditText, defStyleAttr, 0);
		mMaximumTextSize = a.getDimension(R.styleable.AutoSizeEditText_maxTextSize,
				getTextSize());
		mMinimumTextSize = a.getDimension(R.styleable.AutoSizeEditText_minTextSize,
				getTextSize());
		mStepTextSize = a.getDimension(R.styleable.AutoSizeEditText_stepTextSize,
				(mMaximumTextSize - mMinimumTextSize) / 3);
		a.recycle();
		System.out.println("mMaximumTextSize:"+mMaximumTextSize);
		System.out.println("mMinimumTextSize:"+mMinimumTextSize);
		System.out.println("mStepTextSize:"+mStepTextSize);
		setCustomSelectionActionModeCallback(NO_SELECTION_ACTION_MODE_CALLBACK);
		if (isFocusable()) {
			// 禁止选中文字
			setMovementMethod(ScrollingMovementMethod.getInstance());
		}
		setTextSize(TypedValue.COMPLEX_UNIT_PX, mMaximumTextSize);
		setMinHeight(getLineHeight() + getCompoundPaddingBottom()
				+ getCompoundPaddingTop());
	}

	public AutoSizeEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public AutoSizeEditText(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getActionMasked() == MotionEvent.ACTION_UP) {
			// 防止键盘出现
			//cancelLongPress();
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		System.out.println("onMeasure++++++++++++++++");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 获取可显示的宽度
		mWidthConstraint = MeasureSpec.getSize(widthMeasureSpec)
				- getPaddingLeft() - getPaddingRight();
		
		
		setTextSize(TypedValue.COMPLEX_UNIT_PX, getVariableTextSize(getText()
				.toString()));
	}

	public float getVariableTextSize(String text) {
		if (mWidthConstraint < 0 || mMaximumTextSize <= mMinimumTextSize) {
            // Not measured, bail early.
            return getTextSize();
        }

        // Capture current paint state.
        mTempPaint.set(getPaint());

        // Step through increasing text sizes until the text would no longer fit.
        float lastFitTextSize = mMinimumTextSize; //先假定一的值，取最小值
        while (lastFitTextSize < mMaximumTextSize) {
            final float nextSize = Math.min(lastFitTextSize + mStepTextSize, mMaximumTextSize);
            //尝试一个比假定值大一个步长的值
            mTempPaint.setTextSize(nextSize);
            if (mTempPaint.measureText(text) > mWidthConstraint) {
            	//判断nextSize是否超出范围，如果超出范围，使用加步长之前的值
                break;
            } else {
            	//否则继续加步长继续判断
                lastFitTextSize = nextSize;
            }
        }
        return lastFitTextSize;
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		// TODO Auto-generated method stub
		
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		final int textLength = text.length();
		if (getSelectionStart() != textLength
				|| getSelectionEnd() != textLength) {
			// Pin the selection to the end of the current text.
			setSelection(textLength);
		}
		setTextSize(TypedValue.COMPLEX_UNIT_PX,
				getVariableTextSize(text.toString()));
	}

	@Override
    public void setTextSize(int unit, float size) {
        final float oldTextSize = getTextSize();
        super.setTextSize(unit, size);

        if (mOnTextSizeChangeListener != null && getTextSize() != oldTextSize) {
            mOnTextSizeChangeListener.onTextSizeChanged(this, oldTextSize);
        }
    }

	private OnTextSizeChangeListener mOnTextSizeChangeListener;

	public void setOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
		mOnTextSizeChangeListener = listener;
	}

	public interface OnTextSizeChangeListener {
		void onTextSizeChanged(TextView textView, float oldSize);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		super.onSaveInstanceState();
		// 不进行状态保存
		return null;
	}
	/*
	@Override
	public int getCompoundPaddingTop() {
		// Measure the top padding from the capital letter height of the text
		// instead of the top,
		// but don't remove more than the available top padding otherwise
		// clipping may occur.
		
		getPaint().getTextBounds("H", 0, 1, mTempRect);

		final FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
		final int paddingOffset = -(fontMetrics.ascent + mTempRect.height());
		return super.getCompoundPaddingTop()
				- Math.min(getPaddingTop(), paddingOffset);
	}
	
	@Override
	public int getCompoundPaddingBottom() {
		// TODO Auto-generated method stub
		final FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
		return super.getCompoundPaddingBottom()
				- Math.min(getPaddingBottom(), fontMetrics.descent);
	}*/

	static ActionMode.Callback NO_SELECTION_ACTION_MODE_CALLBACK = new ActionMode.Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}
	};

}
