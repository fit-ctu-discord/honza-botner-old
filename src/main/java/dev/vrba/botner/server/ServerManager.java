package dev.vrba.botner.server;

import dev.vrba.botner.config.BotnerConfiguration;
import express.Express;
import org.javacord.api.DiscordApi;

import java.sql.SQLException;

public class ServerManager
{
    private Express server;

    public ServerManager()
    {
        this.server = new Express();
    }

    public void start(DiscordApi api) throws Exception
    {
        this.server.bind(new ServerRequestsHandler(api));
        this.server.listen(80);
    }

    public void stop()
    {
        this.server.stop();
    }
}
