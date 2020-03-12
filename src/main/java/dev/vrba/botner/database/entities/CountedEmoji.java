package dev.vrba.botner.database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "counted_emoji")
public class CountedEmoji {

    public CountedEmoji() {}

    public CountedEmoji(long id, long used, Date firstUsedAt)
    {
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
}
