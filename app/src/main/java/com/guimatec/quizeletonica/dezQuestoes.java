package com.guimatec.quizeletonica;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class dezQuestoes extends AppCompatActivity {
    
    ProgressBar meuPb;
    RadioGroup meuGrupo;
    RadioButton rbA,rbB,rbC,rbD;
    TextView pergunta,titulo;
    Button botaoOk;
    ConstraintLayout meuLayout;
    
    Animation some;
    Animation aparece;
    
    ArrayList<Questao> questoes;
    int rodada = 0;
    int acertos = 0;
    int resposta = 0;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dez_questoes);

        String minhaUrl="http://www.json-generator.com/api/json/get/cqKxaiNuNu?indent=2";


        
        meuPb = findViewById(R.id.meuPB);
        meuLayout = findViewById(R.id.meuLayout);
        meuGrupo = findViewById(R.id.meuGrupo);
        rbA = findViewById(R.id.radioButton1);
        rbB = findViewById(R.id.radioButton2);
        rbC = findViewById(R.id.radioButton3);
        rbD = findViewById(R.id.radioButton4);
        
        pergunta = findViewById(R.id.txtPergunta);
        titulo = findViewById(R.id.txtTitulo);
        
        botaoOk = findViewById(R.id.botaoConfirmar);
        botaoOk.setEnabled(false);
        questoes = new ArrayList<Questao>();
        
        some = new AlphaAnimation(1,0);
        aparece = new AlphaAnimation(0,1);
        
        some.setDuration(1000);
        aparece.setDuration(1000);
        
        meuLayout.setVisibility(View.GONE);
        meuLayout.startAnimation(aparece);
        
        aparece.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                meuLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                meuPb.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        
        some.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                meuLayout.setVisibility(View.VISIBLE);
                meuPb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rodada>=questoes.size()){
                    fimDeJogo();
                }else{
                    atualizaView();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        new JSonTask().execute(minhaUrl);

        botaoOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicaProxima();
            }
        });
        meuGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if (i==R.id.radioButton1){
                    resposta = 1;
                }
                if (i==R.id.radioButton2){
                    resposta = 2;
                }
                if (i==R.id.radioButton3){
                    resposta = 3;
                }
                if (i==R.id.radioButton4){
                    resposta = 4;
                }
                botaoOk.setEnabled(true);
            }
        });

        
        
        
        
    }

    void atualizaView() {
        meuGrupo.clearCheck();
        pergunta.setText(questoes.get(rodada).getPergunta());
        rbA.setText(questoes.get(rodada).getRespA());
        rbB.setText(questoes.get(rodada).getRespB());
        rbC.setText(questoes.get(rodada).getRespC());
        rbD.setText(questoes.get(rodada).getRespD());
        botaoOk.setEnabled(false);
        meuLayout.startAnimation(aparece);

    }
    void clicaProxima(){
        if (resposta == questoes.get(rodada).getCorreta()){
            acertos++;
        }
        rodada++;
        meuLayout.startAnimation(some);
    }

    void fimDeJogo() {
        meuLayout.setVisibility(View.GONE);
        meuPb.setVisibility(View.GONE);
        criaAlerta();

    }

    private void criaAlerta() {
        AlertDialog.Builder alerta;
        alerta = new AlertDialog.Builder(dezQuestoes.this);
        alerta.setTitle("Fim das Perguntas");
        alerta.setMessage("Você acertou "+ acertos +" questões de 10");
        alerta.setIcon(R.drawable.trofeu);
        alerta.setCancelable(false);

        alerta.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(dezQuestoes.this,MainActivity.class));
            }
        });
        alerta.setPositiveButton("Jogar Novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                acertos =0;
                rodada = 0;
                atualizaView();
            }
        });

        alerta.create();
        alerta.show();

    }

    class Questao{
        String pergunta;
        String respA;
        String respB;
        String respC;
        String respD;
        int correta;

        public Questao(String pergunta, String respA, String respB, String respC, String respD, int correta) {
            this.pergunta = pergunta;
            this.respA = respA;
            this.respB = respB;
            this.respC = respC;
            this.respD = respD;
            this.correta = correta;
        }

        public String getPergunta() {
            return pergunta;
        }

        public void setPergunta(String pergunta) {
            this.pergunta = pergunta;
        }

        public String getRespA() {
            return respA;
        }

        public void setRespA(String respA) {
            this.respA = respA;
        }

        public String getRespB() {
            return respB;
        }

        public void setRespB(String respB) {
            this.respB = respB;
        }

        public String getRespC() {
            return respC;
        }

        public void setRespC(String respC) {
            this.respC = respC;
        }

        public String getRespD() {
            return respD;
        }

        public void setRespD(String respD) {
            this.respD = respD;
        }

        public int getCorreta() {
            return correta;
        }

        public void setCorreta(int correta) {
            this.correta = correta;
        }
    }


    private class JSonTask extends AsyncTask<String, String,String> {
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(params [0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> "+ line);
                }
                return buffer.toString();


            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if (connection != null){
                    connection.disconnect();

                }
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("meuLog",""+result);

            try{
                JSONObject listaJson = new JSONObject(result);
                titulo.setText(listaJson.getString("titulo"));
                JSONArray questionario = listaJson.getJSONArray("questionario");

                for (int i=0; i<questionario.length(); i++){
                    JSONObject questao = questionario.getJSONObject(i);

                    String perg = questao.getString("Pergunta");
                    String respA = questao.getString("respA");
                    String respB = questao.getString("respB");
                    String respC = questao.getString("respC");
                    String respD = questao.getString("respD");
                    int correta = questao.getInt("correta");

                    Questao minhaQuestao = new Questao(perg,respA,respB,respC,respD,correta);
                    questoes.add(minhaQuestao);
                }

                atualizaView();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
