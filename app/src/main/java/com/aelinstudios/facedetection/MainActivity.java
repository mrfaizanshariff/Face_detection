package com.aelinstudios.facedetection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton;
    // we need to declare a flag here to make sure that the permission we are asking for  is received or not
    private final static int REQUEST_IMAGE_CAAPTURE=123;// give any value u want
    // instance of FirebaseVisionImage class
    private FirebaseVisionImage image;
    // instance of FirebaseVisionFaceDetector
    private FirebaseVisionFaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // we need to initialize the FirebaseApp in all the activities where we will be using it
        FirebaseApp.initializeApp(this);
        cameraButton=findViewById(R.id.camera_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // now  on the click we will open the camera for wich the following procedure is to be followed

                Intent takepicfrmcamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takepicfrmcamera.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takepicfrmcamera,REQUEST_IMAGE_CAAPTURE);
                     // the 2nd parameter requestcode is the flag declared by us in the beginning
                }



            }
        });
    }

    //now we have to get the image captured frm the camera so

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAAPTURE && resultCode==RESULT_OK){
            // the processed image frm vision library is the form of bitmap so we have to receive it in a bundle first
            Bundle extras = data.getExtras();
            Bitmap bitmap= (Bitmap) extras.get("data");
            // now we have to call a self defined func
            detectFace(bitmap);
        }
    }

    private void detectFace(Bitmap bitmap){
        // High-accuracy landmark detection and face classification
        FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                        .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setMinFaceSize(0.15f)
                        .setTrackingEnabled(true)
                        .build();

        try {
            //the below two lines should be surrounded by try catch
            image=FirebaseVisionImage.fromBitmap(bitmap);
            detector= FirebaseVision.getInstance().getVisionFaceDetector(highAccuracyOpts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // now we have to set up things so that we get all the values like smile,no of faces and all
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                String resultText="";
                int i = 1;
                for(FirebaseVisionFace face : firebaseVisionFaces){
                    resultText=resultText.concat("\n"+i+".")
                                .concat("\nSmile :"+face.getSmilingProbability()*100+"%")
                                .concat("\nLeft eye:"+face.getLeftEyeOpenProbability()*100+"%")
                                .concat("\nRight eye:"+face.getRightEyeOpenProbability()*100+"%");
                    i++;
                }
                if(firebaseVisionFaces.size()==0){
                    Toast.makeText(MainActivity.this,"No faces ",Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle bundle = new Bundle();
                    // now passing the result fetched frm the for loop to the facedetectionfaizan class through a bundle which will be passed to the Resultdialo.java class

                    bundle.putString(Facedetectionfaizan.Result_text,resultText);
                    DialogFragment resultdialog = new Resultdialog();
                    resultdialog.setArguments(bundle);
                    resultdialog.setCancelable(false);
                    resultdialog.show(getSupportFragmentManager(),Facedetectionfaizan.Result_dialog);
                }

                }

        });
    }
}