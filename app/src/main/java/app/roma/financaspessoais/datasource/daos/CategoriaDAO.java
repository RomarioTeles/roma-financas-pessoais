package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.TipoLancamento;
import app.roma.financaspessoais.entities.rel.CategoriaComMetas;

@Dao
public abstract class CategoriaDAO extends BaseDAO<Categoria>{

    @Query("SELECT * FROM Categoria")
    public abstract LiveData<List<CategoriaComMetas>> getTodasCategoriasComMetas();

    @Query("SELECT * FROM Categoria AS c JOIN categoria_meta m ON m.categoria = c.id WHERE UPPER(c.nome) = UPPER(:nome) AND m.periodo = :periodo")
    public abstract LiveData<List<CategoriaComMetas>> getTodasCategoriasComMetas(String nome, String periodo);

    @Query("SELECT * FROM Categoria WHERE subcategoria = 1 and tipo_lancamento = :tipoLancamento and flagRemocao = 0")
    public abstract LiveData<List<Categoria>> getTodasSubcategorias(TipoLancamento tipoLancamento);

    @Query("SELECT * FROM Categoria WHERE tipo_lancamento = :tipoLancamento ")
    public abstract LiveData<List<Categoria>> getTodasCategorias(TipoLancamento tipoLancamento);

}
