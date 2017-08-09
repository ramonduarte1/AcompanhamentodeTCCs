package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfCoordenador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listIncluirParticipantes;
    IncluirAlunoAdapter incluirAlunoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenador_discipina);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.coordenador_discipina, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FrameLayout inicio = (FrameLayout) findViewById(R.id.frame_inicio_cod_discip);
        FrameLayout projetos = (FrameLayout) findViewById(R.id.frame_projetos_tccs_cod_discip);
        FrameLayout turma = (FrameLayout) findViewById(R.id.frame_turmas_tccs_cod_discip);
        FrameLayout incluirParticipante = (FrameLayout) findViewById(R.id.frame_incluir_participante_cod_discip);
        FrameLayout calendario = (FrameLayout) findViewById(R.id.frame_calendario_cod_discip);
        FrameLayout emitirAlerta = (FrameLayout) findViewById(R.id.frame_alerta_cod_discip);

        int id = item.getItemId();
        if (id == R.id.inicio_prof_coordenador) {
            // Handle the camera action
            inicio.setVisibility(View.VISIBLE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.GONE);

        } else if (id == R.id.projetos_tccs_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.VISIBLE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.GONE);

        } else if (id == R.id.turmas_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.VISIBLE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.GONE);

        } else if (id == R.id.incluir_participantes_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.VISIBLE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.GONE);

            listIncluirParticipantes = (ListView)findViewById(R.id.lista_incluir_participantes);
            new ConsultarIncluirParticipantes().execute();


        } else if (id == R.id.calendario_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.VISIBLE);
            emitirAlerta.setVisibility(View.GONE);

        } else if (id == R.id.alerta_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.VISIBLE);

        } else if (id == R.id.sair_sistema) {
            startActivity(new Intent(ProfCoordenador.this , AcessarSistema.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //consultar alunos para incluir na disciplina de tcc
    class ConsultarIncluirParticipantes extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        List<Aluno> alunos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ProfCoordenador.this , "Aguarde", "Consultando...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(Host.getHostName() + "/api/GetAlunosParaIncluir.php?");
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

                alunos = new ArrayList<Aluno>();
                JSONArray array = new JSONArray(buffer.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    alunos.add(new Aluno(row.getString("nome"),
                            row.getString("email"),row.getString("periodo"),row.getInt("matricula"),row.getInt("codCurso")));
                }
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        incluirAlunoAdapter = new IncluirAlunoAdapter(ProfCoordenador.this, alunos);
                        listIncluirParticipantes.setAdapter(incluirAlunoAdapter);
                    }
                });

            }  catch (Exception e) {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        Toast.makeText(ProfCoordenador.this, "Erro de comunicação com o Servidor!", Toast.LENGTH_LONG).show();
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
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            dialog.dismiss();
        }

    }

}
