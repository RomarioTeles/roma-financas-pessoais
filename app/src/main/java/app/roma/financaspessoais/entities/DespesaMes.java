package app.roma.financaspessoais.entities;

import android.content.ContentValues;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DespesaMes extends EntidadeRemovivel implements Comparable<DespesaMes>{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String periodo;

    private String categoriaNome;

    private Long categoriaId;

    public DespesaMes() {
    }

    public DespesaMes(Long id, String periodo, String categoriaNome, Long categoriaId) {
        this.id = id;
        this.periodo = periodo;
        this.categoriaNome = categoriaNome;
        this.categoriaId = categoriaId;
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

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DespesaMes that = (DespesaMes) o;
        return Objects.equals(periodo, that.periodo) &&
                Objects.equals(categoriaNome, that.categoriaNome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodo, categoriaNome);
    }

    @Override
    public String toString() {
        return "DespesaMes{" +
                "id=" + id +
                ", periodo='" + periodo + '\'' +
                ", categoriaNome='" + categoriaNome + '\'' +
                ", categoriaId=" + categoriaId +
                '}';
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("periodo", periodo);
        contentValues.put("categoriaId", categoriaId);
        contentValues.put("categoriaNome", categoriaNome);
        contentValues.put("flagRemocao", isFlagRemocao());

        return contentValues;
    }

    @Override
    public int compareTo(DespesaMes o) {
        return this.categoriaNome.compareTo(o.categoriaNome);
    }
}
