package app.roma.financaspessoais.ui.receitas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.datasource.services.PeriodoService;
import app.roma.financaspessoais.entities.rel.ReceitaMesComItems;
import app.roma.financaspessoais.ui.cadastros.CadastrarReceitaActivity;

public class ReceitasFragment extends Fragment {

    private BigDecimal totalReceitas = BigDecimal.ZERO;
    private BigDecimal totalReceitasPagas = BigDecimal.ZERO;

    private TextView textviewTotalValor, textviewTotalValorPago, textviewTitulo;

    private ProgressBar progressBarTotal;

    private ExpandableListView listViewReceitas;

    private PeriodoService periodoService;

    private List<ReceitaMesComItems> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_receitas, container, false);

        periodoService = new PeriodoService(this.getContext());

        textviewTotalValor = root.findViewById(R.id.textviewTotalValor);
        textviewTotalValorPago = root.findViewById(R.id.textviewTotalValorPago);
        textviewTitulo = root.findViewById(R.id.textviewTitulo);
        progressBarTotal = root.findViewById(R.id.progressBarTotal);
        progressBarTotal.setMax(0);
        progressBarTotal.setMin(0);
        progressBarTotal.setProgress(0);

        listViewReceitas = root.findViewById(R.id.listViewLancamentos);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            String periodo = periodoService.getPeriodoSelecionado();
            textviewTitulo.setText("Receitas de "+ periodo);

            AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTodasComItemReceita(periodo).observe(getViewLifecycleOwner(), event -> {
                items = new ArrayList(event);
                atualizaProgressBar();
            });

            AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTotalReceitas(periodo).observe(getViewLifecycleOwner(), event -> {
                totalReceitas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
                atualizaProgressBar();
            });

            AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTotalReceitasPagas(periodo).observe(getViewLifecycleOwner(), event -> {
                totalReceitasPagas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
                atualizaProgressBar();
            });

    }

    private void atualizaProgressBar(){
        progressBarTotal.setMin(0);
        progressBarTotal.setMax(totalReceitas.intValue());
        progressBarTotal.setProgress(totalReceitasPagas.intValue());
        textviewTotalValor.setText("R$ "+totalReceitas.toString());
        textviewTotalValorPago.setText("R$ "+totalReceitasPagas.toString());
        ReceitasAdapter receitasAdapter = new ReceitasAdapter(this.getContext(), items);
        listViewReceitas.setAdapter(receitasAdapter);
        for(int i = 0; i < items.size(); i++){
            listViewReceitas.expandGroup(i);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.lancamentos_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_novo:
                Intent intent = new Intent(getContext(), CadastrarReceitaActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}