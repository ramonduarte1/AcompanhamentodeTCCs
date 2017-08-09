package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

/**
 * Created by ramon on 25/06/17.
 */

public class Aluno extends Usuario {
    public Aluno(String nome, String email, String periodo, int matricula, int codCurso) {
        super(nome, email, periodo, matricula, codCurso);
    }

    public Aluno(int matricula, String nome, String email, int funcao, String senha, int codCurso, int logado) {
        super(matricula, nome, email, funcao, senha, codCurso, logado);
    }

    public Aluno() {
    }

    public Aluno(int matricula) {
        this.matricula = matricula;
    }


}
