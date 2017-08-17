package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ramon on 25/06/17.
 */

public class Usuario{
    String nome,
           email,
           periodo,
           senha;
    int matricula,
        codCurso,
        funcao,
        logado;

    public Usuario(String nome, String email, String periodo, int matricula, int codCurso,int funcao) {
        this.nome = nome;
        this.email = email;
        this.periodo = periodo;
        this.matricula = matricula;
        this.codCurso = codCurso;
        this.funcao = funcao;
    }

    public Usuario(String nome, String email, String periodo, int matricula, int codCurso) {
        this.nome = nome;
        this.email = email;
        this.periodo = periodo;
        this.matricula = matricula;
        this.codCurso = codCurso;
    }

    public Usuario(int matricula, String nome, String email, int funcao, String senha, int codCurso, int logado) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.funcao = funcao;
        this.senha = senha;
        this.codCurso = codCurso;
        this.logado = logado;
    }
    public Usuario(){}

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getPeriodo() {
        return periodo;
    }
    public int getLogado() {
        return logado;
    }

    public void setLogado(int logado) {
        this.logado = logado;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFuncao() {
        return funcao;
    }

    public void setFuncao(int funcao) {
        this.funcao = funcao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getCodCurso() {
        return codCurso;
    }

    public void setCodCurso(int codCurso) {
        this.codCurso = codCurso;
    }
}

