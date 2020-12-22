package app.roma.financaspessoais.ui.despesas;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import app.roma.financaspessoais.R;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.entities.DespesaMes;
import app.roma.financaspessoais.entities.ItemDespesa;
import app.roma.financaspessoais.entities.rel.DespesaMesComItems;

public class DespesasAdapter extends BaseExpandableListAdapter {

    private Map<DespesaMes, Set<ItemDespesa>> mapItems = new HashMap<>();

    private List<DespesaMes> expandableListTitle = new ArrayList<>();

    private Context context;

    public DespesasAdapter(Context context, List<DespesaMesComItems> items) {
        super();
        this.context = context;
        processaLista(items);
    }

    private void processaLista(List<DespesaMesComItems> items){
        if(items != null && items.size() > 0){
            items.forEach(item -> mapItems.put(item.getDespesaMes(), item.getItens()));
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
        return ( (DespesaMes) getGroup(groupPosition)).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((ItemDespesa) getChild(groupPosition, childPosition)).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DespesaMes despesaMes = (DespesaMes) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(despesaMes.getCategoriaNome());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemDespesa itemDespesa = (ItemDespesa) getChild(groupPosition, childPosition);
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

        textviewDescricao.setText(itemDespesa.getDescricao());
        textviewValor.setText("R$ "+itemDespesa.getValor().toString());
        editTextValor.setText(itemDespesa.getValor().toString());
        checkboxPago.setChecked(itemDespesa.isPago());

        editTextValor.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                BigDecimal valor = new BigDecimal(editTextValor.getText().toString());
                itemDespesa.setValor(valor);
                new UpdateItemTask().execute(itemDespesa);
                return true;
            }
            return false;
        });

        if(itemDespesa.isEditarValor()){
            editTextValor.setVisibility(View.VISIBLE);
            buttonEditar.setVisibility(View.GONE);

            textviewValor.setVisibility(View.GONE);
            buttonConfirmar.setVisibility(View.VISIBLE);

        }else{
            editTextValor.setVisibility(View.GONE);
            buttonEditar.setVisibility(View.VISIBLE);

            textviewValor.setVisibility(View.VISIBLE);
            buttonConfirmar.setVisibility(View.GONE);
        }

        checkboxPago.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemDespesa.setPago(isChecked);
            new UpdateItemTask().execute(itemDespesa);
        });

        buttonEditar.setOnClickListener(v -> {
            itemDespesa.setEditarValor(true);
            notifyDataSetChanged();
        });

        buttonConfirmar.setOnClickListener(v -> {
            BigDecimal valor = new BigDecimal(editTextValor.getText().toString());
            itemDespesa.setValor(valor);
            new UpdateItemTask().execute(itemDespesa);
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class UpdateItemTask extends AsyncTask<ItemDespesa, Void, Void>{

        @Override
        protected Void doInBackground(ItemDespesa... itemDespesas) {

            AppDataBase.getAppDataBase(context).itemDespesaDAO().save(itemDespesas[0]);

            return null;
        }
    }
}
