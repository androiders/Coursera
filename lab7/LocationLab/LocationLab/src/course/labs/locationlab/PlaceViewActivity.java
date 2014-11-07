package course.labs.locationlab;

import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;
import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;



import android.app.ListActivity;
import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class PlaceViewActivity extends ListActivity implements LocationListener, SensorEventListener {
	private static final long FIVE_MINS = 5 * 60 * 1000;
	private static final String TAG = "Lab-Location";

	// Set to false if you don't have network access
	public static boolean sHasNetwork = false;

	private Location mLastLocationReading;
	private PlaceViewAdapter mAdapter;
	private LocationManager mLocationManager;
	private boolean mMockLocationOn = false;

	// default minimum time between new readings
	private long mMinTime = 5000;

	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f;
	
	//keep track och last shake update
	private long mLastShakeUpdate = 0;

	// A fake location provider used for testing
	private MockLocationProvider mMockLocationProvider;
	
	private SensorManagerSimulator mSensorManager;
	//private Sensor mAccelerometer;
	
	private View mFooterView;
	
	private float[] gravity ={.0f,.0f,.0f};
	private float[] linear_acceleration ={.0f,.0f,.0f};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the app's user interface. This class is a ListActivity, 
		// so it has its own ListView. ListView's adapter should be a PlaceViewAdapter

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ListView placesListView = getListView();

		mSensorManager = SensorManagerSimulator.getSystemService(getApplication(),Context.SENSOR_SERVICE);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	    .permitAll().build();
	StrictMode.setThreadPolicy(policy);
	mSensorManager = SensorManagerSimulator.getSystemService(this,
	    SENSOR_SERVICE);
	mSensorManager.connectSimulator();
         //mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		// TODO - add a footerView to the ListView
		// You can use footer_view.xml to define the footer

		
		mFooterView = View.inflate(getApplicationContext(), R.layout.footer_view, null);
		mFooterView.setClickable(false); //disable until position is acquired


		// TODO - footerView must respond to user clicks, handling 3 cases:

		// There is no current location - response is up to you. One good 
		// solution is to disable the footerView until you have acquired a
		// location.

		// There is a current location, but the user has already acquired a
		// PlaceBadge for this location. In this case issue a Toast message with the text -
		// "You already have this location badge." 
		// Use the PlaceRecord class' intersects() method to determine whether 
		// a PlaceBadge already exists for a given location.

		// There is a current location for which the user does not already have
		// a PlaceBadge. In this case download the information needed to make a new
		// PlaceBadge.

		mFooterView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(TAG, "Entered footerView.OnClickListener.onClick()");


				if(null == mLastLocationReading)
					return;
				
				//if the user has already enterd this location
				if(mAdapter.intersects(mLastLocationReading))
				{
					Toast.makeText(getApplicationContext(), "You already have this location badge", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//new location places
				PlaceDownloaderTask pdt = new PlaceDownloaderTask(PlaceViewActivity.this, sHasNetwork);
				pdt.execute(mLastLocationReading);
			}

		});

		placesListView.addFooterView(mFooterView);
		mAdapter = new PlaceViewAdapter(getApplicationContext());
		setListAdapter(mAdapter);

	}

	@Override
	protected void onResume() {
		super.onResume();

		startMockLocationManager();

		mLastLocationReading = null;
		
		// TODO - Check NETWORK_PROVIDER for an existing location reading.
		// Only keep this last reading if it is fresh - less than 5 minutes old
		boolean av = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		Log.i(TAG, "Network provider available: " + av);
		
		// TODO - register to receive location updates from NETWORK_PROVIDER
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mMinTime, mMinDistance, this);
//		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,this);
				
		Location loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(null != loc && ageInMilliseconds(loc) + FIVE_MINS > System.currentTimeMillis())
			mLastLocationReading = loc;
		
		Log.i(TAG,"Setting footer clickable: " + (null != mLastLocationReading));
		mFooterView.setClickable(null != mLastLocationReading);
		
		mSensorManager.registerListener(this, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), 
				SensorManagerSimulator.SENSOR_DELAY_FASTEST);
