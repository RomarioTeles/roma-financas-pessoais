package app.roma.financaspessoais.entities.rel;

import java.util.Objects;
import java.util.Set;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.roma.financaspessoais.entities.ItemDespesa;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.DespesaMes;

public class DespesaMesComItems {

    @Embedded
    private DespesaMes despesaMes;

    @Relation(
            parentColumn = "id",
            entityColumn = "despesa_mes"
    )
    private Set<ItemDespesa> itens;

    public DespesaMes getDespesaMes() {
        return despesaMes;
    }

    public void setDespesaMes(DespesaMes despesaMes) {
        this.despesaMes = despesaMes;
    }

    public Set<ItemDespesa> getItens() {
        return itens;
    }

    public void setItens(Set<ItemDespesa> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DespesaMesComItems that = (DespesaMesComItems) o;
        return Objects.equals(despesaMes, that.despesaMes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(despesaMes);
    }
}
