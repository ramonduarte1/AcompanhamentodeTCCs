package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

/**
 * Created by ramon on 25/06/17.
 */

public class Aluno extends Usuario {


    public Aluno(int matricula, String nome, String email, int funcao, String senha, int codCurso, int logado) {
        super(matricula, nome, email, funcao, senha, codCurso, logado);
    }

    public Aluno(int matricula) {
        this.matricula = matricula;
    }

    public String substituiEspacos(String s){
       String nova = "";
       for(int i = 0;i<s.length();i++){
           if(" ".equals(s.charAt(i))){
               nova += "#";

           }else{
               nova += s.charAt(i);
           }
       }
       return nova;
   }
}