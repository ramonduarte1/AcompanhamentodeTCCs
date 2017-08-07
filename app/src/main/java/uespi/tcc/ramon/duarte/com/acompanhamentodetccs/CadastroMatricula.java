package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroMatricula extends AppCompatActivity {
    Button btCancelarCadastro;
    Button btCadastrar;
    EditText editMatricula;
    Spinner combobox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        editMatricula = (EditText) findViewById(R.id.edit_matricula);
        btCancelarCadastro = (Button)findViewById(R.id.bt_cancelar_cadastro);
        btCadastrar = (Button)findViewById(R.id.bt_cadastrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMatricula.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Matricula não pode ser Vazio!", Toast.LENGTH_SHORT).show();
                }else {
                    Aluno aluno = new Aluno(Integer.parseInt(editMatricula.getText().toString()));
                    InsereAluno insere = new InsereAluno();
                    insere.execute(aluno);
                }
            }
        });

        btCancelarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadastroMatricula.this, AcessarSistema.class));
                finish();

            }
        });
    }
    public  class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            Toast.makeText(parent.getContext(), "Selecionado: " +
                    parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }





    class InsereAluno extends AsyncTask<Aluno, Void, String> {


        @Override
        protected String doInBackground(Aluno... params) {
            String dados = "matricula="+params[0].getMatricula();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(Host.getHostName()+"/api/InsereAluno.php?"+dados);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String linha;
                StringBuffer buffer = new StringBuffer();
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }
                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String dados) {
            // Faça alguma coisa com os dados
            Toast.makeText(getApplicationContext(),dados,Toast.LENGTH_SHORT).show();

        }



    }
}
