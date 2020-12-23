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
import androidx.room.TypeConverters;
import app.roma.financaspessoais.datasource.daos.DataConverters;

@Entity(tableName = "item_receita")
public class ItemReceita extends EntidadeRemovivel{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String descricao;

    private LocalDateTime data;

    private BigDecimal valor;

    private boolean pago;

    private boolean recorrente;

    @ColumnInfo(name = "receita_mes")
    private Long receitaMes;

    @Ignore
    private boolean isEditarValor;

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("descricao", descricao);
        contentValues.put("data", new DataConverters().fromLocalDateTimeToLong(data));
        contentValues.put("valor", new DataConverters().BigDecimalToDouble(valor));
        contentValues.put("pago", pago);
        contentValues.put("receita_mes", receitaMes);
        contentValues.put("flagRemocao", isFlagRemocao());
        contentValues.put("recorrente", recorrente);
        return contentValues;
    }

    public ItemReceita(Long id, String descricao, LocalDateTime data, BigDecimal valor, boolean pago, boolean recorrente, Long receitaMes) {
        this.id = id;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
        this.receitaMes = receitaMes;
        this.recorrente = recorrente;
    }

    public boolean isEditarValor() {
        return isEditarValor;
    }

    public void setEditarValor(boolean editarValor) {
        isEditarValor = editarValor;
    }

    public ItemReceita() {
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

    public Long getReceitaMes() {
        return receitaMes;
    }

    public void setReceitaMes(Long receitaMes) {
        this.receitaMes = receitaMes;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        ItemReceita that = (ItemReceita) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
