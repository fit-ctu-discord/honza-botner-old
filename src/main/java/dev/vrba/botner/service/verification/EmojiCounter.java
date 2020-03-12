package dev.vrba.botner.service.verification;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import dev.vrba.botner.database.DatabaseConnection;
import dev.vrba.botner.database.entities.CountedEmoji;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.emoji.KnownCustomEmoji;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class EmojiCounter {
    private Dao<CountedEmoji, Long> dao;

    public EmojiCounter(DiscordApi api) throws SQLException {
        ConnectionSource source = DatabaseConnection.getGlobalInstance().getSource();
        this.dao = DaoManager.createDao(source, CountedEmoji.class);
    }

    public List<CountedEmoji> all() throws SQLException {
        return this.dao.queryForAll();
    }

    public void increment(KnownCustomEmoji emoji) throws SQLException {
        CountedEmoji record = this.dao.queryForId(emoji.getId());

        if (record == null)
        {
            record = new CountedEmoji(emoji.getId(), 0, new Date());
        }

        record.times++;

        this.dao.createOrUpdate(record);
    }

    public void decrement(KnownCustomEmoji emoji) throws SQLException {
        CountedEmoji record = this.dao.queryForId(emoji.getId());

        if (record == null)
        {
            return;
        }

        record.times--;

        this.dao.createOrUpdate(record);
    }
}
