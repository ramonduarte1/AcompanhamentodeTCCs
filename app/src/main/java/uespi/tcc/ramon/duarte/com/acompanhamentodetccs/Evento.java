package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

/**
 * Created by ramon on 23/07/17.
 */

public class Evento {
    int id;
    String titulo;
    String data;
    String periodo;
    int curso;

    public Evento(int id, String titulo, String data, int curso) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.curso = curso;
    }
    public Evento(int id, String titulo, String data, String periodo, int curso) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.periodo = periodo;
        this.curso = curso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }
}
