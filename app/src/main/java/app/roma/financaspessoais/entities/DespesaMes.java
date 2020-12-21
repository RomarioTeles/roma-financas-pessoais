package app.roma.financaspessoais.entities;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "despesa_mes")
public class DespesaMes extends EntidadeRemovivel{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String periodo;

    private Long categoria;

    public DespesaMes() {
    }

    public DespesaMes(Long id, String periodo, Long categoria) {
        this.id = id;
        this.periodo = periodo;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DespesaMes that = (DespesaMes) o;
        return Objects.equals(periodo, that.periodo) &&
                Objects.equals(categoria, that.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodo, categoria);
    }

    @Override
    public String toString() {
        return "DespesaMes{" +
                "id=" + id +
                ", periodo='" + periodo + '\'' +
                ", categoria=" + categoria +
                '}';
    }
}
