package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.DespesaMes;
import app.roma.financaspessoais.entities.DespesaMes;
import app.roma.financaspessoais.entities.rel.DespesaMesComItems;

@Dao
public abstract class DespesaMesDAO extends BaseDAO<DespesaMes> {

    @Query("SELECT * FROM despesa_mes Where periodo = :periodo")
    public abstract LiveData<List<DespesaMesComItems>> getTodasComItemDespesa(String periodo);

    @Query("SELECT COALESCE(SUM(itd.valor), 0.0) FROM despesa_mes AS d " +
            "join item_despesa AS itd ON itd.despesa_mes = d.id " +
            "join Categoria AS c ON d.categoriaId = c.id " +
            "Where d.periodo = :periodo and d.flagRemocao = 0 and itd.flagRemocao = 0")
    public abstract LiveData<Double> getTotalDespesas(String periodo);

    @Query("SELECT COALESCE(SUM(itd.valor), 0.0) FROM despesa_mes AS d " +
            "join item_despesa AS itd ON itd.despesa_mes = d.id " +
            "join Categoria AS c ON d.categoriaId = c.id " +
            "Where d.periodo = :periodo and d.flagRemocao = 0 and itd.flagRemocao = 0 and itd.pago = 1")
    public abstract LiveData<Double> getTotalDespesasPagas(String periodo);

    @Query("SELECT distinct periodo FROM despesa_mes order by id")
    public abstract List<String> getTodosPeriodos();

    @Query("SELECT COUNT(*) FROM despesa_mes rm WHERE rm.periodo = :periodo")
    public abstract Integer countByPeriodo(String periodo);

}
