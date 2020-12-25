package app.roma.financaspessoais.datasource.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.datasource.daos.CategoriaDAO;
import app.roma.financaspessoais.datasource.daos.DespesaMesDAO;
import app.roma.financaspessoais.datasource.daos.ReceitaMesDAO;
import app.roma.financaspessoais.entities.DespesaMes;
import app.roma.financaspessoais.entities.ItemDespesa;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;
import app.roma.financaspessoais.entities.rel.ItemDespesaRecorrente;
import app.roma.financaspessoais.entities.rel.ItemReceitaRecorrente;

public class PeriodoService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.getDefault());

    private static final String CHAVE_PERIODO = "periodoSelecionado";

    private final Context context;

    public PeriodoService(Context context) {
        this.context = context;
    }

    public CompletableFuture<Set<String>> getTodosPeriodos(){
        CompletableFuture<Set<String>> result = new CompletableFuture<>();

        result.supplyAsync(() -> {
            String periodoAtual = getPeriodoAtual();
            Set<String> periodos = new LinkedHashSet<>();
            periodos.add(periodoAtual);
            List<String> periodosDespesas = AppDataBase.getAppDataBase(context).despesaMesDAO().getTodosPeriodos();
            List<String> periodosReceitas = AppDataBase.getAppDataBase(context).receitaMesDAO().getTodosPeriodos();

            periodos.addAll(periodosDespesas == null ? new ArrayList<>(): periodosDespesas);
            periodos.addAll(periodosReceitas == null ? new ArrayList<>(): periodosReceitas);

            result.complete(periodos);
            return null;
        });
        return result;
    }

    public String getPeriodoSelecionado(){
        SharedPreferences preferences = context.getSharedPreferences("periodo_configs", Context.MODE_PRIVATE);
        String periodoSelecionado = preferences.getString("periodoSelecionado", "");
        if(periodoSelecionado.isEmpty()){
            return getPeriodoAtual();
        }
        return periodoSelecionado;
    }

    public String getPeriodoAtual(){
        SharedPreferences preferences = context.getSharedPreferences("periodo_configs", Context.MODE_PRIVATE);
        LocalDate periodoAtual = LocalDate.now();
        String periodoSelecionado = preferences.getString(CHAVE_PERIODO, "");
        if(periodoSelecionado.isEmpty()){
            periodoSelecionado = periodoAtual.format(dateTimeFormatter);
            preferences.edit().putString(CHAVE_PERIODO, periodoSelecionado).apply();
            new CreateDadosDoMesAtual().execute(periodoSelecionado);
        }else {
            LocalDate periodoSelecionadoAsDate = dateTimeFormatter.parse(periodoSelecionado, YearMonth::from).atDay(periodoAtual.getDayOfMonth());
            if (periodoSelecionadoAsDate.isBefore(periodoAtual)) {
                periodoSelecionado = periodoAtual.format(dateTimeFormatter);
                preferences.edit().putString(CHAVE_PERIODO, periodoSelecionado).apply();
            }
        }

        return periodoSelecionado;
    }

    public void save(String periodo){
        if(isPeriodoValido(periodo)){
            SharedPreferences preferences = context.getSharedPreferences("periodo_configs", Context.MODE_PRIVATE);
            preferences.edit().putString(CHAVE_PERIODO, periodo).apply();
        }else{
            Toast.makeText(context, "Erro ao salvar o periodo.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPeriodoValido(String periodo){
        try{
            dateTimeFormatter.parse(periodo, YearMonth::from).atDay(LocalDate.now().getDayOfMonth());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void createReceitasDoPeriodo(String periodo){
        Set<ReceitaMes> receitasIds = new HashSet<>();
        CategoriaDAO categoriaDAO = AppDataBase.getAppDataBase(context).categoriaDAO();
        ReceitaMesDAO receitaMesDAO = AppDataBase.getAppDataBase(context).receitaMesDAO();
        Integer count = receitaMesDAO.countByPeriodo(periodo);
        if(count == 0){
            List<ItemReceitaRecorrente> receitas = AppDataBase.getAppDataBase(context).itemReceitaDAO().getTodosItemReceitasRecorrentes();
            receitas.stream().forEach(categoria -> {
                ReceitaMes receitaMes = new ReceitaMes(null, periodo, categoria.getCategoriaId(), categoria.getCategoriaNome());
                Long id = null;
                if(receitasIds.contains(receitaMes)){
                    ReceitaMes receitaMesFound = receitasIds.stream().filter(item -> item.getCategoriaId().equals(receitaMes.getCategoriaId())).collect(Collectors.toList()).get(0);
                    id = receitaMesFound.getId();
                }else{
                    id = receitaMesDAO.insert(receitaMes);
                    receitaMes.setId(id);
                    receitasIds.add(receitaMes);
                }
                ItemReceita itemReceita = new ItemReceita(null, categoria.getDescricao(), LocalDateTime.now(), BigDecimal.ZERO, false, true, id );
                AppDataBase.getAppDataBase(context).itemReceitaDAO().insert(itemReceita);
            });
        }
    }

    private void createDespesasDoPeriodo(String periodo) {
        Set<DespesaMes> despesasIds = new HashSet<>();
        DespesaMesDAO despesaMesDAO = AppDataBase.getAppDataBase(context).despesaMesDAO();
        Integer count = despesaMesDAO.countByPeriodo(periodo);
        if(count == 0){
            List<ItemDespesaRecorrente> despesas = AppDataBase.getAppDataBase(context).itemDespesaDAO().getTodosItemDespesasRecorrentes();
            despesas.stream().forEach(categoria -> {
                DespesaMes despesaMes = new DespesaMes(null, periodo, categoria.getCategoriaNome(), categoria.getCategoriaId());
                Long id = null;
                if(despesasIds.contains(despesaMes)){
                    DespesaMes despesaMesFound = despesasIds.stream().filter(item -> item.getCategoriaId().equals(despesaMes.getCategoriaId())).collect(Collectors.toList()).get(0);
                    id = despesaMesFound.getId();
                }else{
                    id = despesaMesDAO.insert(despesaMes);
                    despesaMes.setId(id);
                    despesasIds.add(despesaMes);
                }
                ItemDespesa itemDespesa = new ItemDespesa(null, categoria.getDescricao(), LocalDateTime.now(), BigDecimal.ZERO, false, true, id, categoria.getSubcategoria());
                AppDataBase.getAppDataBase(context).itemDespesaDAO().insert(itemDespesa);
            });
        }
    }

    class CreateDadosDoMesAtual extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String periodo = strings[0];
            createDespesasDoPeriodo(periodo);
            createReceitasDoPeriodo(periodo);
            return null;
        }
    }
}
