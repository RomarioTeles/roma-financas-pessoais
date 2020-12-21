package app.roma.financaspessoais.entities.rel;

import java.util.Set;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.CategoriaMeta;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;

public class ItemReceitaEmbedded {

    @Embedded
    private ItemReceita itemReceita;

    @Relation(parentColumn = "id", entityColumn = "receita_mes")
    private ReceitaMes receitaMes;

    @Relation(parentColumn = "id", entityColumn = "subcategoria")
    private Categoria subcategoria;

    public ItemReceita getItemReceita() {
        return itemReceita;
    }

    public void setItemReceita(ItemReceita itemReceita) {
        this.itemReceita = itemReceita;
    }

    public ReceitaMes getReceitaMes() {
        return receitaMes;
    }

    public void setReceitaMes(ReceitaMes receitaMes) {
        this.receitaMes = receitaMes;
    }

    public Categoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Categoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    @Override
    public String toString() {
        return "ReceitaItemComCategoria{" +
                "itemReceita=" + itemReceita +
                ", receitaMes=" + receitaMes +
                ", subcategoria=" + subcategoria +
                '}';
    }
}