//		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
	}

	@Override
	protected void onPause() {
		super.onPause();

		// TODO - unregister for location updates
		mLocationManager.removeUpdates(this);
		shutdownMockLocationManager();
		mSensorManager.unregisterListener(this);
	}

	// Callback method used by PlaceDownloaderTask
	public void addNewPlace(PlaceRecord place) {
		Log.i(TAG, "Entered addNewPlace()");

		// TODO - Attempt to add place to the adapter, considering the following cases

		// The place is null. In this case issue a Toast message with the text
		// "PlaceBadge could not be acquired"
		// Do not add the PlaceBadge to the adapter
		if(null == place){
			Toast.makeText(getApplicationContext(), "PlaceBadge could not be acquired", Toast.LENGTH_SHORT).show();
			return;
		}

		
		// A PlaceBadge for this location already exists. In this case issue a Toast message
		// with the text - "You already have this location badge." Use the PlaceRecord 
		// class' intersects() method to determine whether a PlaceBadge already exists
		// for a given location. Do not add the PlaceBadge to the adapter
		if( mAdapter.intersects(place.getLocation())){
			Toast.makeText(getApplicationContext(), "You already have this location badge", Toast.LENGTH_SHORT).show();
			return;
		}
			
		// The place has no country name. In this case issue a Toast message
		// with the text - "There is no country at this location". 
		// Do not add the PlaceBadge to the adapter
		if("" == place.getCountryName()){
			Toast.makeText(getApplicationContext(), "There is no country at this location", Toast.LENGTH_SHORT).show();
			return;
		}

		
		// Otherwise - add the PlaceBadge to the adapter
		mAdapter.add(place);

	}

	// LocationListener methods
	@Override
	public void onLocationChanged(Location currentLocation) {

		Log.i(TAG,"onLocationChanged called...");	
		
		// TODO - Update last location considering the following cases.
		// 1) If there is no last location, set the last location to the current
		// location.
		// 2) If the current location is older than the last location, ignore
		// the current location
		// 3) If the current location is newer than the last locations, keep the
		// current location.
		
		if(null == mLastLocationReading){
			mLastLocationReading = currentLocation;
			mFooterView.setClickable(true);
			Log.i(TAG,"Setting footer clickable in onLocationChanged");
			return;
		}
		
		if(ageInMilliseconds(currentLocation) < ageInMilliseconds(mLastLocationReading)){
			mLastLocationReading = currentLocation;
		}
			
	}

	@Override
	public void onProviderDisabled(String provider) {
		// not implemented
		Log.i(TAG, "provider disbled!!!");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// not implemented
		Log.i(TAG, "provider enabled!!!");

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// not implemented
	}

	// Returns age of location in milliseconds
	private long ageInMilliseconds(Location location) {
		return System.currentTimeMillis() - location.getTime();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete_badges:
			mAdapter.removeAllViews();
			return true;
		case R.id.place_one:
			mMockLocationProvider.pushLocation(37.422, -122.084);
			return true;
		case R.id.place_no_country:
			mMockLocationProvider.pushLocation(0, 0);
			return true;
		case R.id.place_two:
			mMockLocationProvider.pushLocation(38.996667, -76.9275);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void shutdownMockLocationManager() {
		if (mMockLocationOn) {
			mMockLocationProvider.shutdown();
		}
	}

	private void startMockLocationManager() {
		if (!mMockLocationOn) {
			mMockLocationProvider = new MockLocationProvider(
					LocationManager.NETWORK_PROVIDER, this);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
//		Log.i(TAG,"Sensor changed!! " + event.type);
//		if(event.type != Sensor.TYPE_ACCELEROMETER)
//			return;
		
		
		//check that enough time has passed since last update
		if(System.currentTimeMillis() < mLastShakeUpdate + 5000)
			return;
		
		
		float[] values = event.values;
		double x2 = values[0]*values[0];
		double y2 = values[1]*values[1];
		double z2 = values[2]*values[2];
		
		
		
		double magnitude = Math.sqrt(x2+y2+z2);
		//only do something if magnitude is large enough
		
		
		if(magnitude > 10.0 && mLastLocationReading != null){
			//new location places
			Log.i(TAG,"Shake magnitude: " + magnitude);
			PlaceDownloaderTask pdt = new PlaceDownloaderTask(PlaceViewActivity.this, sHasNetwork);
			pdt.execute(mLastLocationReading);
		
			mLastShakeUpdate = System.currentTimeMillis();
		}
			
		
//		final float alpha = 0.8f;
//
//        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
//        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
//        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
//		
//        linear_acceleration[0] = event.values[0] - gravity[0];
//        linear_acceleration[1] = event.values[1] - gravity[1];
//        linear_acceleration[2] = event.values[2] - gravity[2];
//        
//        
//		Log.i(TAG,"Sensor changed: " + linear_acceleration[0] + " " + linear_acceleration[1] + " " + linear_acceleration[2]);
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
//		Log.i(TAG,"Sensor changed!!");
		
	}
}
