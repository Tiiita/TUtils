package de.tiiita.minecraft.bungee;


import de.tiiita.util.StringUtils;
import de.tiiita.util.ThreadChecker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class VersionNameFetcher {
    private final String URL = "https://raw.githubusercontent.com/PrismarineJS/minecraft-data/master/data/pc/common/protocolVersions.json";

    /**
     * ONLY USE ASYNC!
     * Get the understandable version name by the bungee protocol id
     * @param protocolId the protocol id of the player
     * @return the found version name or null if nothing was found
     */
    public String getVersionName(int protocolId) {
        ThreadChecker.asyncOnly();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(2500);

            String response = StringUtils.asString(connection.getInputStream());
            return StringUtils.toJson(response).get("version").getAsString();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }
}
