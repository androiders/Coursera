package com.mooc.assignment3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EnterUrlDialogFragment extends DialogFragment {

	 public interface UrlDialogListener {
	        public void onDialogPositiveClick(EnterUrlDialogFragment dialog);
	        public void onDialogNegativeClick(EnterUrlDialogFragment dialog);
	    }
	
	 
	 private UrlDialogListener mListener;
	
	 private String mUrlString;
	
	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
		 // Use the Builder class for convenient dialog construction
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		 LayoutInflater inflater = getActivity().getLayoutInflater();
	     final View root = inflater.inflate(R.layout.urldialog,null);
		 ((EditText)root.findViewById(R.id.url_text)).setText(R.string.defaultUrl2);
	     builder.setView(root)
		 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
				 EditText et = (EditText) root.findViewById(R.id.url_text);
				 mUrlString = et.getText().toString();
				 mListener.onDialogPositiveClick(EnterUrlDialogFragment.this);
			 }
		 })
		 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
				 mUrlString = null;
				 mListener.onDialogNegativeClick(EnterUrlDialogFragment.this);
			 }
		 });
		 
		 // Create the AlertDialog object and return it
		 return builder.create();
	 }	

	 // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
	 @Override
	 public void onAttach(Activity activity) {
		 super.onAttach(activity);
		 // Verify that the host activity implements the callback interface
		 try {
			 // Instantiate the NoticeDialogListener so we can send events to the host
			 mListener = (UrlDialogListener) activity;
		 } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
			 throw new ClassCastException(activity.toString()
					 + " must implement NoticeDialogListener");
		 }
	 }
	  
	  
	public String getUrlString() {
		return mUrlString;
	}
	
	
}
