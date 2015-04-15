package com.mooc.assignment3;

import android.content.Context;
import android.net.Uri;

public class TaskManager implements AbstractTask.TaskCallbacks{

	public interface ImageCallbacks {
		public void downloadFinished(Uri imagePath);
		public void downloadCancelled();
		public void filterFinished(Uri imagePath);
		public void filterUpdate(int progress);
		public void filterCancelled();
	}
	
	private enum State{
		IDLE,
		DOWNLOADING,
		FILTERING
	}
	
	private State state = State.IDLE;
	
	private AbstractTask currentTask;
	
	private ImageCallbacks mCallbacks;

	private Context mContext;
	
	public TaskManager(Context ctx, ImageCallbacks callbacks) {
		mContext = ctx;
		mCallbacks = callbacks;
	}
	
	
	public void cancelCurrent(boolean mayInterruptIfRunning){
		if(currentTask != null)
			currentTask.cancel(mayInterruptIfRunning);
	}
	
	public void startDownloadImage(Uri imageUri){
		state = State.DOWNLOADING;
		currentTask = new ImageDownloadTask(mContext, this);
		currentTask.execute(imageUri);
	}
	
	public void startFilterImage(Uri imageFilePath) {
		state = State.FILTERING;
		currentTask = new FilterTask(mContext, this);
		currentTask.execute(imageFilePath);
	}


	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProgressUpdate(int percent) {
		mCallbacks.filterUpdate(percent);
		
	}


	@Override
	public void onCancelled() {
		switch(state){
		case DOWNLOADING:
			mCallbacks.downloadCancelled();
			state = State.IDLE;
			break;
		case FILTERING:
			mCallbacks.filterCancelled();
			state = State.IDLE;
			break;
		case IDLE:
		default:
			break;
		}		
	}


	@Override
	public void onPostExecute(Uri uri) {
		
		switch(state){
		case DOWNLOADING:
			state = State.IDLE;
			mCallbacks.downloadFinished(uri);
			break;
		case FILTERING:
			state = State.IDLE;
			mCallbacks.filterFinished(uri);
			break;
		case IDLE:
		default:
			break;
		}
	}
}
