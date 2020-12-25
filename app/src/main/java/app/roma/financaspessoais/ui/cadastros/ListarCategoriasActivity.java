package app.roma.financaspessoais.ui.cadastros;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.EntidadeRemovivel;
import app.roma.financaspessoais.entities.Listavel;
import app.roma.financaspessoais.entities.TipoLancamento;
import app.roma.financaspessoais.ui.common.ListaSimplesAdapter;

public class ListarCategoriasActivity extends AppCompatActivity implements IEntidadeListavelActions {

    private ExtendedFloatingActionButton fabAdicionar;

    private ListView listViewCategorias;

    private ListaSimplesAdapter adapter;

    private List<Listavel> items = new ArrayList<>();

    private TipoLancamento tipoLancamento;

    private boolean isSubcategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categorias);

        Bundle extras = getIntent().getExtras();

        if(extras == null){
            finish();
        }

        fabAdicionar = findViewById(R.id.fabAdicionar);

        listViewCategorias = findViewById(R.id.listViewCategorias);
        adapter = new ListaSimplesAdapter(this,this, items);
        listViewCategorias.setAdapter(adapter);

        tipoLancamento = (TipoLancamento) extras.get("tipoLancamento");
        isSubcategoria = extras.getBoolean("isSubcategoria", false);

        if(isSubcategoria){
            getSupportActionBar().setTitle("Subcategorias");
        }else{
            if(TipoLancamento.RECEITA.equals(tipoLancamento)){
                getSupportActionBar().setTitle("Tipos de Receitas");
            }else{
                getSupportActionBar().setTitle("Tipos de Despesas");
            }
        }

        fabAdicionar.setOnClickListener(v -> {

            Intent intent = new Intent(this, CadastroCategoriaActivity.class);
            intent.putExtra("tipoLancamento", tipoLancamento);
            intent.putExtra("isSubcategoria", isSubcategoria);
            startActivity(intent);

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isSubcategoria){
            AppDataBase.getAppDataBase(this).categoriaDAO().getTodasSubcategorias(tipoLancamento).observe(this, this::atualizaLista);
        }else {
            AppDataBase.getAppDataBase(this).categoriaDAO().getTodasCategorias(tipoLancamento).observe(this, this::atualizaLista);
        }

    }

    private void atualizaLista(List<? extends  Listavel> categorias) {
        adapter.clear();
        adapter.addAll(categorias);
    }

    public void onRemoverCallback(Listavel entidade){
        new DeleteTask().execute((Categoria) entidade);
    }

    class DeleteTask extends AsyncTask<Categoria, Void, Void>{

        @Override
        protected Void doInBackground(Categoria... entities) {

            Categoria categoria = entities[0];
            categoria.setFlagRemocao(true);
            AppDataBase.getAppDataBase(ListarCategoriasActivity.this).categoriaDAO().save(categoria);

            return null;
        }
    }
}