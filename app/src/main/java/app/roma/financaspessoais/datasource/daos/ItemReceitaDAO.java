package app.roma.financaspessoais.datasource.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.rel.ItemReceitaRecorrente;
import io.reactivex.Completable;

@Dao
public abstract class ItemReceitaDAO extends BaseDAO<ItemReceita> {


    @Query("SELECT distinct ir.descricao, r.categoriaNome, r.categoriaId FROM item_receita ir JOIN receita_mes r ON r.id = ir.receita_mes WHERE ir.recorrente = 1 and ir.flagRemocao = 0 and r.flagRemocao = 0")
    public abstract List<ItemReceitaRecorrente> getTodosItemReceitasRecorrentes();

}
