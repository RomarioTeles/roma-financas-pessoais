package app.roma.financaspessoais.entities.rel;

import java.util.Objects;
import java.util.Set;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;

public class ReceitaMesComItems {

    @Embedded
    private ReceitaMes receitaMes;

    @Relation(
            parentColumn = "id",
            entityColumn = "receita_mes"
    )
    private Set<ItemReceita> itens;

    public ReceitaMes getReceitaMes() {
        return receitaMes;
    }

    public void setReceitaMes(ReceitaMes receitaMes) {
        this.receitaMes = receitaMes;
    }

    public Set<ItemReceita> getItens() {
        return itens;
    }

    public void setItens(Set<ItemReceita> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceitaMesComItems that = (ReceitaMesComItems) o;
        return Objects.equals(receitaMes, that.receitaMes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receitaMes);
    }
}
