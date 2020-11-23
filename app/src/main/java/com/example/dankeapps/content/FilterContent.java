package com.example.dankeapps.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.dankeapps.MainActivity;
import com.example.dankeapps.R;
import com.google.android.material.textfield.TextInputLayout;

public class FilterContent extends Activity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button OkBtn, ResetBtn;
    TextInputLayout minUp, maxUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_filter);
        OkBtn = findViewById(R.id.OKBtn);
        ResetBtn = findViewById(R.id.ResetBtn);
        minUp = findViewById(R.id.minUpahLay);
        maxUP = findViewById(R.id.maxUpahLay);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.7));

        radioGroup = findViewById(R.id.layoutRadio);

        //reset button
        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                minUp.getEditText().setText("");
                maxUP.getEditText().setText("");
                radioGroup.clearCheck();
            }
        });

        //apply fillter
        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    String min = minUp.getEditText().getText().toString().trim();
                    String max = maxUP.getEditText().getText().toString().trim();

                    // nek kategori ra dipilih
                    if (radioGroup.getCheckedRadioButtonId() == -1){
                        int minup = Integer.parseInt(min);
                        int maxup = Integer.parseInt(max);

                        intent.putExtra("minup", minup);
                        intent.putExtra("maxup", maxup);
                        setResult(2, intent);

                    }
                    // nek ora set range upah
                    else if (radioGroup.getCheckedRadioButtonId() != -1){
                        int radioId = radioGroup.getCheckedRadioButtonId();
                        radioButton = findViewById(radioId);
                        String kategori = radioButton.getText().toString();

                        intent.putExtra("Kategori", kategori);
                        setResult(3, intent);

                    }
                    // nek set kabeh
                    else {
                        int radioId = radioGroup.getCheckedRadioButtonId();
                        radioButton = findViewById(radioId);

                        String kategori = radioButton.getText().toString();
                        int minup = Integer.parseInt(min);
                        int maxup = Integer.parseInt(max);

                        intent.putExtra("Kategori", kategori);
                        intent.putExtra("minup", minup);
                        intent.putExtra("maxup", maxup);
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                }
                //nek ra set apa2
                catch (Exception e){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            }
        });

    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }
}
