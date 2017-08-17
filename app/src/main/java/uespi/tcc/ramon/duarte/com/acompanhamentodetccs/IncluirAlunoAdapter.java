package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
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
import java.util.List;

/**
 * Created by ramon on 09/08/17.
 */

public class IncluirAlunoAdapter extends BaseAdapter {

        Context context;
        private List<Aluno> lista;
        private LayoutInflater inflater;
        private int posicaoGetView;

        public IncluirAlunoAdapter(Context context, List<Aluno> listaResult) {
            this.context = context;
            this.lista = listaResult;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public Object getItem(int position) {
            return lista.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Aluno aluno = lista.get(position);
            final ItemSuporte itemSuporte;
            this.posicaoGetView = position;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.aluno_participante, null);
                itemSuporte = new ItemSuporte();
                itemSuporte.matricula = (TextView) convertView.findViewById(R.id.matricula_participante);
                itemSuporte.nome = (TextView) convertView.findViewById(R.id.nome_participante);
                itemSuporte.email = (TextView) convertView.findViewById(R.id.email_participante);


                convertView.setTag(itemSuporte);
            } else {
                //se a view já existe pega os itens.
                itemSuporte = (ItemSuporte) convertView.getTag();
            }

            itemSuporte.matricula.setText("Matricula: "+aluno.getMatricula());
            itemSuporte.nome.setText("Nome: "+ aluno.getNome());
            itemSuporte.email.setText("Email: "+aluno.getEmail());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(context);
                    mensagem.setTitle("Matricular Aluno!");
                    //mensagem.setMessage("Insira a sua matricula, será enviado uma nova senha para o email cadastrado!");
                    mensagem.setPositiveButton("Matricular", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String dados = "matricula="+aluno.getMatricula()+"&tipoUsuario="+aluno.getFuncao();
                            System.out.println("jhgfcvbhfgcvbbgh"+dados);
                         new IncluirUsuario().execute(dados);


                        }
                    });
                    mensagem.setNeutralButton("Voltar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                        }

                    }).show();
                }
            });

            return convertView;
        }

        private class ItemSuporte{
            TextView  matricula,
                      nome,
                      email;

        }
    class IncluirUsuario extends AsyncTask<String , Void, String> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "Aguarde", "Incluindo Aluno, Por Favor Aguarde...");
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String dados = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(Host.getHostName() + "/api/IncluirUsuario.php?" + dados);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
            dialog.dismiss();
            lista.remove(posicaoGetView);
            notifyDataSetChanged();
        }

    }
}



