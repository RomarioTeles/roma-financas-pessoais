package app.roma.financaspessoais.ui.despesas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import app.roma.financaspessoais.entities.rel.DespesaMesComItems;
import app.roma.financaspessoais.ui.cadastros.CadastrarDespesaActivity;
import app.roma.financaspessoais.ui.cadastros.CadastrarReceitaActivity;

public class DespesasFragment extends Fragment {

    private BigDecimal totalDespesas = BigDecimal.ZERO;
    private BigDecimal totalDespesasPagas = BigDecimal.ZERO;

    private TextView textviewTotalValor, textviewTotalValorPago, textviewTitulo;

    private ProgressBar progressBarTotal;

    private ExpandableListView listViewDespesas;

    private String periodo;

    private PeriodoService periodoService;

    private DespesasAdapter adapter;

    private List<DespesaMesComItems> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_despesas, container, false);
        setHasOptionsMenu(true);
        periodoService = new PeriodoService(getContext());

        textviewTotalValor = root.findViewById(R.id.textviewTotalValor);
        textviewTotalValorPago = root.findViewById(R.id.textviewTotalValorPago);
        textviewTitulo = root.findViewById(R.id.textviewTitulo);
        progressBarTotal = root.findViewById(R.id.progressBarTotal);
        progressBarTotal.setMax(0);
        progressBarTotal.setMin(0);
        progressBarTotal.setProgress(0);

        listViewDespesas = root.findViewById(R.id.listViewLancamentos);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        periodo = periodoService.getPeriodoSelecionado();
        textviewTitulo.setText("Despesas de " + periodo);

        AppDataBase.getAppDataBase(getContext()).despesaMesDAO().getTodasComItemDespesa(periodo).observe(getViewLifecycleOwner(), event -> {
            Log.i("List Observer", "Chamado");
            items = new ArrayList(event);
            atualizaProgressBar();
        });

        AppDataBase.getAppDataBase(getContext()).despesaMesDAO().getTotalDespesas(periodo).observe(getViewLifecycleOwner(), event -> {
            totalDespesas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
            atualizaProgressBar();
        });

        AppDataBase.getAppDataBase(getContext()).despesaMesDAO().getTotalDespesasPagas(periodo).observe(getViewLifecycleOwner(), event -> {
            totalDespesasPagas = new BigDecimal(event).setScale(2, RoundingMode.HALF_EVEN);
            atualizaProgressBar();
        });

    }

    private void atualizaProgressBar() {
        progressBarTotal.setMin(0);
        progressBarTotal.setMax(totalDespesas.intValue());
        progressBarTotal.setProgress(totalDespesasPagas.intValue());
        textviewTotalValor.setText("R$ " + totalDespesas.toString());
        textviewTotalValorPago.setText("R$ " + totalDespesasPagas.toString());
        adapter = new DespesasAdapter(this.getContext(), items);
        listViewDespesas.setAdapter(adapter);
        for (int i = 0; i < items.size(); i++) {
            listViewDespesas.expandGroup(i);
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
                Intent intent = new Intent(getContext(), CadastrarDespesaActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}