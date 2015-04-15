package com.mooc.assignment3;

import android.content.Context;
import android.net.Uri;

/**
 * CLass to manage the tasks for filtering and downloading. Keeps an internal state 
 * to know what is going on at the moment.
 *
 */
public class TaskManager implements AbstractTask.TaskCallbacks{

	//callbacks that user can implement to get feedback from this class
	public interface ImageCallbacks {
		public void downloadFinished(Uri imagePath);
		public void downloadCancelled();
		public void filterFinished(Uri imagePath);
		public void filterUpdate(int progress);
		public void filterCancelled();
	}
	
	//internal state values
	private enum State{
		IDLE,
		DOWNLOADING,
		FILTERING
	}
	
	//the internal state
	private State state = State.IDLE;
	
	//the task that is currently executing
	private AbstractTask currentTask;
	
	//the callbacks to use when result is sent from tasks
	private ImageCallbacks mCallbacks;

	private Context mContext;
	
	public TaskManager(Context ctx, ImageCallbacks callbacks) {
		mContext = ctx;
		mCallbacks = callbacks;
	}
	
	/**
	 * cancenls the currently running task
	 * @param mayInterruptIfRunning
	 */
	public void cancelCurrent(boolean mayInterruptIfRunning){
		if(currentTask != null)
			currentTask.cancel(mayInterruptIfRunning);
	}
	
	/**
	 * starts the download task to fetch an image
	 * @param imageUri the uri to try to download from
	 * @return true if download was started. False if another task is already running
	 */
	public boolean startDownloadImage(Uri imageUri){
		if(state != State.IDLE)
			return false;
		
		state = State.DOWNLOADING;
		currentTask = new ImageDownloadTask(mContext, this);
		currentTask.execute(imageUri);
		return true;
	}
	
	/**
	 * starts filtering task
	 * @param imageFilePath the file to filter
	 * @return true if filter task was started. False if other task is already running.
	 */
	public boolean startFilterImage(Uri imageFilePath) {
		if(state != State.IDLE)
			return false;

		state = State.FILTERING;
		currentTask = new FilterTask(mContext, this);
		currentTask.execute(imageFilePath);
		
		return true;
	}

	
	///
	///Callback methiods from the tasks
	///
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
