package dev.vrba.botner;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vrba.botner.config.BotnerConfiguration;
import dev.vrba.botner.database.DatabaseConnection;
import dev.vrba.botner.discord.MessageDispatcher;
import dev.vrba.botner.discord.ReactionsHandler;
import dev.vrba.botner.server.ServerManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application
{
    public static void main(String[] args) throws SQLException
    {
        System.out.println("OMFG FUNGUJ");

        Logger logger = Logger.getGlobal();

        // Configure environment from the .env file
        Dotenv dotenv = Dotenv.load();

        String token = dotenv.get("DISCORD_TOKEN");

        // Load discord token
        if (token == null)
        {
            logger.log(Level.SEVERE, "Cannot start without DISCORD_TOKEN being set.");
            return;
        }

        // Load bot configuration
        try
        {
            Application.loadConfiguration(dotenv.get("CONFIG_FILE"));
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, "Cannot load configuration from the config.development.json in the application resources.");
            return;
        }

        logger.log(Level.INFO, "Loaded configuration");

        // Create discord client and connect to the API
        DiscordApi client = new DiscordApiBuilder()
                .setToken(token)
                .login()
                .join();

        logger.log(Level.INFO, "Connected to Discord");

        try
        {
            // Create database connection
            new DatabaseConnection(dotenv.get("DATABASE_URL"));

            // Start the web server used for verification
            ServerManager server = new ServerManager();
            System.out.println(args.length);
            System.out.println(args[0]);
            int port = Integer.parseInt(args[0]);
            server.start(client, port);

            logger.log(Level.INFO, "Started webserver");
        }
        catch (Exception exception)
        {
            logger.log(Level.SEVERE, "Cannot establish the database connection.");
            return;
        }

        logger.log(Level.INFO, "Connected to the database");

        // Create and bind event listeners
        MessageDispatcher dispatcher = new MessageDispatcher();
        ReactionsHandler handler = new ReactionsHandler();

        client.addMessageCreateListener(dispatcher::handleMessage);
        client.addReactionAddListener(handler::handleReactionAdded);
        client.addReactionRemoveListener(handler::handleReactionRemoved);

        logger.log(Level.INFO, "Registered event listeners.");
    }

    private static void loadConfiguration(String source) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        BotnerConfiguration configuration = mapper.readValue(new File(source), BotnerConfiguration.class);

        configuration.setGlobalInstance(configuration);
    }
}
