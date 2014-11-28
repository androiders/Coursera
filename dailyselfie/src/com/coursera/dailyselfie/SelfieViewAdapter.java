package com.coursera.dailyselfie;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * ListView adapter for the main list view. Holds SelfieInfo objects and uses them to read the image files
 * from disc to display them. Uses the ViewHolder pattern.
 * @author androiders
 *
 */

public class SelfieViewAdapter extends BaseAdapter {

	//private view holder class for efficiency
	static class ViewHolder {
		
		ImageView selfie;
		TextView fileName;
	}
	
	
	//the ist that holds all uri to images
	private ArrayList<SelfieInfo> list = new ArrayList<SelfieInfo>();
	
	//inflater to inflate lsit views
	private static LayoutInflater inflater = null;
	
	//context....
	private Context mContext;

//	private String ASK_DELETE = "Really delete this selfie?";
//	private String NO = "No!";
//	private String OK = "Yes";
//
//	private AlertDialog mAlertDialog;
	
	/**
	 * Creates an adapter from a context. Searches the disc for images with the selfie string in them
	 * If any are found, they are added to the list.
	 * @param context
	 */
	public SelfieViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		
		//See if we have any selfies stored on disk already.
		//If so, read them in
		File dir = SelfieInfo.getMediaFileStorageDir();
		if(dir == null)
			return;
		
		//Read files and filter on selfie string
		String [] pics = dir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				return filename.contains(SelfieInfo.SELFIE_STRING);
			}
		});
		
		for (String string : pics) {
			
			Uri path = Uri.parse(dir.toString() + File.separator + string);
			SelfieInfo selfie = new SelfieInfo(path);
			add(selfie);
		}
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
		final int pos = position;
		
		final SelfieInfo curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.list_item_layout, null);
			holder.selfie = (ImageView) newView.findViewById(R.id.selfie);
			holder.fileName = (TextView) newView.findViewById(R.id.selfie_file_name);
			newView.setTag(holder);
			
		} else {
			holder = (ViewHolder) newView.getTag();
		}

		setPic(curr.getUri(),holder.selfie);
		holder.fileName.setText(curr.getFormatedTimeString());
	
		
		//add a click listener to listen to clicks to show image
		newView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SelfieViewActivity.class);
				intent.putExtra(SelfieViewActivity.EXTRA_URI_STRING,curr.getUri().toString());
				mContext.startActivity(intent);
			}
		});
		
		newView.setOnLongClickListener(new OnLongClickListener() {
			int lPos = pos;
			@Override
			public boolean onLongClick(View v) {
//				mAlertDialog.show();
				//Here we delete the selfie without hesitation adn without asking the user! Great UI indeed :P
				SelfieInfo si = (SelfieInfo) SelfieViewAdapter.this.getItem(lPos);
				if( si.deleteFile()){
					SelfieViewAdapter.this.remove(si);
				}
				else{
					Toast.makeText(mContext, "Could not delete file",Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		
		return newView;
	
	}

	protected void remove(SelfieInfo si) {
		list.remove(si);
		notifyDataSetChanged();
		
	}

	public void add(SelfieInfo selfie) {
		list.add(selfie);
		notifyDataSetChanged();
	}
	
	
	/**
	 * Sets the scaled image on the view. Code taken from android developer pages...
	 * @param photoPath the path of the image to view
	 * @param imageView the view to set scaled image on
	 */
	private void setPic(Uri photoPath, ImageView imageView) {
	    // Get the dimensions of the View
		//max width and height are set in layout file
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
	
//	private void createLongClickDialog() {
//		  // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setMessage(ASK_DELETE)
//               .setPositiveButton(OK, new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int id) {
//                       
//                   }
//               })
//               .setNegativeButton(NO, new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int id) {
//                       // User cancelled the dialog
//                   }
//               });
//        // Create the AlertDialog object and return it
//        mAlertDialog = builder.create();
//    }
}
