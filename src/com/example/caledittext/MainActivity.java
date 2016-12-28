package com.example.caledittext;




import com.example.caledittext.AutoSizeEditText.OnTextSizeChangeListener;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoSizeEditText mFormulaEditText = (AutoSizeEditText) findViewById(R.id.formula);
        
        mFormulaEditText.setOnTextSizeChangeListener(new OnTextSizeChangeListener() {
			
			@Override
			public void onTextSizeChanged(final TextView textView, float oldSize) {
				// TODO Auto-generated method stub
				final float textScale = oldSize / textView.getTextSize();
		        final float translationX = (1.0f - textScale) *
		                (textView.getWidth() / 2.0f - textView.getPaddingEnd());
		        final float translationY = (1.0f - textScale) *
		                (textView.getHeight() / 2.0f - textView.getPaddingBottom());

		        final AnimatorSet animatorSet = new AnimatorSet();
		        animatorSet.playTogether(
		                ObjectAnimator.ofFloat(textView, View.SCALE_X, textScale, 1.0f),
		                ObjectAnimator.ofFloat(textView, View.SCALE_Y, textScale, 1.0f)
		                //ObjectAnimator.ofFloat(textView, View.TRANSLATION_X, translationX, 0.0f),
		                //ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, translationY, 0.0f)
		                );
		        animatorSet.setDuration(1000);
		        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
		        animatorSet.start();
			}
		});
    }
   
    
	
}
