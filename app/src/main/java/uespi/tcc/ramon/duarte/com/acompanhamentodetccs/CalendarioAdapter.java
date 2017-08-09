package uespi.tcc.ramon.duarte.com.acompanhamentodetccs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramon on 24/07/17.
 */

public class CalendarioAdapter extends BaseAdapter {
    Context context;
    private List<Evento> lista;
    private LayoutInflater inflater;

    public CalendarioAdapter(Context context, List<Evento> listaResult) {
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
        final Evento dados = lista.get(position);
        final ItemSuporte itemSuporte;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_evento, null);
            itemSuporte = new ItemSuporte();
            itemSuporte.data = (TextView) convertView.findViewById(R.id.data_evento);
            itemSuporte.descricao = (TextView) convertView.findViewById(R.id.titulo_evento);



            convertView.setTag(itemSuporte);
        } else {
            //se a view já existe pega os itens.
            itemSuporte = (ItemSuporte) convertView.getTag();
        }

        itemSuporte.data.setText("Data: "+dados.getData());
        itemSuporte.descricao.setText("Descrição: "+dados.getTitulo());

        return convertView;
    }

    private class ItemSuporte{
        TextView  data,
                  descricao;
    }

    /*public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = lista.size();
                    results.values = lista;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    ArrayList<Dados> itens_filtrados = new ArrayList<Dados>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < lista.size(); i++) {
                        Dados data = lista.get(i);
                        filtro = filtro.toString().toLowerCase();
                        String condicao = data.getDescricao().toLowerCase();

                        if (condicao.contains(filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            itens_filtrados.add(data);
                        }
                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = itens_filtrados.size();
                    results.values = itens_filtrados;
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                lista = (ArrayList<Dados>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

        };
        return filter;
    }*/
}
