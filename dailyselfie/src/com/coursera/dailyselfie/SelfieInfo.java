package com.coursera.dailyselfie;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * Class for holding information about a selfie
 * @author androiders
 *
 */

public class SelfieInfo {

	public static String SELFIE_STRING = "_selfie_";
	private static String DATE_FORMAT_STRING = "yyyyMMdd_HHmmss";
	
	Uri mPath;
	String mTimeStamp;
	
	public SelfieInfo(){
		mPath = getOutputMediaFileUri();
		mTimeStamp = getTimeStampFromUri(mPath);
	}
	
	/**
	 * Creates a selfiinfo object from a file path. useful for when reading from disc
	 * @param path the path of the selfie image
	 */
	public SelfieInfo(Uri path) {
		mPath = path;
		mTimeStamp = getTimeStampFromUri(path);
	}
	
	public Uri getUri() {
		return mPath;
	}
	
	/**
	 * brute force date string formatter. Should of cpurse use SimpleDateFormat or something
	 * similat to get locale sensitive format...but... time is short!!! :)
	 * @return a formated string showing date and time.
	 */
	public String getFormatedTimeString() {
		String year = mTimeStamp.substring(0,4);
		String month = mTimeStamp.substring(4, 6);
		String day = mTimeStamp.substring(6, 8);

		String time = mTimeStamp.substring(9,15);
		String hour = time.substring(0, 2);
		String min = time.substring(2, 4);
		String date = year + "-" + month + "-" + day;
		time = hour + ":" + min;
		return date + " " + time;
	}
	
	/**
	 * Parse a Uri and find the timestamp in it. Assumes the Uri is created from this class
	 * @param path the Uri to parse
	 * @return returns the timestamp as a string
	 */
	private static String getTimeStampFromUri(Uri path) {
		String lPath = path.toString();
		int lastSlash = lPath.lastIndexOf(File.separator);
		int startOfTime = lastSlash + SELFIE_STRING.length() + 1;
		String timeStamp = lPath.substring(startOfTime, startOfTime+DATE_FORMAT_STRING.length());
		return timeStamp;
	}
	
	/**
	 * returns the directory of the storage of media
	 * @return a File object representing the media storage directory. null if nons is found or external storage not mounted
	 */
	public static File getMediaFileStorageDir() {
		// To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		
		if(	!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
			return null;

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }
	    
	    return mediaStorageDir;
	}
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(){
		
		File mediaStorageDir = getMediaFileStorageDir();
	    
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat(DATE_FORMAT_STRING).format(new Date());
	    File mediaFile;
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	    		SELFIE_STRING  + timeStamp + ".jpg");
	    
	    return mediaFile;
	}

	public boolean deleteFile() {
		File f = new File(mPath.getPath());
		return f.delete();
	}
	
}
