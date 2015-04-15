package com.mooc.assignment3;

import android.content.Context;
import android.net.Uri;

public class ImageDownloadTask extends AbstractTask {

	
	public ImageDownloadTask(Context ctx, TaskCallbacks callbacks) {
		super(ctx,callbacks);
	}

	@Override
	protected Uri doInBackground(Uri... params) {
		Uri u = Utils.downloadImage(getContext(), params[0]);
		
		return u;
	}

}
