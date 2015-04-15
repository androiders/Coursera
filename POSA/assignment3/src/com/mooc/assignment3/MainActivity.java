package com.mooc.assignment3;

import com.mooc.assignment3.EnterUrlDialogFragment.UrlDialogListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends LifecycleLoggingActivity implements UrlDialogListener {

	private MainFragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			mFragment = new MainFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, mFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_download) {
			EnterUrlDialogFragment d = new EnterUrlDialogFragment();
			d.show(getFragmentManager(), "urlFragment");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(EnterUrlDialogFragment dialog) {
		String str = dialog.getUrlString();
		
		if(str == null || !URLUtil.isValidUrl(str)){
			Toast.makeText(this,
					"Invalid URL",
					Toast.LENGTH_SHORT).show();
		}
		else{
			 Uri url = Uri.parse(str);
			 mFragment.startDownload(url);
		}
	}

	@Override
	public void onDialogNegativeClick(EnterUrlDialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container,
//					false);
//			return rootView;
//		}
//	}
}
