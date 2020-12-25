package app.roma.financaspessoais.entities;

import android.content.ContentValues;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import app.roma.financaspessoais.datasource.daos.DataConverters;

@Entity
public class ItemDespesa extends EntidadeRemovivel implements Comparable<ItemDespesa> {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String descricao;

    private LocalDateTime data;

    private BigDecimal valor;

    private boolean pago;

    @ColumnInfo(name = "despesa_mes")
    private Long despesaMes;

    private Long subcategoria;

    private String cor = "#FFFFFF";

    private boolean recorrente;

    @Ignore
    private boolean isEditarValor;

    public ItemDespesa() {
    }

    public ItemDespesa(Long id, String descricao, LocalDateTime data, BigDecimal valor, boolean pago, boolean recorrente, Long despesaMes, Long subcategoria) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
        this.despesaMes = despesaMes;
        this.subcategoria = subcategoria;
        this.recorrente = recorrente;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor == null ? null : valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public Long getDespesaMes() {
        return despesaMes;
    }

    public void setDespesaMes(Long despesaMes) {
        this.despesaMes = despesaMes;
    }

    public Long getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Long subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isEditarValor() {
        return isEditarValor;
    }

    public void setEditarValor(boolean editarValor) {
        isEditarValor = editarValor;
    }

    public boolean isRecorrente() {
        return recorrente;
    }

    public void setRecorrente(boolean recorrente) {
        this.recorrente = recorrente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDespesa that = (ItemDespesa) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("descricao", descricao);
        contentValues.put("data", new DataConverters().fromLocalDateTimeToLong(data));
        contentValues.put("valor", new DataConverters().BigDecimalToDouble(valor));
        contentValues.put("pago", pago);
        contentValues.put("despesa_mes", despesaMes);
        contentValues.put("subcategoria", subcategoria);
        contentValues.put("cor", cor);
        contentValues.put("flagRemocao", isFlagRemocao());
        contentValues.put("recorrente", recorrente);
        return contentValues;
    }

    @Override
    public int compareTo(ItemDespesa o) {
        return o.getSubcategoria().compareTo(subcategoria);
    }
}
