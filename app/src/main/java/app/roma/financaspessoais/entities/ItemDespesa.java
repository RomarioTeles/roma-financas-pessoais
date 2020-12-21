package app.roma.financaspessoais.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemDespesa extends EntidadeRemovivel{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private LocalDateTime data;

    private BigDecimal valor;

    private boolean pago;

    @ColumnInfo(name = "despesa_mes")
    private Long despesaMes;

    private Long subcategoria;

    private String cor = "#FFFFFF";

    public ItemDespesa() {
    }

    public ItemDespesa(Long id, LocalDateTime data, BigDecimal valor, boolean pago, Long despesaMes, Long subcategoria, String cor) {
        this.id = id;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
        this.despesaMes = despesaMes;
        this.subcategoria = subcategoria;
        this.cor = cor;
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
        return valor;
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
}
