package app.roma.financaspessoais.ui.receitas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.entities.rel.ReceitaMesComItems;
import io.reactivex.Flowable;

public class ReceitasFragment extends Fragment {

    private BigDecimal totalReceitas = BigDecimal.ZERO;
    private BigDecimal totalReceitasPagas = BigDecimal.ZERO;

    private TextView textviewTotalValor, textviewTotalValorPago;

    private ProgressBar progressBarTotal;

    private ExpandableListView listViewReceitas;

    private final String periodo = "12/2020";

    private ReceitasAdapter receitasAdapter;

    private List<ReceitaMesComItems> items = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receitas, container, false);
        textviewTotalValor = root.findViewById(R.id.textviewTotalValor);
        textviewTotalValorPago = root.findViewById(R.id.textviewTotalValorPago);
        progressBarTotal = root.findViewById(R.id.progressBarTotal);
        progressBarTotal.setMax(0);
        progressBarTotal.setMin(0);
        progressBarTotal.setProgress(0);

        listViewReceitas = root.findViewById(R.id.listViewReceitas);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTodasComItemReceita(periodo).observe(getViewLifecycleOwner(), event -> {
                items = new ArrayList(event);
                atualizaProgressBar();
            });

            AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTotalReceitas(periodo).observe(getViewLifecycleOwner(), event -> {
                totalReceitas = totalReceitas.add(new BigDecimal(event)).divide(new BigDecimal(100));
                atualizaProgressBar();
            });

            AppDataBase.getAppDataBase(getContext()).receitaMesDAO().getTotalReceitasPagas(periodo).observe(getViewLifecycleOwner(), event -> {
                totalReceitasPagas = totalReceitasPagas.add(new BigDecimal(event)).divide(new BigDecimal(100));
                atualizaProgressBar();
            });

    }

    private void atualizaProgressBar(){
        progressBarTotal.setMin(0);
        progressBarTotal.setMax(totalReceitas.intValue());
        progressBarTotal.setProgress(totalReceitasPagas.intValue());
        textviewTotalValor.setText("R$ "+totalReceitas.toString());
        textviewTotalValorPago.setText("R$ "+totalReceitasPagas.toString());
        receitasAdapter = new ReceitasAdapter(this.getContext(), items);
        listViewReceitas.setAdapter(receitasAdapter);
    }
}