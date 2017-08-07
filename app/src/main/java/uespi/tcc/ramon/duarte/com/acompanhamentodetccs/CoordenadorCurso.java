package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CoordenadorCurso extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView webInicial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //InsereAlunoThread dow = new InsereAlunoThread();
        //Aluno aluno = new Aluno(1049905,"ramon%20medeirods%20duarte","ramdonduarte1@hotmail.com",2);
        //Aluno aluno = new Aluno(12332,"ramonnn","r@r.com",1);
        //Toast.makeText(this,aluno.salvarAluno(),Toast.LENGTH_LONG).show();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*webInicial = (WebView)findViewById(R.id.web_inicial);
        webInicial.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webInicial.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webInicial.loadUrl("http://www.uespi.br/site");*/
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.inicio) {
            // Handle the camera action
        } else if (id == R.id.matricular_aluno) {

            startActivity(new Intent(CoordenadorCurso.this, CadastroMatricula.class));
            //finish();
        } else if (id == R.id.matricular_aluno) {


        } else if (id == R.id.inicio) {

        } else if (id == R.id.inicio) {


        } else if (id == R.id.sair_sistema) {
            startActivity(new Intent(CoordenadorCurso.this , AcessarSistema.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
