package app.roma.financaspessoais.entities;

import android.content.ContentValues;

import java.math.BigDecimal;
import java.util.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoriaMeta extends EntidadeRemovivel{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long categoria;

    private BigDecimal meta;

    private String periodo;

    public CategoriaMeta(Long categoria, BigDecimal meta, String periodo) {
        this.categoria = categoria;
        this.meta = meta;
        this.periodo = periodo;
    }

    public CategoriaMeta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaMeta that = (CategoriaMeta) o;
        return Objects.equals(categoria, that.categoria) &&
                Objects.equals(periodo, that.periodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoria, periodo);
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("categoria", categoria);
        contentValues.put("meta", meta.doubleValue());
        contentValues.put("periodo", periodo);
        contentValues.put("flagRemocao", isFlagRemocao());

        return contentValues;
    }

    @Override
    public String toString() {
        return "CategoriaMeta{" +
                "meta=" + meta +
                ", periodo='" + periodo + '\'' +
                '}';
    }
}
