package com.coursera.modernartgui;

import com.coursera.modernartgui.ArtDialogFragment.ArtDialogListener;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnSeekBarChangeListener, ArtDialogListener {

	private String MOMA_URL = "http://www.moma.com";
	
	//refrences to text views for changing colors
	Button sq1;
	Button sq2;
	Button sq3;
	Button sq4;
	Button sq5;
	
	private String COLOR_1_STR = "c1";
	private String COLOR_2_STR = "c2";
	private String COLOR_3_STR = "c3";
	private String COLOR_4_STR = "c4";
	private String PROGR_STR   = "prgress";
	
	int mLastProgress;
	
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

		mLastProgress = 0;
		
		//restore colors and slider progress from savedinstance!!
		if(savedInstanceState != null)
		{
			int c1 = savedInstanceState.getInt(COLOR_1_STR);
			int c2 = savedInstanceState.getInt(COLOR_2_STR);
			int c3 = savedInstanceState.getInt(COLOR_3_STR);
			int c4 = savedInstanceState.getInt(COLOR_4_STR);

			mLastProgress = savedInstanceState.getInt(PROGR_STR);
			
			sq1.setBackgroundColor(c1);
			sq2.setBackgroundColor(c2);
			sq3.setBackgroundColor(c3);
			sq4.setBackgroundColor(c4);
		}
		
	}
	
	@Override
	protected void onSaveInstanceState (Bundle outState)
	{
		//NOTE the illogical order on colors
		int c1 = getColor1();
		int c2 = getColor2();
		int c3 = getColor3();
		int c5 = getColor5();
		
		outState.putInt(COLOR_1_STR,c1);
		outState.putInt(COLOR_2_STR,c2);
		outState.putInt(COLOR_3_STR,c3);
		outState.putInt(COLOR_4_STR,c5);
		outState.putInt(PROGR_STR,mLastProgress);
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

	//Create the options menu here!
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	private void showDialog()
	{
		 DialogFragment newFragment = new ArtDialogFragment();
		 newFragment.show(getFragmentManager(), "");
	}
	
	
	//check for clicks in the menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.more_info:
	        	Log.i("Main","Menu item clicked");
	        	showDialog();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.i("Main","color slider changed "+ progress);
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
	
	
	private int getColor1(){
		return ((ColorDrawable)sq1.getBackground()).getColor();
	}
	
	private int getColor2(){
		return ((ColorDrawable)sq2.getBackground()).getColor();
	}
	
	private int getColor3(){
		return ((ColorDrawable)sq3.getBackground()).getColor();
	}

	private int getColor5(){
		return ((ColorDrawable)sq5.getBackground()).getColor();
	}
	
	
	private void updateColors(int progress) {
		
		int lProg = (progress - mLastProgress)*2;
		mLastProgress = progress;
		
		//get the colors from the ui buttons
		int c1 = getColor1();
		int c2 = getColor2();
		int c3 = getColor3();
		int c5 = getColor5();
		
		//this is where the magic happens!
		//bit operations to change colors...
		
		//change c1 in red color range
		//mask out  red component
		int red = (c3 >> 16) & 0xFF;
		red += lProg;
		red = red << 16;
		//clear red component of c1
		c3 &= 0xFF00FFFF;
		//set new red component
		c3 |= (red & 0x00FF0000);
		sq3.setBackgroundColor(c3);
		
		//change c2 in blue color range
		//mask out  red component
		int blue =  c2 & 0xFF;
		blue += lProg;
		//clear red component of c2
		c2 &= 0xFFFFFF00;
		//set new red component
		c2 |= (blue & 0x000000FF);
		sq2.setBackgroundColor(c2);

		//change c3 in green color range
		//mask out  green component
		int green = (c1 >> 8) & 0xFF;
		green += lProg ;
		green = green << 8;
		//clear red component of c3
		c1 &= 0xFFFF00FF;
		//set new red component
		c1 |= (green & 0x0000FF00);
		sq1.setBackgroundColor(c1);

		//change c4 in some other color range
		//mask out some component
		int some = (c5 >> 16) & 0xFF;
		some += lProg;
		some = some << 16;
		//clear some component of c4
		c5 &= 0xFF00FFFF;
		//set new  component
		c5 |= (some & 0x00FF0000);
		sq5.setBackgroundColor(c5);
				
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		Log.i("main","Dialog positively clicked");
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(MOMA_URL));
		startActivity(i);
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		Log.i("main","Dialog negatively clicked");

	}
}
