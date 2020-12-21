package app.roma.financaspessoais.entities;

import android.content.ContentValues;

import java.util.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "receita_mes")
public class ReceitaMes extends EntidadeRemovivel{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String periodo;

    private String categoriaNome;

    private Long categoriaId;

    public ReceitaMes(Long id, String periodo, Long categoriaId, String categoriaNome) {
        this.id = id;
        this.periodo = periodo;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
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
        ReceitaMes that = (ReceitaMes) o;
        return Objects.equals(periodo, that.periodo) &&
                Objects.equals(categoriaId, that.categoriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodo, categoriaId);
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("periodo", periodo);
        contentValues.put("categoriaId", categoriaId);
        contentValues.put("categoriaNome", categoriaNome);
        contentValues.put("flagRemocao", true);

        return contentValues;
    }
}
