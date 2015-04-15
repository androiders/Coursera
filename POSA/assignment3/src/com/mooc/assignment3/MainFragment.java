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

	
	private TextView statusText;
	
	private ProgressBar progress;
	
	private TaskManager mTaskManager;
	
	private ImageView mImage;
	private ImageView mFiltered;
	
	private Button cancel;
	
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
		
		cancel.setEnabled(false);
		progress.setVisibility(View.INVISIBLE);
		statusText.setText("Idle");
		
		return rootView;
	}

	
	public void startDownload(Uri imageUri){
		progress.setIndeterminate(true);
		mImage.setImageBitmap(null);
		mFiltered.setImageBitmap(null);
		mTaskManager.startDownloadImage(imageUri);
		progress.setVisibility(View.VISIBLE);
		statusText.setText(R.string.downloading);
		cancel.setEnabled(true);
	}

	@Override
	public void downloadFinished(Uri imagePath) {
		statusText.setText(R.string.idle);
		cancel.setEnabled(false);
		mImage.setImageBitmap(Utils.decodeSampledBitmap(imagePath,2));
		
		progress.setIndeterminate(false);
		
		//start filtering right away
		mTaskManager.startFilterImage(imagePath);
		statusText.setText(R.string.filtering);
		cancel.setEnabled(true);
	}

	@Override
	public void downloadCancelled() {
		statusText.setText(R.string.idle);
		cancel.setEnabled(false);
		progress.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public void filterFinished(Uri imagePath) {
		statusText.setText(R.string.idle);
		cancel.setEnabled(false);
		progress.setVisibility(View.INVISIBLE);
		mFiltered.setImageBitmap(Utils.decodeSampledBitmap(imagePath,2));
		
	}

	@Override
	public void filterCancelled() {
		statusText.setText(R.string.idle);
		cancel.setEnabled(false);
		progress.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public void filterUpdate(int progress) {
		this.progress.setProgress(progress);
		// TODO Auto-generated method stub
		
	}
	
	
	
}

