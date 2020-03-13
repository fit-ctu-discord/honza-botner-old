package dev.vrba.botner.database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@DatabaseTable(tableName = "counted_emoji")
public class CountedEmoji {

    public CountedEmoji() {
    }

    public CountedEmoji(long id, long used, Date firstUsedAt) {
        this.id = id;
        this.times = used;
        this.firstUsedAt = firstUsedAt;
    }

    @DatabaseField(id = true)
    public long id;

    @DatabaseField(defaultValue = "0")
    public long times;

    @DatabaseField(dataType = DataType.DATE)
    public Date firstUsedAt;

    public double getUsagePerDay() {
        long daysInUsage = ChronoUnit.DAYS.between(this.firstUsedAt.toInstant(), new Date().toInstant()) + 1;
        return (double) this.times / daysInUsage;
    }

    public long getTotalUsage() {
        return this.times;
    }
}
