package com.ashrafcoder.astrofacts.dialog;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ashrafcoder.astrofacts.R;

import org.w3c.dom.Text;

/**
 * Created by Ashraf-XCODER on 1/13/2017.
 */

public class HiDialogFragment extends DialogFragment implements View.OnClickListener{

    private Button okButton;
    private Button cancelButton;
    private TextView titleTextView;
    private TextView messageTextView;

    //Listener
    private OnHiDialogActionListener dialogActionListener;

    private OnOkButtonClickListener okButtonClickListener;
    private OnCancelButtonClickListener cancelButtonClickListener;

    //onClick xml attribute doesnt work in DialogFragment
    //imlement OnClickListener

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.hi_dialog_fragment, null);

        okButton = (Button) root.findViewById(R.id.okButton);
        cancelButton = (Button) root.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        titleTextView = (TextView) root.findViewById(R.id.titleTextView);
        messageTextView = (TextView) root.findViewById(R.id.detailTextView);


        setCancelable(true);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okButton){
            if (dialogActionListener != null){
                dialogActionListener.sayHi("OK Button Tapped");
            }
            //Toast.makeText(getActivity(), "Ok Button Tapped", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else {
            if (dialogActionListener != null){
                dialogActionListener.sayHi("Cancel Button Tapped");
            }
            //Toast.makeText(getActivity(), "Cancel Button Tapped", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    public void setOnHiDialogActionListener(OnHiDialogActionListener listener){
        this.dialogActionListener = listener;
    }

    public void setOnOkButtonClickListener(OnOkButtonClickListener listener){
        this.okButtonClickListener = listener;
    }
    public void setOnCancelButtonClickListener(OnCancelButtonClickListener listener){
        this.cancelButtonClickListener = listener;
    }

    public interface OnHiDialogActionListener {
        void sayHi(String s);
    }

    public interface OnOkButtonClickListener {
        void sayHi(String s);
    }

    public interface OnCancelButtonClickListener {
        void sayHi(String s);
    }
}
