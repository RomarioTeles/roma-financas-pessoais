package app.roma.financaspessoais.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.datasource.services.PeriodoService;

public class HomeFragment extends Fragment {

    private PeriodoService periodoService;

    private String[] periodosArray = null;

    private TextView textViewPeriodo;

    private String periodo;

    private BigDecimal totalDespesas = BigDecimal.ZERO;
    private BigDecimal totalReceitas = BigDecimal.ZERO;
    private BigDecimal totalDespesasPagas = BigDecimal.ZERO;
    private BigDecimal totalDespesasFaltaPagar = BigDecimal.ZERO;
    private BigDecimal totalReceitasPagas = BigDecimal.ZERO;


    private TextView textviewTotalReceitas, textviewTotalReceitasRecebidas,
            textviewTotalDespesas, textviewTotalDespesasPagas,
            textviewTotalDespesasFaltaPagar, textviewTotal, textviewTotalGeral;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        periodoService = new PeriodoService(this.getContext());
        periodo = periodoService.getPeriodoSelecionado();

        textviewTotalReceitas = root.findViewById(R.id.textviewTotalReceitas);
        textviewTotalReceitasRecebidas = root.findViewById(R.id.textviewTotalReceitasRecebidas);
        textviewTotalDespesas = root.findViewById(R.id.textviewTotalDespesas);
        textviewTotal = root.findViewById(R.id.textviewTotal);
        textviewTotalDespesasPagas = root.findViewById(R.id.textviewTotalDespesasPagas);
        textviewTotalDespesasFaltaPagar = root.findViewById(R.id.textviewTotalDespesasFaltaPagar);
        textviewTotalGeral = root.findViewById(R.id.textviewTotalGeral);

        textViewPeriodo = root.findViewById(R.id.textviewPeriodo);
        textViewPeriodo.setText(periodo);
        textViewPeriodo.setOnClickListener(v -> {
            showDialog();
        });

        loadPeriodos();
        loadTotais();

        return root;
    }

    private void loadPeriodos(){

        periodoService.getTodosPeriodos().handle((periodos, throwable) -> {
            periodosArray = periodos.toArray(new String[]{});
            return null;
        });

    }

    private void loadTotais(){
        AppDataBase.getAppDataBase(getContext()).despesaMesDAO().getTotalDespesas(periodo).observe(getViewLifecycleOwner(), event -> {
            totalDespesas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
            atualizaTela();
        });

        AppDataBase.getAppDataBase(getContext()).despesaMesDAO().getTotalDespesasPagas(periodo).observe(getViewLifecycleOwner(), event -> {
            totalDespesasPagas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
            atualizaTela();
        });

        AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTotalReceitas(periodo).observe(getViewLifecycleOwner(), event -> {
            totalReceitas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
            atualizaTela();
        });

        AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTotalReceitasPagas(periodo).observe(getViewLifecycleOwner(), event -> {
            totalReceitasPagas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
            atualizaTela();
        });
    }

    private void atualizaTela() {
        textviewTotalDespesas.setText("R$ "+totalDespesas.toString());
        textviewTotalReceitas.setText("R$ "+totalReceitas.toString());
        textviewTotalDespesasPagas.setText("R$ "+totalDespesasPagas.toString());
        totalDespesasFaltaPagar = totalDespesas.subtract(totalDespesasPagas);
        textviewTotalDespesasFaltaPagar.setText("R$ "+totalDespesasFaltaPagar.toString());
        BigDecimal saldo = totalReceitasPagas.subtract(totalDespesasPagas);
        BigDecimal totalGeral = totalReceitas.subtract(totalDespesas);
        textviewTotal.setText("R$ "+saldo.toString());
        textviewTotalGeral.setText("R$ "+totalGeral.toString());
        textviewTotalReceitasRecebidas.setText("R$ "+totalReceitasPagas.toString());

        if(saldo.min(BigDecimal.ZERO).equals(saldo)){
            textviewTotal.setTextColor(getContext().getColor(R.color.negative));
        }else{
            textviewTotal.setTextColor(getContext().getColor(android.R.color.darker_gray));
        }

    }

    public void showDialog() {
        if(periodosArray != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Selecione um periodo")
                    .setItems(periodosArray, (dialog, which) -> {
                        textViewPeriodo.setText(periodosArray[which]);
                        periodoService.save(periodosArray[which]);
                        periodo = periodosArray[which];
                        loadTotais();
                        dialog.dismiss();
                    });
            builder.create().show();
        }
    }


}