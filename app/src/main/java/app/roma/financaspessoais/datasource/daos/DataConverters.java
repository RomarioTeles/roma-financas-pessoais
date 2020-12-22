package app.roma.financaspessoais.datasource.daos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import app.roma.financaspessoais.entities.TipoLancamento;

public class DataConverters {

    @TypeConverter
    public LocalDateTime fromLongToLocalDateTime(Long millis){
        return millis == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    @TypeConverter
    public Long fromLocalDateTimeToLong(LocalDateTime localDateTime){
        return localDateTime == null ? null : ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @TypeConverter
    public TipoLancamento fromStringToTipoLancamento(String value){
        return value == null ? null : TipoLancamento.valueOf(value);
    }

    @TypeConverter
    public String fromTipoLancamentoToString(TipoLancamento value){
        return  value == null ? null : String.valueOf(value);
    }

    @TypeConverter
    public BigDecimal BigDecimalfromDouble(Double value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public Double BigDecimalToDouble(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        } else {
            return bigDecimal.doubleValue();
        }
    }

}
