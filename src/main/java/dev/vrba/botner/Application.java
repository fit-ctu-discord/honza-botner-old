package dev.vrba.botner;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vrba.botner.config.BotnerConfiguration;
import dev.vrba.botner.discord.MessageDispatcher;
import dev.vrba.botner.discord.ReactionsHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application
{
    public static void main(String[] args)
    {
        // Configure environment from the .env file
        Dotenv dotenv = Dotenv.load();

        String token = dotenv.get("DISCORD_TOKEN");

        // Load discord token
        if (token == null)
        {
            Logger.getGlobal().log(Level.SEVERE, "Cannot start without DISCORD_TOKEN being set.");
            return;
        }

        // Load bot configuration
        try
        {
            BotnerConfiguration configuration = loadConfiguration();
        }
        catch (IOException e)
        {
            Logger.getGlobal().log(Level.SEVERE, "Cannot load configuration from the config.json in the application resources.");
            return;
        }

        // Create discord client and connect to the API
        DiscordApi client = new DiscordApiBuilder()
                .setToken(token)
                .login()
                .join();

        // Create and bind event listeners
        MessageDispatcher dispatcher = new MessageDispatcher();
        ReactionsHandler handler = new ReactionsHandler();

        client.addMessageCreateListener(dispatcher::handleMessage);
        client.addReactionAddListener(handler::handleReactionAdded);
        client.addReactionRemoveListener(handler::handleReactionRemoved);
    }

    private static BotnerConfiguration loadConfiguration() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = Application.class.getClassLoader();
        String resourceFile = Objects.requireNonNull(classLoader.getResource("config.json")).getFile();

        BotnerConfiguration configuration = mapper.readValue(new File(resourceFile), BotnerConfiguration.class);

        configuration.setGlobalInstance(configuration);

        return configuration;
    }
}
