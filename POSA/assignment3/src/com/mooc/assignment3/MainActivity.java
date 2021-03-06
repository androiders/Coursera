package com.mooc.assignment3;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.mooc.assignment3.EnterUrlDialogFragment.UrlDialogListener;

public class MainActivity extends LifecycleLoggingActivity implements UrlDialogListener {

	private MainFragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = getFragmentManager();
		mFragment = (MainFragment) fm.findFragmentById(R.id.container);
		
		if(mFragment == null){
			mFragment = new MainFragment();
			fm.beginTransaction().add(R.id.container, mFragment).commit();
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
