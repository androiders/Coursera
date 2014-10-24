/**
 * 
 */
package com.coursera.modernartgui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * @author anfol1
 *
 */
public class ArtDialogFragment extends DialogFragment {

	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     */
	public interface ArtDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	ArtDialogListener mListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			mListener = (ArtDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}
	
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.visit_moma_info)
               .setPositiveButton(R.string.visit_moma, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       Log.i("Dialog","positive click");
                       //call the corresponding callback in the listener
                       mListener.onDialogPositiveClick(ArtDialogFragment.this);
                   }
               })
               .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Log.i("Dialog","negative click");
                	 //call the corresponding callback in the listener
                	   mListener.onDialogNegativeClick(ArtDialogFragment.this);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	
}
