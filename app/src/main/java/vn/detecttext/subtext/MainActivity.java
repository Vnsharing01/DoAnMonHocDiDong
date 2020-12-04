package vn.detecttext.subtext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CAMERA_REQUEST_CODE = 200;
    public static final int STORAGE_REQUEST_CODE = 400;
    public static final int IMAGE_PICK_GALLERY_CODE = 1000;
    public static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    private EditText edtResult;
    private ImageView imgView;
    private ImageView imgCamera, imgGallery, imgMic, imgSpeaker;
    private Button tvTranslate;
    TextToSpeech toSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidget();
//        imgMic = findViewById(R.id.img_mic);
//        imgMic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getSpeechInput();
//            }
//        });//mic

        // camera permission
        cameraPermission = new  String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // storage permission
        storagePermission = new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    }
    public void getSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(intent,200);
        }else {
            Toast.makeText(this,"your device don`t support speech input",Toast.LENGTH_LONG).show();
        }
    } //giọng noi

    private void setWidget() {
        edtResult = findViewById(R.id.edt_result);
        imgView = findViewById(R.id.img_view);
        imgCamera = findViewById(R.id.img_camera);
        imgGallery = findViewById(R.id.img_gallery);
        imgMic = findViewById(R.id.img_mic);
        imgSpeaker = findViewById(R.id.img_speaker);
        tvTranslate = findViewById(R.id.btn_translateText);

        imgCamera.setOnClickListener(this);
        imgGallery.setOnClickListener(this);
        imgMic.setOnClickListener(this);
        imgSpeaker.setOnClickListener(this);
        tvTranslate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_camera:
                // camera option click
                if(!checkCameraPermission()){
                    // nếu chưa cấp quyền camera , hãy yêu cầu xin quyền này
                    requestCameraPermission();
                } else {
                    // đã được cấp quyền camera, có thể chụp ảnh
                    pickCamera();
                }
                break;
            case R.id.img_gallery:
                // gallery option click
                if(!checkStoragePermission()){
                    // nếu chưa cấp quyền vào thư viện  , hãy yêu cầu xin quyền này
                    requestStoragePermission();
                } else {
                    // đã được cấp quyền camera, có thể chụp ảnh
                    pickGallery();
                }
                break;
            case R.id.img_mic: // chỗ này để click dc vào mic này bạn
                getSpeechInput();
                break;
            case R.id.img_speaker: // chỗ này d
                final String text = edtResult.getText().toString();

                toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR){
                            toSpeech.setLanguage(Locale.getDefault());
                            toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });
                break;
            case R.id.btn_translateText:
                break;
        }
    }

    /** dưới 3 func dưới là để xử lý gallery */
    private void pickGallery() {
        // intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        // set intent type to image
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.
                checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    /** dưới 3 func dưới là để xử lý camera */
    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.
                checkSelfPermission(this, Manifest.permission.CAMERA) ==  (PackageManager.PERMISSION_GRANTED);

        boolean resultWrite = ContextCompat.
                checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  (PackageManager.PERMISSION_GRANTED);
        return resultCamera && resultWrite;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickCamera() {
        // intent to take image from camera, ảnh sẽ đc lưu vào thư viện
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pic"); //title picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To Text"); // discription
        image_uri  = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    // xử lý permisison trả về
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length >0 ){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    } else {
                        Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length >0 ){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if ( writeStorageAccepted){
                        pickGallery();
                    } else {
                        Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // got image from camera
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtResult.setText(result.get(0));
                }
                break;
        }// set mic cho vao editext
// got image from camera
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // got image from gallery now crop it
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) // enable image guidlines
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // got image from  camera now crop it
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON) // enable image guidlines
                        .start(this);
            }
        }
        //get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); // get image uri
                // set image to image view
                imgView.setImageURI(resultUri);

                // get drawable bitmap for text recognition
                BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    //get text from sb until there is o text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                    // set text to editext
                    edtResult.setText(sb.toString());
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //if there is any error show it
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_LONG).show();
            }
        }
    }

}