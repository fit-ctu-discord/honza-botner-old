package dev.vrba.botner.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseConnection
{
    private ConnectionSource source;

    private static DatabaseConnection globalInstance;

    public DatabaseConnection(String databaseUrl) throws Exception
    {
        this.source = new JdbcConnectionSource(databaseUrl);
        globalInstance = this;
    }

    public ConnectionSource getSource()
    {
        return source;
    }

    public static DatabaseConnection getGlobalInstance()
    {
        return globalInstance;
    }
}
