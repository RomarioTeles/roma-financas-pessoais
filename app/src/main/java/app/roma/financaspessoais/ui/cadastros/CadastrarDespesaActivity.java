package app.roma.financaspessoais.ui.cadastros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.datasource.services.PeriodoService;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.ItemDespesa;
import app.roma.financaspessoais.entities.DespesaMes;
import app.roma.financaspessoais.entities.TipoLancamento;

public class CadastrarDespesaActivity extends AppCompatActivity{

    private EditText editTextDescricao, editTextValor;

    private TextView textviewCategoriaSelecionada, textviewSubcategoriaSelecionada;

    private CheckBox checkboxPago, checkboxRecorrente;

    private ExtendedFloatingActionButton fabAdicionar;

    private ItemDespesa itemDespesa = new ItemDespesa();

    private String[] receitasArray;

    private List<DespesaMes> despesaMesList;

    private List<Categoria> subcategoriasList;

    private String[] subcategoriaArray;

    private PeriodoService periodoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_lancamentos);

        periodoService = new PeriodoService(this);

        editTextDescricao = findViewById(R.id.editTextDescricao);

        editTextValor = findViewById(R.id.editTextValor);

        textviewCategoriaSelecionada = findViewById(R.id.textviewCategoriaSelecionada);

        textviewSubcategoriaSelecionada = findViewById(R.id.textviewSubcategoriaSelecionada);

        checkboxPago = findViewById(R.id.checkboxPago);

        checkboxRecorrente = findViewById(R.id.checkboxRecorrente);

        fabAdicionar = findViewById(R.id.fabAdicionar);

        LinearLayout linearlayoutCadDespesa = findViewById(R.id.linearlayoutCadDespesa);
        linearlayoutCadDespesa.setVisibility(View.VISIBLE);

        addListeners();

        new CadastrarDespesaActivity.LoadReceitasTask().execute();

    }

    private void addListeners(){

        checkboxPago.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemDespesa.setPago(isChecked);
        });

        checkboxRecorrente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemDespesa.setRecorrente(isChecked);
        });

        fabAdicionar.setOnClickListener(v -> {
            String descricao = editTextDescricao.getText().toString();
            String valor = editTextValor.getText().toString();

            if(!descricao.isEmpty() && !valor.isEmpty()){
                BigDecimal bdValor = new BigDecimal(valor);
                itemDespesa.setValor(bdValor);
                itemDespesa.setDescricao(descricao);
                new CadastrarDespesaActivity.InsertItemDespesaTask().execute();
            }
        });

        textviewCategoriaSelecionada.setOnClickListener(v -> {
            showDespesaMesDialog();
        });

        textviewSubcategoriaSelecionada.setOnClickListener(v -> {
            showSubcategoriaDialog();
        });

        AppDataBase.getAppDataBase(CadastrarDespesaActivity.this).categoriaDAO().getTodasSubcategorias(TipoLancamento.DESPESA)
        .observe(CadastrarDespesaActivity.this, categorias -> {
            subcategoriasList = categorias;
            subcategoriaArray = categorias.stream().map(item -> item.getNome()).collect(Collectors.toList()).toArray(new String[]{});
        });

    }

    private void showDespesaMesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Selecione")
                .setItems(receitasArray, (dialog, which) -> {
                    itemDespesa.setDespesaMes(despesaMesList.get(which).getId());
                    textviewCategoriaSelecionada.setText(despesaMesList.get(which).getCategoriaNome());
                    dialog.dismiss();
                });
        builder.create().show();
    }

    private void showSubcategoriaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Selecione")
                .setItems(subcategoriaArray, (dialog, which) -> {
                    itemDespesa.setSubcategoria(subcategoriasList.get(which).getId());
                    itemDespesa.setCor(subcategoriasList.get(which).getCor());
                    textviewSubcategoriaSelecionada.setText(subcategoriasList.get(which).getNome());
                    dialog.dismiss();
                });
        builder.create().show();
    }

    class LoadReceitasTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            despesaMesList = AppDataBase.getAppDataBase(CadastrarDespesaActivity.this).despesaMesDAO().findByPeriodo(periodoService.getPeriodoSelecionado());
            receitasArray = despesaMesList.stream().map(item -> item.getCategoriaNome()).collect(Collectors.toList()).toArray(new String[]{});

            return null;
        }
    }

    class InsertItemDespesaTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            AppDataBase.getAppDataBase(CadastrarDespesaActivity.this).itemDespesaDAO().insert(itemDespesa);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CadastrarDespesaActivity.this.onBackPressed();
        }
    }
}
