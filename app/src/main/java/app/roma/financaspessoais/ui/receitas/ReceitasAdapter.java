package app.roma.financaspessoais.ui.receitas;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;
import app.roma.financaspessoais.entities.rel.ReceitaMesComItems;

public class ReceitasAdapter extends BaseExpandableListAdapter {

    private Map<ReceitaMes, Set<ItemReceita>> mapItems = new HashMap<>();

    private List<ReceitaMes> expandableListTitle = new ArrayList<>();

    private Context context;

    public ReceitasAdapter(Context context, List<ReceitaMesComItems> items) {
        super();
        this.context = context;
        processaLista(items);
    }

    private void processaLista(List<ReceitaMesComItems> items){
        if(items != null && items.size() > 0){
            items.forEach(item -> {
                Set<ItemReceita> itemReceitas = item.getItens().stream().filter(ir -> !ir.isFlagRemocao() ).collect(Collectors.toSet());
                mapItems.put(item.getReceitaMes(), itemReceitas);
            });
            expandableListTitle.addAll(mapItems.keySet());
        }
    }

    @Override
    public int getGroupCount() {
        return this.mapItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mapItems.get(expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return new ArrayList(this.mapItems.get(expandableListTitle.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ( (ReceitaMes) getGroup(groupPosition)).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((ItemReceita) getChild(groupPosition, childPosition)).getId();
    }

    public void removeChild(int groupPosition, int childPosition){
        this.mapItems.get(expandableListTitle.get(groupPosition)).remove(childPosition);
        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ReceitaMes receitaMes = (ReceitaMes) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(receitaMes.getCategoriaNome());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemReceita itemReceita = (ItemReceita) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_listview_item_lancamento, null);
        }

        CheckBox checkboxPago = convertView.findViewById(R.id.checkboxPago);
        TextView textviewDescricao = convertView.findViewById(R.id.textviewDescricao);
        TextView textviewValor = convertView.findViewById(R.id.textviewValor);
        EditText editTextValor = convertView.findViewById(R.id.editTextValor);
        ImageButton buttonEditar = convertView.findViewById(R.id.buttonEditar);
        ImageButton buttonConfirmar = convertView.findViewById(R.id.buttonConfirmar);
        ImageButton buttonDelete = convertView.findViewById(R.id.buttonDelete);

        checkboxPago.setOnCheckedChangeListener(null);

        textviewDescricao.setText(itemReceita.getDescricao());
        textviewValor.setText("R$ "+itemReceita.getValor().toString());
        editTextValor.setText(itemReceita.getValor().toString());
        checkboxPago.setChecked(itemReceita.isPago());

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        editTextValor.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                BigDecimal valor = new BigDecimal(editTextValor.getText().toString());
                itemReceita.setValor(valor);
                imm.hideSoftInputFromWindow(editTextValor.getWindowToken(), 0);
                new UpdateItemTask().execute(itemReceita);
                return true;
            }
            return false;
        });

        if(itemReceita.isEditarValor()){
            editTextValor.setVisibility(View.VISIBLE);
            buttonEditar.setVisibility(View.GONE);

            textviewValor.setVisibility(View.GONE);
            buttonConfirmar.setVisibility(View.VISIBLE);

            editTextValor.requestFocus();
            editTextValor.setSelection(editTextValor.length());

            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        }else{
            editTextValor.setVisibility(View.GONE);
            buttonEditar.setVisibility(View.VISIBLE);

            textviewValor.setVisibility(View.VISIBLE);
            buttonConfirmar.setVisibility(View.GONE);

        }

        checkboxPago.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemReceita.setPago(isChecked);
            new UpdateItemTask().execute(itemReceita);
        });

        buttonEditar.setOnClickListener(v -> {
            itemReceita.setEditarValor(true);
            notifyDataSetChanged();
        });

        buttonConfirmar.setOnClickListener(v -> {
            BigDecimal valor = new BigDecimal(editTextValor.getText().toString());
            itemReceita.setValor(valor);
            imm.hideSoftInputFromWindow(editTextValor.getWindowToken(), 0);
            new UpdateItemTask().execute(itemReceita);
        });

        buttonDelete.setOnClickListener(v -> {
            itemReceita.setFlagRemocao(true);
            new UpdateItemTask().execute(itemReceita);
            removeChild(groupPosition, childPosition);
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    class UpdateItemTask extends AsyncTask<ItemReceita, Void, Void> {

        @Override
        protected Void doInBackground(ItemReceita... itemReceitas) {

            AppDataBase.getAppDataBase(context).itemReceitaDAO().save(itemReceitas[0]);

            return null;
        }
    }
}
