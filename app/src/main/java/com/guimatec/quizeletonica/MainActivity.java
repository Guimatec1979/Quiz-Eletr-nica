package com.guimatec.quizeletonica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    //Definir Spinner
    Integer SpinnerValue;

    Spinner sp;
    //montar o array do spinner
    Integer questoes[] = {10,20,30};
    //Definir array adapter of tipo int
    ArrayAdapter<Integer> adapter;

    Button itemClick;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        itemClick = findViewById(R.id.btnIniciar);

        sp = findViewById(R.id.spNumero);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, questoes);


        //conjunto adapter da spinner
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerValue = (Integer)sp.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switch (SpinnerValue){
                   case 10:
                       intent = new Intent(MainActivity.this, dezQuestoes.class);
                       startActivity(intent);
                       break;
                   case 20:
                       intent = new Intent(MainActivity.this, vinteQuestoes.class);
                       startActivity(intent);
                       break;
                   case 30:
                       intent = new Intent(MainActivity.this, trintaQuestoes.class);
                       startActivity(intent);
                       break;

               }
            }
        });


    }
    public void share(View view) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("Text/plain");
        String shareBody = "Seu corpo aqui";
        String shareSub = "Seu Assunto aqui";
        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        startActivity(Intent.createChooser(myIntent,"Compartilhar via."));
    }


}
