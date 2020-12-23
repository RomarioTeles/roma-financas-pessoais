package app.roma.financaspessoais.entities.rel;

public class ItemDespesaRecorrente {

    private String descricao;

    private String categoriaNome;

    private Long categoriaId;

    private Long subcategoria;

    private String cor;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Long getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(Long subcategoria) {
        this.subcategoria = subcategoria;
    }
}
