package utility.nbkuk.com.tasknotemanager;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by jason.dobo on 19/07/2016.
 */
public class TaskDialogFragment extends DialogFragment {

    EditText mEditText;

    public TaskDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taskedit_layout, container);
        mEditText = (EditText) view.findViewById(R.id.dialogedittext);
        getDialog().setTitle("Hello");

        return view;
    }
}
