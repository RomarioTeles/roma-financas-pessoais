package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.ReceitaMes;
import app.roma.financaspessoais.entities.rel.ReceitaMesComItems;

@Dao
public abstract class ReceitaMesDAO extends BaseDAO<ReceitaMes> {

    @Query("SELECT * FROM receitames rm Where rm.periodo = :periodo")
    public abstract LiveData<List<ReceitaMesComItems>> getTodasComItemReceita(String periodo);

    @Query("SELECT COALESCE(SUM(ir.valor), 0.0) FROM receitames AS r " +
            "join itemreceita AS ir ON ir.receita_mes = r.id and ir.flagRemocao = 0 and r.flagRemocao = 0 " +
            "join Categoria AS c ON r.categoriaId = c.id " +
            "Where r.periodo = :periodo ")
    public abstract LiveData<Double> getTotalReceitas(String periodo);

    @Query("SELECT COALESCE(SUM(ir.valor), 0.0) FROM receitames AS r " +
            "join itemreceita AS ir ON ir.receita_mes = r.id and ir.flagRemocao = 0 and r.flagRemocao = 0 " +
            "join Categoria AS c ON r.categoriaId = c.id " +
            "Where r.periodo = :periodo and ir.pago = 1")
    public abstract LiveData<Double> getTotalReceitasPagas(String periodo);

    @Query("SELECT distinct periodo FROM receitames order by id")
    public abstract List<String> getTodosPeriodos();

    @Query("SELECT COUNT(*) FROM receitames rm WHERE rm.periodo = :periodo")
    public abstract Integer countByPeriodo(String periodo);

    @Query("SELECT * FROM receitames WHERE periodo = :periodo")
    public abstract List<ReceitaMes> findByPeriodo(String periodo);
}
