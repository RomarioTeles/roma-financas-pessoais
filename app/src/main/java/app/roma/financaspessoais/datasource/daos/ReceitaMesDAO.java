package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.ReceitaMes;
import app.roma.financaspessoais.entities.rel.CategoriaComMetas;
import app.roma.financaspessoais.entities.rel.ReceitaMesComItems;
import io.reactivex.Flowable;

@Dao
public abstract class ReceitaMesDAO extends BaseDAO<ReceitaMes> {

    @Query("SELECT * FROM receita_mes Where periodo = :periodo")
    public abstract LiveData<List<ReceitaMesComItems>> getTodasComItemReceita(String periodo);

    @Query("SELECT COALESCE(SUM(ir.valor), 0.0) FROM receita_mes AS r " +
            "join item_receita AS ir ON ir.receita_mes = r.id " +
            "join Categoria AS c ON r.categoriaId = c.id " +
            "Where r.periodo = :periodo ")
    public abstract LiveData<Double> getTotalReceitas(String periodo);

    @Query("SELECT COALESCE(SUM(ir.valor), 0.0) FROM receita_mes AS r " +
            "join item_receita AS ir ON ir.receita_mes = r.id " +
            "join Categoria AS c ON r.categoriaId = c.id " +
            "Where r.periodo = :periodo ")
    public abstract LiveData<Double> getTotalReceitasPagas(String periodo);
}
