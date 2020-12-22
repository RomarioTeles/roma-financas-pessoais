package app.roma.financaspessoais.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.entities.Listavel;

public class ListaSimplesAdapter extends ArrayAdapter<Listavel> {

    private List<? extends Listavel> items;

    public ListaSimplesAdapter(@NonNull Context context, List<? extends Listavel> items) {
        super(context, R.layout.item_lista_simples);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_simples, parent, false);
        }

        Listavel item = getItem(position);
        TextView textView = convertView.findViewById(R.id.textviewConteudo);
        textView.setText(item.getConteudo());

        return convertView;
    }
}
