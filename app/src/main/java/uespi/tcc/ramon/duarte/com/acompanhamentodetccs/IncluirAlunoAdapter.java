package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ramon on 09/08/17.
 */

public class IncluirAlunoAdapter extends BaseAdapter {

        Context context;
        private List<Aluno> lista;
        private LayoutInflater inflater;

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

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.aluno_participante, null);
                itemSuporte = new ItemSuporte();
                itemSuporte.matricula = (TextView) convertView.findViewById(R.id.matricula_participante);
                itemSuporte.nome = (TextView) convertView.findViewById(R.id.nome_participante);
                itemSuporte.email = (TextView) convertView.findViewById(R.id.email_participante);
                itemSuporte.matricular = (Button) convertView.findViewById(R.id.bt_incluir_participante);

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
                    Toast.makeText(context,"teste",Toast.LENGTH_LONG).show();
                }
            });

            return convertView;
        }

        private class ItemSuporte{
            TextView  matricula,
                      nome,
                      email;
            Button matricular;
        }
   }


