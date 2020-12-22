package app.roma.financaspessoais.ui.categorias;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.entities.TipoLancamento;
import app.roma.financaspessoais.ui.cadastros.CadastroCategoriaActivity;
import app.roma.financaspessoais.ui.cadastros.ListarCategoriasActivity;

public class CategoriasFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categorias, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.listviewCadastros);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        List<String> items = Arrays.asList(
                "Tipos de Receitas",
                "Tipos de Despesas",
                "Subcategorias de Despesas"
        );

        adapter.addAll(items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getContext(), ListarCategoriasActivity.class);
            if(position == 0){
                intent.putExtra("tipoLancamento", TipoLancamento.RECEITA);
            }else if(position == 1){
                intent.putExtra("tipoLancamento", TipoLancamento.DESPESA);
            }else{
                intent.putExtra("tipoLancamento", TipoLancamento.DESPESA);
                intent.putExtra("isSubcategoria", true);
            }
            startActivity(intent);
        });
    }
}