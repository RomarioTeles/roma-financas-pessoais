package app.roma.financaspessoais.datasource.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import app.roma.financaspessoais.datasource.AppDataBase;
import io.reactivex.Completable;

public class PeriodoService {

    private static final String CHAVE_PERIODO = "periodoSelecionado";

    private final Context context;

    public PeriodoService(Context context) {
        this.context = context;
    }

    public CompletableFuture<Set<String>> getTodosPeriodos(){
        CompletableFuture<Set<String>> result = new CompletableFuture<>();

        result.supplyAsync(() -> {
            Set<String> periodos = new LinkedHashSet<>();
            List<String> periodosDespesas = AppDataBase.getAppDataBase(context).despesaMesDAO().getTodosPeriodos();
            List<String> periodosReceitas = AppDataBase.getAppDataBase(context).receitaMesDAO().getTodosPeriodos();

            periodos.addAll(periodosDespesas == null ? new ArrayList<>(): periodosDespesas);
            periodos.addAll(periodosReceitas == null ? new ArrayList<>(): periodosReceitas);

            if(periodos.isEmpty()){
                periodos.add(getPeriodoAtual());
            }

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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.getDefault());
        SharedPreferences preferences = context.getSharedPreferences("periodo_configs", Context.MODE_PRIVATE);
        LocalDate periodoAtual = LocalDate.now();
        String periodoSelecionado = preferences.getString(CHAVE_PERIODO, "");
        if(periodoSelecionado.isEmpty()){
            periodoSelecionado = periodoAtual.format(dateTimeFormatter);
            preferences.edit().putString(CHAVE_PERIODO, periodoSelecionado).apply();
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.getDefault());
            dateTimeFormatter.parse(periodo, YearMonth::from).atDay(LocalDate.now().getDayOfMonth());
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
