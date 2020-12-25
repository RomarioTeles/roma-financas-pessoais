package app.roma.financaspessoais.datasource;

import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import app.roma.financaspessoais.datasource.daos.CategoriaDAO;
import app.roma.financaspessoais.datasource.daos.DataConverters;
import app.roma.financaspessoais.datasource.daos.DespesaMesDAO;
import app.roma.financaspessoais.datasource.daos.ItemDespesaDAO;
import app.roma.financaspessoais.datasource.daos.ItemReceitaDAO;
import app.roma.financaspessoais.datasource.daos.ReceitaMesDAO;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.CategoriaMeta;
import app.roma.financaspessoais.entities.DespesaMes;
import app.roma.financaspessoais.entities.ItemDespesa;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;
import app.roma.financaspessoais.entities.TipoLancamento;
import app.roma.financaspessoais.entities.rel.CategoriaComMetas;

@Database(entities = {
        Categoria.class,
        CategoriaMeta.class,
        ReceitaMes.class,
        ItemReceita.class,
        DespesaMes.class,
        ItemDespesa.class
}, version = 200, exportSchema = false)
@TypeConverters(DataConverters.class)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    public abstract CategoriaDAO categoriaDAO();

    public abstract ReceitaMesDAO receitaMesDAO();

    public abstract DespesaMesDAO despesaMesDAO();

    public abstract ItemReceitaDAO itemReceitaDAO();

    public abstract ItemDespesaDAO itemDespesaDAO();

    public static AppDataBase getAppDataBase(Context context){
        if(INSTANCE == null){
            buildDataBase(context);
        }
        return INSTANCE;
    }

    private static void buildDataBase(Context context){
        Builder<AppDataBase> database = Room.databaseBuilder(context, AppDataBase.class, "database11111111");
        database.fallbackToDestructiveMigration();
        database.addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Log.i("Create Date Called", "Get it...");
                db.beginTransaction();
                try {

                    String periodo = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale.getDefault()));

                    db.insert("categoria", 5, new Categoria("Gastos Essenciais", TipoLancamento.DESPESA, false).toContentValues());
                    db.insert("categoria", 5, new Categoria("Desejos", TipoLancamento.DESPESA, false).toContentValues());
                    db.insert("categoria", 5, new Categoria("Poupança", TipoLancamento.DESPESA, false).toContentValues());

                    db.insert("categoria", 5, new Categoria("Moradia", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Estudos", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Alimentação", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Saúde", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Lazer", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Transporte", TipoLancamento.DESPESA, true).toContentValues());

                    db.insert("categoria", 5, new Categoria("Salário", TipoLancamento.RECEITA, false).toContentValues());
                    db.insert("categoria", 5, new Categoria("Dividendos", TipoLancamento.RECEITA, false).toContentValues());
                    db.insert("categoria", 5, new Categoria("Renda Extra", TipoLancamento.RECEITA, false).toContentValues());


                    db.insert("receitames", 5, new ReceitaMes(null, periodo, 10L,  "Salário").toContentValues());
                    db.insert("receitames", 5, new ReceitaMes(null, periodo, 11L,  "Dividendos").toContentValues());
                    db.insert("receitames", 5, new ReceitaMes(null, periodo, 12L,  "Renda Extra").toContentValues());

                    db.insert("itemreceita", 5, new ItemReceita(null, "Pagamento", LocalDateTime.now(), BigDecimal.ZERO, false, true, 1L).toContentValues());
                    db.insert("itemreceita", 5, new ItemReceita(null, "Meus dividendos", LocalDateTime.now(), BigDecimal.ZERO, false, true, 2L).toContentValues());
                    db.insert("itemreceita", 5, new ItemReceita(null, "Minhas Rendas Extras", LocalDateTime.now(), BigDecimal.ZERO, false, true, 3L).toContentValues());


                    db.insert("despesames", 5, new DespesaMes(null, periodo, "Gastos Essenciais", 1L).toContentValues());
                    db.insert("despesames", 5, new DespesaMes(null, periodo, "Desejos", 2L).toContentValues());
                    db.insert("despesames", 5, new DespesaMes(null, periodo, "Poupança", 3L).toContentValues());


                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Luz", LocalDateTime.now(), BigDecimal.ZERO, false, true, 1L, 4L ).toContentValues());
                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Água", LocalDateTime.now(),BigDecimal.ZERO, false, true, 1L, 4L ).toContentValues());
                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Telefone", LocalDateTime.now(),BigDecimal.ZERO, false, true, 1L, 4L ).toContentValues());
                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Condomínio", LocalDateTime.now(),BigDecimal.ZERO, false, true, 1L, 4L ).toContentValues());
                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Internet", LocalDateTime.now(),BigDecimal.ZERO, false, true, 1L, 4L ).toContentValues());
                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Combustível", LocalDateTime.now(),BigDecimal.ZERO, false, true, 1L, 9L ).toContentValues());

                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Aporte Mensal", LocalDateTime.now(),BigDecimal.ZERO, false, true, 3L, 3L ).toContentValues());

                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Cinema", LocalDateTime.now(),BigDecimal.ZERO, false, true, 2L, 8L ).toContentValues());
                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Praia", LocalDateTime.now(),BigDecimal.ZERO, false, true, 2L, 8L ).toContentValues());
                    db.insert("itemdespesa", 5, new ItemDespesa(null, "Parque", LocalDateTime.now(),BigDecimal.ZERO, false, true, 2L, 8L ).toContentValues());

                    db.setTransactionSuccessful();
                }catch (Exception e){
                    Log.e("Create Database", e.getMessage(), e);
                }finally {
                    db.endTransaction();
                }

            }

            @Override
            public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                super.onDestructiveMigration(db);
                INSTANCE = null;
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        });

        INSTANCE = database.build();
    }



}
