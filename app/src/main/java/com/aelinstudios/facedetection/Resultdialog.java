package com.aelinstudios.facedetection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Resultdialog extends DialogFragment {
private Button okbtn;
private TextView resulttv;
// now override the oncreateview() by pressing ctrl+o


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //inflating the result fragment
        View view=inflater.inflate(R.layout.fragments,container,false);
        //we will initialize an empty String which will be populated by the result
        String resultText="";
        //note that we use view.findviewbyId(); because we are in onCreateView()
        okbtn=view.findViewById(R.id.result_ok_button);
        resulttv=view.findViewById(R.id.result_text_view);
        // now we have to initialize the bundle so that we can pass the values to the result dialog box
        Bundle bundle= getArguments();
        // assigning the Result_text frm the Facedetectionfaizan.java class to the resultText of this class
        // basically the output frm firebase gets populated on the resulttext string of this class
        resultText=bundle.getString(Facedetectionfaizan.Result_text);

        resulttv.setText(resultText);
        // now wen we click on the ok button the result fragment must be dismissed

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // now changing this return statement
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
}
