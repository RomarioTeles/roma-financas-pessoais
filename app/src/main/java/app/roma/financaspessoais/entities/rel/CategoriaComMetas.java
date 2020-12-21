package app.roma.financaspessoais.entities.rel;

import java.util.Set;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.CategoriaMeta;

public class CategoriaComMetas {

    @Embedded Categoria categoria;

    @Relation(
            parentColumn = "id",
            entityColumn = "categoria"
    )
    Set<CategoriaMeta> metas;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Set<CategoriaMeta> getMetas() {
        return metas;
    }

    public void setMetas(Set<CategoriaMeta> metas) {
        this.metas = metas;
    }

    @Override
    public String toString() {
        return "CategoriaComMetas{" +
                "categoria=" + categoria +
                ", metas=" + metas +
                '}';
    }
}
