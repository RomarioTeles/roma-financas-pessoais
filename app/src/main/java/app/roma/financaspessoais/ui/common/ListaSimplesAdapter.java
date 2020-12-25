package app.roma.financaspessoais.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.entities.Listavel;
import app.roma.financaspessoais.ui.cadastros.IEntidadeListavelActions;

public class ListaSimplesAdapter extends ArrayAdapter<Listavel> {

    private List<? extends Listavel> items;

    private IEntidadeListavelActions actions;

    public ListaSimplesAdapter(@NonNull Context context, IEntidadeListavelActions actions, List<? extends Listavel> items) {
        super(context, R.layout.item_lista_simples);
        this.items = items;
        this.actions = actions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_simples, parent, false);
        }

        Listavel item = getItem(position);
        TextView textView = convertView.findViewById(R.id.textviewConteudo);
        ImageButton buttonDelete  = convertView.findViewById(R.id.buttonDelete);

        textView.setText(item.getConteudo());

        buttonDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Confirmação")
                    .setMessage("Deseja prosseguir com a remoção?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        actions.onRemoverCallback(item);
                        items.remove(item);
                    })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                });
            builder.create().show();
        });

        return convertView;
    }
}
