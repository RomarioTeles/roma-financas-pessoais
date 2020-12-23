package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.ItemDespesa;
import app.roma.financaspessoais.entities.rel.ItemDespesaRecorrente;
import app.roma.financaspessoais.entities.rel.ItemReceitaRecorrente;

@Dao
public abstract class ItemDespesaDAO extends BaseDAO<ItemDespesa> {

    @Query("SELECT distinct ir.descricao, r.categoriaNome, r.categoriaId FROM item_despesa ir JOIN receita_mes r ON r.id = ir.despesa_mes WHERE ir.recorrente = 1 and ir.flagRemocao = 0 and r.flagRemocao = 0")
    public abstract List<ItemDespesaRecorrente> getTodosItemReceitasRecorrentes();

}
