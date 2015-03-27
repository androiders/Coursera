package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param Bundle object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
    	super.onCreate(savedInstanceState);

        // Get the URL associated with the Intent data.
    	final Uri uri = getIntent().getData();
    	

        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

    	Thread imageThread = new Thread(){
    		public void run(){
    			//download the image in this thread
    			final Uri img = DownloadUtils.downloadImage(DownloadImageActivity.this, uri);

    			//create a runnable that we can post to the ui thread using runOnUiThread below
    			Runnable uiRunnable = new Runnable(){
    				public void run(){
    					//create the intent result
    	    	    	Intent result = new Intent();
    	    	    	
    	    	    	//if the dl failed return something that is not OK
    	    	    	if(img == null || img.toString().isEmpty())
    	    	    		setResult(RESULT_CANCELED);
    	    	    	else{
    	    	    	//if dl was OK, set the data and return OK
    	    	    	result.setData(img);
    	    	    	setResult(RESULT_OK, result);
    	    	    	}
    	    	    	//signal finish for this intent
    	    	    	finish();
    				}
    			};
    			//when the thread finishes the download, the runnable will be posted to the ui thread and execute the finish()
    			runOnUiThread(uiRunnable);

    		}
    	};
    	//start the thread and go :)
    	imageThread.start();
    }
}
