package com.mooc.assignment3;

import android.content.Context;
import android.net.Uri;

public class FilterTask extends AbstractTask {

	public FilterTask(Context ctx,TaskCallbacks callbacks) {
		super(ctx,callbacks);
	}

	@Override
	protected Uri doInBackground(Uri... params) {
		Uri u = Utils.grayScaleFilter(getContext(), params[0], new ProgressObserver() {
			
			@Override
			public void update(int max, int progress) {
				double prg = ((double)progress / (double)max) * 100.0;
				publishProgress((int)prg);
				
			}
		});
		
		return u;
	}
	
}
