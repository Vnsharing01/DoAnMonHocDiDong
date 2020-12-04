package vn.detecttext.subtext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class TranslateActivity extends AppCompatActivity {

    private EditText edtSourceText;
    private TextView tvTranslate, tvSourceLang;
    private Button btnTranslate;

    private String sourceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        edtSourceText = findViewById(R.id.edt_sourceText);
        tvSourceLang = findViewById(R.id.tv_sourceLang);
        tvTranslate = findViewById(R.id.tv_translatedText);
        btnTranslate = findViewById(R.id.btn_translate);

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                identifyLanguage();
            }
        });
    }

//    private void identifyLanguage() {
//        sourceText = edtSourceText.getText().toString();
//
//        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
//                .getLanguageIdentification();
//
//        tvSourceLang.setText("Detecting..");
//        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
//            @Override
//            public void onSuccess(String s) {
//                if (s.equals("und")){
//                    Toast.makeText(getApplicationContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();
//
//                }
//                else {
//                    getLanguageCode(s);
//                }
//            }
//        });
//    }
//
//    private void getLanguageCode(String language) {
//        int langCode;
//        switch (language){
//            case "vi":
//                langCode = FirebaseTranslateLanguage.VI;
//                tvSourceLang.setText("Vietnam");
//                break;
//            case "en":
//                langCode = FirebaseTranslateLanguage.EN;
//                tvSourceLang.setText("English");
//
//                break;
//            case "ja":
//                langCode = FirebaseTranslateLanguage.JA;
//                tvSourceLang.setText("Japanese");
//
//                break;
//            default:
//                langCode = 0;
//        }
//
//        translateText(langCode);
//    }
//
//    private void translateText(int langCode) {
//        tvTranslate.setText("Translating..");
//        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
//                //from language
//                .setSourceLanguage(langCode)
//                // to language
//                .setTargetLanguage(FirebaseTranslateLanguage.VI)
//                .build();
//
//        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
//                .getTranslator(options);
//
//        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
//                .build();
//
//
//        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        tvTranslate.setText(s);
//                    }
//                });
//            }
//        });
//    }

}