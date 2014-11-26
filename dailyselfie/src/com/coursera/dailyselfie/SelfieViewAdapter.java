package com.coursera.dailyselfie;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieViewAdapter extends BaseAdapter {

	//private view holder class for efficiency
	static class ViewHolder {
		
		ImageView selfie;
		TextView fileName;
	}
	
	//the ist that holds all uri to images
	private ArrayList<Uri> list = new ArrayList<Uri>();
	
	//inflater to inflate lsit views
	private static LayoutInflater inflater = null;
	
	//context....
	private Context mContext;
	
	public SelfieViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		final Uri curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.list_item_layout, null);
			holder.selfie = (ImageView) newView.findViewById(R.id.selfie);
			holder.fileName = (TextView) newView.findViewById(R.id.selfie_file_name);
			newView.setTag(holder);
			
		} else {
			holder = (ViewHolder) newView.getTag();
		}

		setPic(curr,holder.selfie);
		holder.fileName.setText(curr.getPath());
	
		newView.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, SelfieViewActivity.class);
				intent.putExtra(SelfieViewActivity.EXTRA_URI_STRING,curr.toString());
				mContext.startActivity(intent);
			}
		});
		
		return newView;
	
	}

	public void add(Uri selfiUri) {
		list.add(selfiUri);
		notifyDataSetChanged();
	}

	private void setPic(Uri photoPath, ImageView imageView) {
	    // Get the dimensions of the View
		//max width and height are set in layot file
	    int targetW = imageView.getMaxWidth();
	    int targetH = imageView.getMaxHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(photoPath.getPath(), bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(photoPath.getPath(), bmOptions);
	    imageView.setImageBitmap(bitmap);
	}
	
}
