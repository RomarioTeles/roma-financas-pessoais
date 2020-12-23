package app.roma.financaspessoais.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.services.PeriodoService;

public class HomeFragment extends Fragment {

    private PeriodoService periodoService;

    String[] periodosArray = null;

    TextView textViewPeriodo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        periodoService = new PeriodoService(this.getContext());
        textViewPeriodo = root.findViewById(R.id.textviewPeriodo);
        textViewPeriodo.setText(periodoService.getPeriodoSelecionado());
        textViewPeriodo.setOnClickListener(v -> {
            showDialog();
        });

        loadPeriodos();

        return root;
    }

    private void loadPeriodos(){

        periodoService.getTodosPeriodos().handle((periodos, throwable) -> {
            periodosArray = periodos.toArray(new String[]{});
            return null;
        });

    }

    public void showDialog() {
        if(periodosArray != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("Selecione um periodo")
                    .setItems(periodosArray, (dialog, which) -> {
                        textViewPeriodo.setText(periodosArray[which]);
                        periodoService.save(periodosArray[which]);
                        dialog.dismiss();
                    });
            builder.create().show();
        }
    }


}