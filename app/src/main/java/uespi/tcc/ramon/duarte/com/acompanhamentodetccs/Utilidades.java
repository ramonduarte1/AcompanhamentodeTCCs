package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

/**
 * Created by ramon on 14/08/17.
 */

public class Utilidades {

    public static String substituiEspacos(String s){
        String nova = "";
        for(int i = 0;i<s.length();i++){
            if(s.charAt(i) == ' '){
                nova += '%';
            }else{
                nova += s.charAt(i);
            }
        }
        return nova;
    }
}