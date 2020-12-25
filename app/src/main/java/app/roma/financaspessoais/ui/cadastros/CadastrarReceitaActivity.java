package app.roma.financaspessoais.ui.cadastros;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.datasource.services.PeriodoService;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CadastrarReceitaActivity extends AppCompatActivity {

    private EditText editTextDescricao, editTextValor;

    private TextView textviewReceitaSelecionada;

    private CheckBox checkboxPago, checkboxRecorrente;

    private ExtendedFloatingActionButton fabAdicionar;

    private ItemReceita itemReceita = new ItemReceita();

    private String[] receitasArray;

    private List<ReceitaMes> receitaMesList;

    private PeriodoService periodoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_lancamentos);

        periodoService = new PeriodoService(this);

        editTextDescricao = findViewById(R.id.editTextDescricao);

        editTextValor = findViewById(R.id.editTextValor);

        textviewReceitaSelecionada = findViewById(R.id.textviewCategoriaSelecionada);

        checkboxPago = findViewById(R.id.checkboxPago);

        checkboxRecorrente = findViewById(R.id.checkboxRecorrente);

        fabAdicionar = findViewById(R.id.fabAdicionar);

        addListeners();

        new LoadReceitasTask().execute();

    }

    private void addListeners(){

        checkboxPago.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemReceita.setPago(isChecked);
        });

        checkboxRecorrente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemReceita.setRecorrente(isChecked);
        });

        fabAdicionar.setOnClickListener(v -> {
            String descricao = editTextDescricao.getText().toString();
            String valor = editTextValor.getText().toString();

            if(!descricao.isEmpty() && !valor.isEmpty()){
                BigDecimal bdValor = new BigDecimal(valor);
                itemReceita.setValor(bdValor);
                itemReceita.setDescricao(descricao);
                new InsertItemReceitaTask().execute();
            }
        });

        textviewReceitaSelecionada.setOnClickListener(v -> {
            showReceitaMesDialog();
        });

    }

    private void showReceitaMesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Selecione")
                .setItems(receitasArray, (dialog, which) -> {
                    itemReceita.setReceitaMes(receitaMesList.get(which).getId());
                    textviewReceitaSelecionada.setText(receitaMesList.get(which).getCategoriaNome());
                    dialog.dismiss();
                });
        builder.create().show();
    }

    class LoadReceitasTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            receitaMesList = AppDataBase.getAppDataBase(CadastrarReceitaActivity.this).receitaMesDAO().findByPeriodo(periodoService.getPeriodoSelecionado());
            receitasArray = receitaMesList.stream().map(item -> item.getCategoriaNome()).collect(Collectors.toList()).toArray(new String[]{});

            return null;
        }
    }

    class InsertItemReceitaTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            AppDataBase.getAppDataBase(CadastrarReceitaActivity.this).itemReceitaDAO().insert(itemReceita);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CadastrarReceitaActivity.this.onBackPressed();
        }
    }
}
