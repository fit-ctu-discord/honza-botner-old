package dev.vrba.botner.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.net.URI;

public class DatabaseConnection
{
    private ConnectionSource source;

    private static DatabaseConnection globalInstance;

    public DatabaseConnection(String databaseUrl) throws Exception
    {
        URI databaseUri = new URI(databaseUrl);

        String username = databaseUri.getUserInfo().split(":")[0];
        String password = databaseUri.getUserInfo().split(":")[1];
        String url = "jdbc:postgresql://" + databaseUri.getHost() + ':' + databaseUri.getPort() + databaseUri.getPath() + "?sslmode=require";

        this.source = new JdbcConnectionSource(url, username, password);
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
