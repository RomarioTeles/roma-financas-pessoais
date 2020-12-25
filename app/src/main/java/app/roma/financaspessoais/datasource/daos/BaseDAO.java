package app.roma.financaspessoais.datasource.daos;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public abstract class BaseDAO<T> {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(T obj);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(T... objs);

    @Insert
    public abstract long insert(T obj);

    @Insert
    public abstract long[] insert(T... objs);

    @Delete
    public abstract void delete(T obj);

    public int deleteAll() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "delete from " + getTableName()
        );
        return doDeleteAll(query);
    }

    public List<T> findAllValid() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "select distinct * from " + getTableName() + " where flagRemocao = 0 order by id"
        );
        return doFindAllValid(query);
    }

    public T find(long id) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "select * from " + getTableName() + " where flagRemocao = 0 and id = ?",
                new Object[]{id}
        );
        return doFind(query);
    }

    String getTableName() {

        Class clazz = (Class)
                ((ParameterizedType) getClass().getSuperclass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        // tableName = StringUtil.toSnakeCase(clazz.getSimpleName());
        String tableName = clazz.getSimpleName();
        return tableName;
    }

    @RawQuery
    protected abstract List<T> doFindAllValid(SupportSQLiteQuery query);

    @RawQuery
    protected abstract T doFind(SupportSQLiteQuery query);

    @RawQuery
    protected abstract int doDeleteAll(SupportSQLiteQuery query);
}

