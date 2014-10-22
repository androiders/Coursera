package course.labs.todomanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToDoListAdapter extends BaseAdapter {

	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	private final Context mContext;

	private static final String TAG = "Lab-UserInterface";
	
	static class ViewHolder
	{
		public	TextView titleView;
//		public	TextView statusLabel;
//		public	TextView priorityLabel;
		public	TextView priorityView;
//		public	TextView dateLabel;
		public	TextView dateView;
		public	CheckBox statusCheckbox;
	}

	public ToDoListAdapter(Context context) {

		mContext = context;
		
		

	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}

	// Clears the list adapter of all items.

	public void clear() {

		mItems.clear();
		notifyDataSetChanged();

	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	// Create a View for the ToDoItem at specified position
	// Remember to check whether convertView holds an already allocated View
	// before created a new View.
	// Consider using the ViewHolder pattern to make scrolling more efficient
	// See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		// TODO - Get the current ToDoItem
		final ToDoItem toDoItem = (ToDoItem) getItem(position);

		
		
		RelativeLayout itemLayout;
		if(convertView == null)
		{
			 LayoutInflater inflater = LayoutInflater.from(mContext);
			 itemLayout = (RelativeLayout)inflater.inflate(R.layout.todo_item, parent, false);
		
			 viewHolder = new ViewHolder();
			 viewHolder.dateView = (TextView)itemLayout.findViewById(R.id.dateView);
//			 viewHolder.dateLabel = (TextView)itemLayout.findViewById(R.id.DateLabel);
			 viewHolder.priorityView = (TextView)itemLayout.findViewById(R.id.priorityView);
//			 viewHolder.priorityLabel = (TextView)itemLayout.findViewById(R.id.PriorityLabel);
//			 viewHolder.statusLabel = (TextView)itemLayout.findViewById(R.id.StatusLabel);
			 viewHolder.statusCheckbox = (CheckBox)itemLayout.findViewById(R.id.statusCheckBox);
			 viewHolder.titleView = (TextView)itemLayout.findViewById(R.id.titleView);
		
			 convertView = itemLayout;
			 convertView.setTag(viewHolder);
			 
		}
		else
		{
			itemLayout = (RelativeLayout)convertView;
			viewHolder = (ViewHolder)convertView.getTag();
		}
			

		// TODO - Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined
		// in the layout file

		// TODO - Display Title in TextView
		final TextView titleView = (TextView)viewHolder.titleView;
		titleView.setText(toDoItem.getTitle());

		// TODO - Set up Status CheckBox
		final CheckBox statusView = (CheckBox)viewHolder.statusCheckbox;
		statusView.setChecked(toDoItem.getStatus().equals(ToDoItem.Status.DONE));
		
		
		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.i(TAG, "Entered onCheckedChanged()");

				// TODO - set up an OnCheckedChangeListener, which
				// is called when the user toggles the status checkbox
				if(isChecked)
					toDoItem.setStatus(ToDoItem.Status.DONE);
				else
					toDoItem.setStatus(ToDoItem.Status.NOTDONE);

			}
		});

		// TODO - Display Priority in a TextView

		final TextView priorityView = (TextView)viewHolder.priorityView;
		priorityView.setText(toDoItem.getPriority().name());

		// TODO - Display Time and Date.
		// Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and
		// time String

		final TextView dateView = (TextView)viewHolder.dateView;
		String date = ToDoItem.FORMAT.format(toDoItem.getDate());
		dateView.setText(date);

		// Return the View you just created
		return itemLayout;

	}
}
