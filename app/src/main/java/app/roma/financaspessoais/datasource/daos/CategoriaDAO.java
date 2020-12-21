package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.rel.CategoriaComMetas;
import io.reactivex.Flowable;

@Dao
public abstract class CategoriaDAO extends BaseDAO<Categoria>{

    @Query("SELECT * FROM Categoria")
    public abstract Flowable<List<CategoriaComMetas>> getTodasCategoriasComMetas();

    @Query("SELECT * FROM Categoria AS c JOIN categoria_meta m ON m.categoria = c.id WHERE UPPER(c.nome) = UPPER(:nome) AND m.periodo = :periodo")
    public abstract Flowable<List<CategoriaComMetas>> getTodasCategoriasComMetas(String nome, String periodo);

    @Query("SELECT * FROM Categoria WHERE subcategoria = 1")
    public abstract Flowable<List<Categoria>> getTodasSubcategorias();

}
