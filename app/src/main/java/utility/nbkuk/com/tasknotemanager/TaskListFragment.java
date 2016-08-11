package utility.nbkuk.com.tasknotemanager;

/**
 * Created by jason.dobo on 12/07/2016.
 */
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TaskDatabaseHandler taskdb;
    private Cursor taskcursor;
    private SimpleCursorAdapter taskadaptor;

    // to do Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";

    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskdb = new TaskDatabaseHandler(getContext());
        taskcursor = taskdb.getAllTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tasklist_layout, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get my list view to display tasks
        ListView myListView = (ListView) getView().findViewById(R.id.myTaskListView);

        // Used to map notes entries from the database to views
        String[] from = new String[]{
                taskcursor.getColumnName(taskcursor.getColumnIndex(KEY_NAME)),
                taskcursor.getColumnName(taskcursor.getColumnIndex(KEY_STATUS)),
                taskcursor.getColumnName(taskcursor.getColumnIndex(KEY_DATE))};
        int[] to = new int[]{
                R.id.TaskTextView,
                R.id.TaskCheckbox,
                R.id.TaskDate};

        // Now create an array adapter and set it to display using our row
        getActivity().startManagingCursor(taskcursor);
        taskadaptor = new SimpleCursorAdapter(getContext(), R.layout.taskrow_layout, taskcursor, from, to);

        // Custom set view binder to handle check box and date in simple cursor adapter
        taskadaptor.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int statusIndex = cursor.getColumnIndex(KEY_STATUS);
                int dateIndex = cursor.getColumnIndex(KEY_DATE);
                if (statusIndex == columnIndex) {
                    int complete = cursor.getInt(statusIndex);
                    CheckBox checkBox = (CheckBox) view; // Cast view as check box
                    checkBox.setTag(cursor.getInt(cursor.getColumnIndex(KEY_ID))); // Add tag to store _id
                    switch(complete) {
                        case 0: ((CheckBox) view).setChecked(false); break;
                        case 1: ((CheckBox) view).setChecked(true); break;
                    }
                    return true;
                }
                else if (dateIndex == columnIndex) {
                    long dtMili = cursor.getLong(columnIndex);
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(dtMili);
                    String date = formatter.format(calendar.getTime());
                    ((TextView) view).setText(date);
                    return true;
                }
                return false;
            }
        });

        myListView.setAdapter(taskadaptor);

        // set listeners
        myListView.setOnItemClickListener(this);
        ImageButton addbutton = (ImageButton)getView().findViewById(R.id.addTaskButton);
        addbutton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view == getView().findViewById(R.id.addTaskButton)) {
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            TaskDialogFragment editTaskDialog = new TaskDialogFragment();
            editTaskDialog.show(getActivity().getSupportFragmentManager(), "Jason");

//            // Get the users task
//            EditText edittext = (EditText) getView().findViewById(R.id.newTaskText);
//            String task = edittext.getText().toString();
//            // Make sure its not empty
//            if(!task.equals("")) {
//                taskdb.addTask(task);
//                edittext.setText(null);
//            }
        }

        // Dismiss virtual keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().findViewById(R.id.newTaskText).getWindowToken(), 0);

        // Redraw screen maybe should be more elegant
        ListView myListView = (ListView) getView().findViewById(R.id.myTaskListView);
        taskcursor = taskdb.getAllTasks();
        taskadaptor.changeCursor(taskcursor);
        myListView.setAdapter(taskadaptor);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


    }

    public void onCheckChanged(View view) {

    }

}
