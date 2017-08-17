package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AcessarSistema extends AppCompatActivity {
    private EditText editMatricula;
    private EditText editSenha;
    private Button btCriarConta;
    private Button btEntrar;
    private TextView esqueceuASenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acessar_sistema);


        editMatricula = (EditText) findViewById(R.id.edit_matricula);
        editSenha = (EditText) findViewById(R.id.edit_senha);
        btCriarConta = (Button) findViewById(R.id.bt_criar_conta);
        btEntrar = (Button) findViewById(R.id.bt_entrar);
        esqueceuASenha = (TextView) findViewById(R.id.esqueceu_a_senha);

        //temporario
       // String requisicao = "matricula=" + 1049903 + "&senha=" + 12345 + "";

        //new VerificaLogin().execute(requisicao);

        //temporario

        esqueceuASenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(AcessarSistema.this);
                mensagem.setTitle("Esqueceu a senha?");
                mensagem.setMessage("Insira a sua matricula, será enviado uma nova senha para o email cadastrado!");
                // DECLARACAO DO EDITTEXT
                final EditText input = new EditText(AcessarSistema.this);
                input.setHint("Matricula");
                input.setSingleLine();
                input.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                mensagem.setView(input);
                mensagem.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //enviar senha para o email
                        Toast.makeText(getApplicationContext(), "Email enviado com sucesso!",
                                Toast.LENGTH_SHORT).show();


                    }
                });
                mensagem.setNeutralButton("Voltar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(), input.getText().toString().trim(),
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }

                });

                mensagem.show();
                // FORÇA O TECLADO APARECER AO ABRIR O ALERT
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //Toast.makeText(AcessarSistema.this, "esqueceu a senha!", Toast.LENGTH_LONG).show();
            }
        });

        btCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AcessarSistema.this, CadastrarSenha.class));
                //finish();
            }
        });

            btEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Usuario user = null;

                    if (editMatricula.getText().toString().isEmpty()) {
                        Toast.makeText(AcessarSistema.this, "Matricula não pode ser Vazio!", Toast.LENGTH_SHORT).show();
                    } else if (editSenha.getText().toString().isEmpty()) {
                        Toast.makeText(AcessarSistema.this, "Senha não pode ser Vazio!", Toast.LENGTH_SHORT).show();
                    } else {

                        String requisicao = "matricula=" + editMatricula.getText().toString() + "&senha=" + editSenha.getText().toString() + "";

                       new VerificaLogin().execute(requisicao);

                    }
                }
            });

    }

    class VerificaLogin extends AsyncTask<String , Void, Usuario> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(AcessarSistema.this , "Aguarde", "Verificando os Dados, Por Favor Aguarde...");
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Usuario doInBackground(String... params) {
            String dados = params[0];
            Usuario usuario = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(Host.getHostName() + "/api/VerificaLogin.php?" + dados);
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

                usuario = new Usuario();
                JSONObject jsonObject = new JSONObject(buffer.toString());
                usuario.setMatricula(jsonObject.getInt("matricula"));
                usuario.setNome(jsonObject.getString("nome"));
                usuario.setEmail(jsonObject.getString("email"));
                usuario.setFuncao(jsonObject.getInt("funcao"));
                usuario.setCodCurso(jsonObject.getInt("codCurso"));
                //usuario.setLogado(jsonObject.getInt("logado"));

                return usuario;

            } catch (JSONException e) {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                      Toast.makeText(AcessarSistema.this, "Usuario ou senha invalidos!", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            } catch (Exception e) {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                      Toast.makeText(AcessarSistema.this, "Erro de comunicação com o Servidor!", Toast.LENGTH_LONG).show();
                    }
                });

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
        protected void onPostExecute(Usuario user) {
            super.onPostExecute(user);
            dialog.dismiss();
            if(user != null) {
                switch (user.getFuncao()) {

                    case 0://Coordenador curso
                        Intent intent = new Intent(AcessarSistema.this,CoordenadorCurso.class);
                        Toast.makeText(getApplicationContext(), "Bem vindo, " + user.getNome() + "!", Toast.LENGTH_LONG).show();
                        intent.putExtra("nome", user.getNome());
                        intent.putExtra("matricula",String.valueOf(user.getMatricula()));
                        startActivity(intent);

                        finish();
                        break;
                    case 1://Coodenador Disciplina
                        Toast.makeText(getApplicationContext(), "Bem vindo, " + user.getNome() + "!", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(AcessarSistema.this, ProfCoordenador.class);
                        intent1.putExtra("nome", user.getNome());
                        intent1.putExtra("matricula",String.valueOf(user.getMatricula()));
                        startActivity(intent1);

                        finish();
                        break;
                    case 2://Orientador
                        Toast.makeText(getApplicationContext(), "Bem vindo, " + user.getNome() + "!", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(AcessarSistema.this, OrientadorActivity.class);
                        intent2.putExtra("nome", user.getNome());
                        intent2.putExtra("matricula",String.valueOf(user.getMatricula()));
                        startActivity(intent2);

                        finish();
                        break;
                    case 3://Aluno
                        Intent intent3 = new Intent(AcessarSistema.this,AlunoActivity.class);
                        intent3.putExtra("nome", user.getNome());
                        intent3.putExtra("matricula",String.valueOf(user.getMatricula()));
                        Toast.makeText(getApplicationContext(), "Bem vindo, " + user.getNome() + "!", Toast.LENGTH_LONG).show();
                        startActivity(intent3);
                        finish();
                        break;
                    default:

                        break;
                }
            }
        }
    }
}

