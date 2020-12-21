package app.roma.financaspessoais;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import androidx.room.Room;
import app.roma.financaspessoais.datasource.AppDataBase;
import app.roma.financaspessoais.entities.rel.CategoriaComMetas;

public class RomaApplication extends Application {

    private AppDataBase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDataBase.getAppDataBase(this);
    }

    public AppDataBase getDatabase() {
        return database;
    }
}
