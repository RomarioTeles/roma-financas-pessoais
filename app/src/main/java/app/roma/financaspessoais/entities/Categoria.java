package app.roma.financaspessoais.entities;

import android.content.ContentValues;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Categoria extends EntidadeRemovivel implements Listavel{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String nome;

    private String cor = "#FFFFFF";

    @ColumnInfo(name = "tipo_lancamento")
    private TipoLancamento tipoLancamento;

    private boolean subcategoria;

    public static Categoria from(TipoLancamento tipoLancamento){
        Categoria categoria = new Categoria();
        categoria.nome = "Nova Categoria";
        categoria.tipoLancamento = tipoLancamento;
        return categoria;
    }

    public Categoria(Long id, String nome, TipoLancamento tipoLancamento) {
        this.id = id;
        this.nome = nome;
        this.tipoLancamento = tipoLancamento;
    }

    public Categoria(String nome, TipoLancamento tipoLancamento, boolean subcategoria) {
        this.nome = nome;
        this.tipoLancamento = tipoLancamento;
        this.subcategoria = subcategoria;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public boolean isSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(boolean subcategoria) {
        this.subcategoria = subcategoria;
    }

    public Categoria() {
    }

    @Override
    public String getConteudo() {
        return nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoLancamento getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(TipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(nome, categoria.nome) &&
                tipoLancamento == categoria.tipoLancamento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, tipoLancamento);
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("nome", nome);
        contentValues.put("subcategoria", subcategoria);
        contentValues.put("tipo_lancamento", tipoLancamento.name());
        contentValues.put("cor", cor);
        contentValues.put("flagRemocao", isFlagRemocao());

        return contentValues;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
