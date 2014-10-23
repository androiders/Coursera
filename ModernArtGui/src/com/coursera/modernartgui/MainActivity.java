package com.coursera.modernartgui;

import android.R.drawable;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	//refrences to text views for changing colors
	Button sq1;
	Button sq2;
	Button sq3;
	Button sq4;
	Button sq5;
	
	ColorMatrix mColorMatrix;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sq1 = (Button)findViewById(R.id.sq1);
		sq2 = (Button)findViewById(R.id.sq2);
		sq3 = (Button)findViewById(R.id.sq3);
		sq4 = (Button)findViewById(R.id.sq4);
		sq5 = (Button)findViewById(R.id.sq5);
		
		SeekBar colorSlider = (SeekBar)findViewById(R.id.color_slider);
		colorSlider.setOnSeekBarChangeListener(this);
		
		float colors[] = 
			{	1,0.5f,0.8f,1,1,
				1,0.5f,0.3f,1,1,
				1,0.5f,0.8f,1,1,
				1,00.5f,0.6f,1,1	};
		
		mColorMatrix = new ColorMatrix(colors);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.i("mAIN","color slider changed "+ progress);
		updateColors(progress);
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateColors(int progress) {
		
		int c1 = ((ColorDrawable)sq1.getBackground()).getColor();
		
		//change c1 in red color range
		//mask out  red component
		int red = (c1 >> 16) & 0xFF;
		red -= progress;
		red = red << 16;
		//clear red component of c1
		c1 &= 0xFF00FFFF;
		//st new red component
		c1 |= (red & 0x00FF0000);

		sq1.setBackgroundColor(c1);
	}
}
