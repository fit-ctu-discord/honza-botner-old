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

    public void start(DiscordApi api, int port) throws Exception
    {
        this.server.bind(new ServerRequestsHandler(api));
        this.server.listen(port);

        this.server.get("/", (req, res) -> {
            res.send("Beta");
        }).listen(port);
    }

    public void stop()
    {
        this.server.stop();
    }
}
