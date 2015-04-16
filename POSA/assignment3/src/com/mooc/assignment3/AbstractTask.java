package com.mooc.assignment3;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

public abstract class AbstractTask extends AsyncTask<Uri,Integer,Uri> {

	
	public interface TaskCallbacks {
		void onPreExecute();
	    void onProgressUpdate(int percent);
	    void onCancelled();
	    void onPostExecute(Uri uri);
	}
	
	private Context mContext;
	private TaskCallbacks mCallbacks;
	
	public AbstractTask(Context ctx, TaskCallbacks callbacks) {
		super();
		mContext = ctx;
		mCallbacks = callbacks;
	}

	protected Context getContext(){
		return mContext;
	}

	@Override
	protected void onPreExecute(){
		if(mCallbacks != null)
			mCallbacks.onPreExecute();
	}
	
	@Override
	protected void onProgressUpdate(Integer... percent){
		if(mCallbacks != null)
			mCallbacks.onProgressUpdate(percent[0]);
		
	}
	
	@Override
	protected void onPostExecute(Uri uri){
		if(mCallbacks != null)
			mCallbacks.onPostExecute(uri);
	}
	
	@Override
	protected void onCancelled(){
		if(mCallbacks != null)
			mCallbacks.onCancelled();

	}
}
