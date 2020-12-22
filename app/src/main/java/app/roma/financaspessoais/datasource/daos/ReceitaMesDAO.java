package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.ReceitaMes;
import app.roma.financaspessoais.entities.rel.ReceitaMesComItems;

@Dao
public abstract class ReceitaMesDAO extends BaseDAO<ReceitaMes> {

    @Query("SELECT * FROM receita_mes Where periodo = :periodo")
    public abstract LiveData<List<ReceitaMesComItems>> getTodasComItemReceita(String periodo);

    @Query("SELECT COALESCE(SUM(ir.valor), 0.0) FROM receita_mes AS r " +
            "join item_receita AS ir ON ir.receita_mes = r.id " +
            "join Categoria AS c ON r.categoriaId = c.id " +
            "Where r.periodo = :periodo and ir.flagRemocao = 0 and r.flagRemocao = 0")
    public abstract LiveData<Double> getTotalReceitas(String periodo);

    @Query("SELECT COALESCE(SUM(ir.valor), 0.0) FROM receita_mes AS r " +
            "join item_receita AS ir ON ir.receita_mes = r.id " +
            "join Categoria AS c ON r.categoriaId = c.id " +
            "Where r.periodo = :periodo and ir.flagRemocao = 0 and r.flagRemocao = 0 and ir.pago = 1")
    public abstract LiveData<Double> getTotalReceitasPagas(String periodo);

    @Query("SELECT distinct periodo FROM receita_mes order by id")
    public abstract List<String> getTodosPeriodos();
}
