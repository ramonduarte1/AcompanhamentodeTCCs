package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ProfCoordenador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ActionBar actionBar;
    ListView listIncluirParticipantes;
    IncluirAlunoAdapter incluirAlunoAdapter;
    GregorianCalendar calendar;
    CompactCalendarView compactCalendarView;
    CalendarioAdapter calendarioAdapter;
    ListView listViewCalanderio;

    TextView nome,
             matricula;

    private SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private SimpleDateFormat dataFormatBD = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
    private SimpleDateFormat mesDate = new SimpleDateFormat("MM",Locale.getDefault());


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
        //View header = navigationView.getHeaderView(0);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Prof. Coordenador");
        View header = navigationView.getHeaderView(0);
        calendar = new GregorianCalendar();//manipulador de dada



        nome = (TextView) header.findViewById(R.id.nome_usuario_perfil);
        matricula = (TextView) header.findViewById(R.id.textView_matricula_perfil);
        nome.setText(getIntent().getStringExtra("nome"));
        matricula.setText(getIntent().getStringExtra("matricula"));
        //calendario

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.getInstance().getFirstDayOfWeek());
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.showPreviousMonth();


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(ProfCoordenador.this);
                mensagem.setTitle("Adicionar Evento.");
                mensagem.setMessage("Data: "+dataFormat.format(dateClicked));
                final String data = dataFormatBD.format(dateClicked);
                // DECLARACAO DO EDITTEXT
                final EditText input = new EditText(ProfCoordenador.this);
                input.setHint("Ex: Entrega primeiro Relatório.");
                input.setSingleLine();
                input.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                input.setRawInputType(Configuration.UI_MODE_TYPE_NORMAL);
                mensagem.setView(input);
                mensagem.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //pega os dados do input e a data para mandar para a API
                        //title=teste API&start=2017-07-29&curso=1
                        String dados = "title="+input.getText().toString()+"&start="+data+"&curso=1";
                        new ProfCoordenador.CriarEventoCalendario().execute(dados);

                    }
                });
                mensagem.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }

                });

                mensagem.show();
                // FORÇA O TECLADO APARECER AO ABRIR O ALERT
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //Toast.makeText(AcessarSistema.this, "esqueceu a senha!", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //seta a data no action bar
                calendar.setTime(firstDayOfNewMonth); //seta a data do calendario atual
                final int ano = calendar.get(GregorianCalendar.YEAR);
                final int mes = calendar.get(GregorianCalendar.MONTH)+1;

                new ProfCoordenador.ConsultarEventos().execute(mes,ano);//carrega a lista de eventos da base de dados alterar para consultar por periodo
                actionBar.setTitle(dataFormat.format(firstDayOfNewMonth));
            }
        });

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
        FrameLayout inicio = (FrameLayout) findViewById(R.id.frame_inicio);
        FrameLayout projetos = (FrameLayout) findViewById(R.id.frame_projetos_tccs_cod_discip);
        FrameLayout turma = (FrameLayout) findViewById(R.id.frame_turmas_tccs_cod_discip);
        FrameLayout incluirParticipante = (FrameLayout) findViewById(R.id.frame_incluir_participante_cod_discip);
        FrameLayout calendario = (FrameLayout) findViewById(R.id.frame_calendario);
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
            actionBar.setTitle("Inicio");

            listViewCalanderio = (ListView)findViewById(R.id.lista_eventos_inicio);

            int  mes = 8,ano = 2017;
            new ProfCoordenador.ConsultarEventos().execute(mes,ano);//carrega a lista de eventos da base de dados alterar para consultar por periodo


        } else if (id == R.id.projetos_tccs_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.VISIBLE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.GONE);
            actionBar.setTitle("Projetos de Tcc's");

        } else if (id == R.id.turmas_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.VISIBLE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.GONE);
            actionBar.setTitle("Turmas");

        } else if (id == R.id.incluir_participantes_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.VISIBLE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.GONE);
            actionBar.setTitle("Incluir Participantes");

            listIncluirParticipantes = (ListView)findViewById(R.id.lista_incluir_participantes);
            new ConsultarIncluirParticipantes().execute();


        } else if (id == R.id.calendario_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.VISIBLE);
            emitirAlerta.setVisibility(View.GONE);
            listViewCalanderio = (ListView)findViewById(R.id.lista_eventos);

            final int ano = calendar.get(GregorianCalendar.YEAR);
            final int mes = calendar.get(GregorianCalendar.MONTH)+1;
            actionBar.setTitle(mes+"-"+ano);

            new ConsultarEventos().execute(mes,ano);//carrega a lista de eventos da base de dados alterar para consultar por periodo



        } else if (id == R.id.alerta_prof_coordenador) {
            inicio.setVisibility(View.GONE);
            projetos.setVisibility(View.GONE);
            turma.setVisibility(View.GONE);
            incluirParticipante.setVisibility(View.GONE);
            calendario.setVisibility(View.GONE);
            emitirAlerta.setVisibility(View.VISIBLE);
            actionBar.setTitle("Emitir Alerta");

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
        List<Professor> professores;

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
                URL url = new URL(Host.getHostName() + "/api/GetAlunosParaIncluir.php");
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
                professores = new ArrayList<Professor>();

                JSONArray array = new JSONArray(buffer.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);

                    alunos.add(new Aluno(row.getString("nome"),
                            row.getString("email"),row.getString("periodo"),row.getInt("matricula"),row.getInt("codCurso"),row.getInt("funcao")));
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
    class CriarEventoCalendario extends AsyncTask<String , Void, String> {

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ProfCoordenador.this , "Aguarde", "Verificando os Dados, Por Favor Aguarde...");
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
                URL url = new URL(Host.getHostName() + "/api/InsereEvento.php?" + dados);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Toast.makeText(ProfCoordenador.this, "Adicionado com sucesso!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    class ConsultarEventos extends AsyncTask<Integer, Void, Void> {
        ProgressDialog dialog;
        List<Evento> eventos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ProfCoordenador.this , "Aguarde", "Consultando Calendario...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int mes = params[0];
            int ano = params[1];
            String dados = "mes="+mes+"&ano="+ano;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(Host.getHostName() + "/api/GetEventos.php?"+dados);
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

                eventos = new ArrayList<Evento>();
                JSONArray array = new JSONArray(buffer.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    eventos.add(new Evento(row.getInt("id"),row.getString("title"),
                            row.getString("start"),row.getInt("curso")));
                }
                runOnUiThread(new Runnable(){

                    @Override
                    public void run(){
                        calendarioAdapter = new CalendarioAdapter(ProfCoordenador.this, eventos);
                        listViewCalanderio.setAdapter(calendarioAdapter);
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
