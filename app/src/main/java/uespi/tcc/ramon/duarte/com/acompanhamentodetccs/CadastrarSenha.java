package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastrarSenha extends AppCompatActivity {
    EditText matricula,
             nome,
             email,
             senha,
             confirmaSenha,
             periodo;

    Button   cancelar,
             cadastrar;

    Spinner spinner_user,
            spinner_cursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_senha);

        matricula = (EditText)findViewById(R.id.edit_matricula);
        nome = (EditText)findViewById(R.id.edit_nome);
        email = (EditText)findViewById(R.id.edit_email);
        senha = (EditText)findViewById(R.id.edit_senha);
        confirmaSenha = (EditText)findViewById(R.id.edit_confirma_senha);
        cancelar = (Button)findViewById(R.id.bt_voltar);
        periodo = (EditText)findViewById(R.id.edit_periodo);
        cadastrar = (Button)findViewById(R.id.bt_cadastrar_senha);
        spinner_user = (Spinner) findViewById(R.id.spinner_usuario);
        spinner_cursos = (Spinner) findViewById(R.id.spinner_cursos);


        ArrayAdapter adapterCursos = ArrayAdapter.createFromResource(this,
                R.array.spinner_cursos, android.R.layout.simple_spinner_item);
        adapterCursos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cursos.setAdapter(adapterCursos);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_usuario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_user.setAdapter(adapter);
        //Toast.makeText(CadastrarSenha.this,"texto:"+spinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
        //Toast.makeText(CadastrarSenha.this,"numero:"+spinner.getSelectedItemPosition(),Toast.LENGTH_LONG).show();


        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matricula.getText().toString().isEmpty()){
                    Toast.makeText(CadastrarSenha.this,"Matricula não pode ser vazio!",Toast.LENGTH_LONG).show();
                }else if(periodo.getText().toString().isEmpty()){
                    Toast.makeText(CadastrarSenha.this,"Periodo não pode ser Vazio!Ex:2017.1",Toast.LENGTH_LONG).show();
                }else if(nome.getText().toString().isEmpty()){
                    Toast.makeText(CadastrarSenha.this,"Nome não pode ser vazio!",Toast.LENGTH_LONG).show();
                }else if(email.getText().toString().isEmpty()){
                    Toast.makeText(CadastrarSenha.this,"Email não pode ser vazio!",Toast.LENGTH_LONG).show();
                }else if(senha.getText().toString().isEmpty()){
                    Toast.makeText(CadastrarSenha.this,"Senha não pode ser vazio!",Toast.LENGTH_LONG).show();
                }else if(confirmaSenha.getText().toString().isEmpty()){
                    Toast.makeText(CadastrarSenha.this,"Confirme a Senha!",Toast.LENGTH_LONG).show();
                }else{
                    if(senha.getText().toString().equals(confirmaSenha.getText().toString())){
                        Usuario usuario = new Usuario();
                        usuario.setMatricula(Integer.valueOf(matricula.getText().toString()));
                        usuario.setNome(nome.getText().toString());
                        usuario.setEmail(email.getText().toString());
                        usuario.setSenha(confirmaSenha.getText().toString());
                        usuario.setFuncao(spinner_user.getSelectedItemPosition());
                        usuario.setCodCurso(spinner_cursos.getSelectedItemPosition());
                        usuario.setPeriodo(periodo.getText().toString());

                        new CadastrarUsuario().execute(usuario);
                    }
                }

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setUpToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    class CadastrarUsuario extends AsyncTask<Usuario , Void, String> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(CadastrarSenha.this , "Aguarde", "Cadastrando, Por Favor Aguarde...");
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Usuario... user) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String nomeModificado = Utilidades.substituiEspacos(user[0].getNome());
            System.out.println(nomeModificado);
            String dados = "matricula="+user[0].getMatricula()+
                           "&nome="+nomeModificado+ //user[0].getNome()+
                           "&email="+user[0].getEmail()+
                           "&codCurso="+user[0].getCodCurso()+
                           "&tipoUsuario="+user[0].getFuncao()+
                           "&senha="+user[0].getSenha()+
                           "&periodo="+user[0].getPeriodo();

            try {
                URL url = new URL(Host.getHostName() + "/api/cadastroUsuario.php?" + dados);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String linha;
                StringBuffer buffer = new StringBuffer();
                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }
                urlConnection.disconnect();
                return buffer.toString();

            } catch (Exception e) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(CadastrarSenha.this, "Erro de comunicação com o Servidor!", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            System.out.println(s);
            Toast.makeText(CadastrarSenha.this, s , Toast.LENGTH_LONG).show();
        }
    }
}
