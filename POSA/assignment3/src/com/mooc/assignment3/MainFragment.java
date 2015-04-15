package com.mooc.assignment3;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainFragment extends Fragment implements TaskManager.ImageCallbacks {

	
	//handle to the tasks
	private TaskManager mTaskManager;
	
	//view components
	private TextView statusText;
	private ProgressBar progress;
	private ImageView mImage;
	private ImageView mFiltered;
	private Button cancel;

	//file paths to store state
	private String imageFile;
	private String filteredFile;

	//State key values 
	private String CANCEL_ENABLED 		 = "cancelenabled";
	private String PROGRESS_VISIBLE 	 = "progressvisible";
	private String PROGRESS_VALUE 		 = "progressvalue";
	private String PROGRESS_INDETERMINTE = "progressindeterminate";
	private String IMAGE_FILE 			 = "imagefile";
	private String FILTERED_FILE 		 = "filteredfile";
	private String STATUS_TEXT 			 = "statustext";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retain this fragment
		setRetainInstance(true);
		mTaskManager = new TaskManager(getActivity(), this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		progress = (ProgressBar) rootView.findViewById(R.id.progress);
		statusText = (TextView) rootView.findViewById(R.id.statusText);
		mImage = (ImageView) rootView.findViewById(R.id.image);
		mFiltered = (ImageView) rootView.findViewById(R.id.filteredImage);
		cancel = (Button) rootView.findViewById(R.id.cancelButton);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progress.setIndeterminate(true);
				statusText.setText(R.string.cancelling);
				mTaskManager.cancelCurrent(true);
				
			}
		});

		//fragment restored from state
		if(savedInstanceState != null)
			restoreState(savedInstanceState);
		else{//a new fragment is created
			updateUiState(R.string.idle, false, View.INVISIBLE, true);
		}
		
		return rootView;
	}

	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		storeState(outState);
	}

	/**
	 * Store the state so we can restore it on configuration changes
	 * @param outState the state to store
	 */
	private void storeState(Bundle outState) {
		outState.putBoolean(CANCEL_ENABLED, cancel.isEnabled());
		
		outState.putInt(PROGRESS_VISIBLE, progress.getVisibility());
		outState.putInt(PROGRESS_VALUE,progress.getProgress());
		outState.putBoolean(PROGRESS_INDETERMINTE, progress.isIndeterminate());
		
		outState.putString(STATUS_TEXT , statusText.getText().toString());
		
		outState.putString(IMAGE_FILE, imageFile);
		outState.putString(FILTERED_FILE, filteredFile);
		
	}

	/**
	 * Restore the state from bundle
	 * @param savedState the saved state
	 */
	private void restoreState(Bundle savedState){
		
		cancel.setEnabled(savedState.getBoolean(CANCEL_ENABLED));
		progress.setVisibility(savedState.getInt(PROGRESS_VISIBLE));
		progress.setProgress(savedState.getInt(PROGRESS_VALUE));
		progress.setIndeterminate(savedState.getBoolean(PROGRESS_INDETERMINTE));
		statusText.setText(savedState.getString(STATUS_TEXT));
		imageFile = savedState.getString(IMAGE_FILE);
		filteredFile = savedState.getString(FILTERED_FILE);
		
		if(imageFile != null)
		{
			Uri name = Uri.parse(imageFile);
			mImage.setImageBitmap(Utils.decodeSampledBitmap(name,2));
		}
		if(filteredFile != null)
		{
			Uri name = Uri.parse(filteredFile);
			mFiltered.setImageBitmap(Utils.decodeSampledBitmap(name,2));
		}
		
	}

	/**
	 * Starts a download of image and sets all ui controls to correct state
	 * @param imageUri the uri to try to download from
	 */
	public void startDownload(Uri imageUri){
		
		
		if(!mTaskManager.startDownloadImage(imageUri)){
			Utils.showToast(getActivity(), "Wont start download while other operation is in progress");
			return;
		}
	
		updateUiState(R.string.downloading, true, View.VISIBLE, true);
		
		mImage.setImageBitmap(null);
		mFiltered.setImageBitmap(null);
		imageFile = null;
		filteredFile = null;
		
	}

	/**
	 * callback method to know when download is finished. Starts filtering if
	 * downlaod was successfull.
	 */
	@Override
	public void downloadFinished(Uri imagePath) {
		
		updateUiState(R.string.idle, false, View.VISIBLE, false);
		progress.setProgress(0);

		//if something went wrong, show toast and return
		if(imagePath == null){
			progress.setVisibility(View.INVISIBLE);
			Utils.showToast(getActivity(), "No Image at given URL");
			return;
		}
				
		mImage.setImageBitmap(Utils.decodeSampledBitmap(imagePath,2));
		imageFile = imagePath.toString();
		
		//start filtering right away
		mTaskManager.startFilterImage(imagePath);
		statusText.setText(R.string.filtering);
		cancel.setEnabled(true);
	}

	/**
	 * If user cancells the download we will know it here. 
	 */
	@Override
	public void downloadCancelled() {
		
		updateUiState(R.string.idle, false, View.INVISIBLE, false);
		imageFile = null;
	}

	/**
	 * This method is called when filtering is finished and the path
	 * to the filtered image is the parameter
	 */
	@Override
	public void filterFinished(Uri imagePath) {
		updateUiState(R.string.idle, false, View.INVISIBLE, false);

		//if something went wrong we show a toast and return
		if(imagePath == null){
			Utils.showToast(getActivity(), "Unable to decode image!");
			return;
		}
		
		//show the filtered image
		filteredFile = imagePath.toString();
		mFiltered.setImageBitmap(Utils.decodeSampledBitmap(imagePath,2));
	}

	/**
	 * If the user cancels the filtering we know it here
	 */
	@Override
	public void filterCancelled() {
		updateUiState(R.string.idle, false, View.INVISIBLE, false);
		filteredFile = null;
	}

	/**
	 * Updates the progress for filtering
	 */
	@Override
	public void filterUpdate(int progress) {
		this.progress.setProgress(progress);
	}
	
	
	/**
	 * Helper method to set the state of various UI components
	 * @param statusId id of string to show in status text (i.e. R.string.idle)
	 * @param cancelEnable if true cancel button is enabled.
	 * @param progessVisible sets visibility of progress bar
	 * @param progressIndeterminate sets if progress bar is indeterminate
	 */
	private void updateUiState(int statusId, boolean cancelEnable, int progessVisible, boolean progressIndeterminate){
		statusText.setText(statusId);
		cancel.setEnabled(cancelEnable);
		progress.setVisibility(progessVisible);
		progress.setIndeterminate(progressIndeterminate);
	}
	
}

