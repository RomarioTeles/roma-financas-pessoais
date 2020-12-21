package app.roma.financaspessoais.ui.receitas;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.roma.financaspessoais.R;
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
            items.forEach(item -> mapItems.put(item.getReceitaMes(), item.getItens()));
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
            convertView = layoutInflater.inflate(R.layout.item_listview_item_receita, null);
        }

        CheckBox checkboxPago = convertView.findViewById(R.id.checkboxPago);
        TextView textviewDescricao = convertView.findViewById(R.id.textviewDescricao);
        TextView textviewValor = convertView.findViewById(R.id.textviewValor);

        textviewDescricao.setText(itemReceita.getDescricao());
        textviewValor.setText("R$ "+itemReceita.getValor().divide(new BigDecimal(100)).toString());
        checkboxPago.setChecked(itemReceita.isPago());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
