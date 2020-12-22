package app.roma.financaspessoais.ui.cadastros;

import androidx.appcompat.app.AppCompatActivity;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.TipoLancamento;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class CadastroCategoriaActivity extends AppCompatActivity {

    private Categoria categoria;

    private boolean isEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_categoria);

        Bundle extras = getIntent().getExtras();

        getSupportActionBar().setTitle("Categoria");

        if(extras != null){
            isEdicao = extras.getBoolean("isEdicao", false);
            if(isEdicao) {
                categoria = extras.getParcelable("categoria");
            }else{
                TipoLancamento tipoLancamento = (TipoLancamento) extras.get("tipoLancamento");
                categoria = Categoria.from(tipoLancamento);
                boolean isSubCategoria = extras.getBoolean("isSubcategoria", false);
                categoria.setSubcategoria(isSubCategoria);
                if(isSubCategoria){
                    getSupportActionBar().setTitle("Subcategoria");
                }
            }
        }

        EditText editTextNome = findViewById(R.id.editTextNome);

        Button buttonAdicionar = findViewById(R.id.buttonAdicionar);

        Switch switchAtivar = findViewById(R.id.switchAtivar);

        if(categoria != null){
            editTextNome.setText(categoria.getNome());
            switchAtivar.setChecked(!categoria.isFlagRemocao());
        }

        buttonAdicionar.setOnClickListener(v -> {
            new TaskCategoriaDB().execute();
        });

        switchAtivar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            categoria.setFlagRemocao(!isChecked);
        });
    }

    @SuppressLint("StaticFieldLeak")
    class TaskCategoriaDB extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            if (isEdicao) {
                AppDataBase.getAppDataBase(CadastroCategoriaActivity.this).categoriaDAO().save(categoria);
            } else {
                AppDataBase.getAppDataBase(CadastroCategoriaActivity.this).categoriaDAO().insert(categoria);
            }

            return null;
        }
    }
}