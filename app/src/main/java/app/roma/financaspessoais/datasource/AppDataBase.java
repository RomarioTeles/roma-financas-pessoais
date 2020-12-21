package app.roma.financaspessoais.datasource;

import android.content.Context;
import android.util.Log;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import app.roma.financaspessoais.datasource.daos.CategoriaDAO;
import app.roma.financaspessoais.datasource.daos.DataConverters;
import app.roma.financaspessoais.datasource.daos.ReceitaMesDAO;
import app.roma.financaspessoais.entities.Categoria;
import app.roma.financaspessoais.entities.CategoriaMeta;
import app.roma.financaspessoais.entities.ItemReceita;
import app.roma.financaspessoais.entities.ReceitaMes;
import app.roma.financaspessoais.entities.TipoLancamento;
import app.roma.financaspessoais.entities.rel.CategoriaComMetas;

@Database(entities = {
        Categoria.class,
        CategoriaMeta.class,
        ReceitaMes.class,
        ItemReceita.class
}, version = 1, exportSchema = false)
@TypeConverters(DataConverters.class)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    public abstract CategoriaDAO categoriaDAO();

    public abstract ReceitaMesDAO receitaMesDAO();

    public static AppDataBase getAppDataBase(Context context){
        if(INSTANCE == null){
            buildDataBase(context);
        }
        return INSTANCE;
    }

    private static void buildDataBase(Context context){
        Builder<AppDataBase> database = Room.databaseBuilder(context, AppDataBase.class, "database_1_2");
        database.fallbackToDestructiveMigration();
        database.addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Log.i("Create Date Called", "Get it...");
                db.beginTransaction();
                try {
                    db.insert("categoria", 5, new Categoria("Gastos Essenciais", TipoLancamento.DESPESA, false).toContentValues());
                    db.insert("categoria", 5, new Categoria("Salário", TipoLancamento.RECEITA, false).toContentValues());
                    db.insert("categoria", 5, new Categoria("Dividendos", TipoLancamento.RECEITA, false).toContentValues());
                    db.insert("categoria", 5, new Categoria("Renda Extra", TipoLancamento.RECEITA, false).toContentValues());

                    db.insert("categoria", 5, new Categoria("Moradia", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Estudos", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Alimentação", TipoLancamento.DESPESA, true).toContentValues());
                    db.insert("categoria", 5, new Categoria("Saúde", TipoLancamento.DESPESA, true).toContentValues());

                    db.insert("categoria_meta", 5, new CategoriaMeta(2L, BigDecimal.TEN, "12/2020").toContentValues());
                    db.insert("categoria_meta", 5, new CategoriaMeta(1L, BigDecimal.TEN, "01/2021").toContentValues());
                    db.insert("categoria_meta", 5, new CategoriaMeta(1L, BigDecimal.TEN, "02/2021").toContentValues());

                    db.insert("receita_mes", 5, new ReceitaMes(null, "12/2020", 2L,  "Salário").toContentValues());
                    db.insert("receita_mes", 5, new ReceitaMes(null, "12/2020", 3L,  "Dividendos").toContentValues());
                    db.insert("receita_mes", 5, new ReceitaMes(null, "12/2020", 4L,  "Renda Extra").toContentValues());

                    db.insert("item_receita", 5, new ItemReceita(null, "TED da Empresa", LocalDateTime.now(), new BigDecimal("2500"), true, 1L).toContentValues());
                    db.insert("item_receita", 5, new ItemReceita(null, "Parcela do 13º", LocalDateTime.now(), new BigDecimal("1250"), true, 1L).toContentValues());

                    db.insert("item_receita", 5, new ItemReceita(null, "ITAUSA", LocalDateTime.now(), new BigDecimal("10.90"), true, 2L).toContentValues());
                    db.insert("item_receita", 5, new ItemReceita(null, "WEG", LocalDateTime.now(), new BigDecimal("13.60"), true, 2L).toContentValues());
                    db.insert("item_receita", 5, new ItemReceita(null, "MAGALU", LocalDateTime.now(), new BigDecimal("60"), true, 2L).toContentValues());

                    db.insert("item_receita", 5, new ItemReceita(null, "Vendas dos Salgados", LocalDateTime.now(), new BigDecimal("560"), true, 3L).toContentValues());



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
