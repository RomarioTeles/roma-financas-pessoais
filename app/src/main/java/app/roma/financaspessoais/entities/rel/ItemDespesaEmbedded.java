package app.roma.financaspessoais.entities.rel;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.DespesaMes;
import app.roma.financaspessoais.entities.ItemDespesa;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;

public class ItemDespesaEmbedded {

    @Embedded
    private ItemDespesa itemDespesa;

    @Relation(parentColumn = "id", entityColumn = "despesa_mes")
    private DespesaMes despesaMes;

    @Relation(parentColumn = "id", entityColumn = "subcategoria")
    private Categoria subcategoria;

    public ItemDespesa getItemDespesa() {
        return itemDespesa;
    }

    public void setItemDespesa(ItemDespesa itemDespesa) {
        this.itemDespesa = itemDespesa;
    }

    public DespesaMes getDespesaMes() {
        return despesaMes;
    }

    public void setDespesaMes(DespesaMes despesaMes) {
        this.despesaMes = despesaMes;
    }


    public Categoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Categoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    @Override
    public String toString() {
        return "DespesaItemComCategoria{" +
                "itemDespesa=" + itemDespesa +
                ", despesaMes=" + despesaMes +
                ", subcategoria=" + subcategoria +
                '}';
    }
}
