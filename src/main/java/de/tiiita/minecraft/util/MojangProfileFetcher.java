package de.tiiita.minecraft.util;

import com.google.gson.JsonElement;
import de.tiiita.Logger;
import de.tiiita.StringUtils;
import de.tiiita.ThreadChecker;
import net.md_5.bungee.api.ProxyServer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.UUID;
import java.util.logging.Level;

public class MojangProfileFetcher {

    private final static String UUID_URL = "https://api.mojang.com/user/profile/%s";
    private final static String NAME_URL = "https://api.mojang.com/users/profiles/minecraft/%s";


    /**
     * Fetches the name or uuid from the mojang api by using the opposite identifier.
     * This code blocks the thread for about 250-500 ms!
     *
     * @param identifier the name or uuid of the player.
     * @return the name of the player.
     * @throws IOException           if no name was found or an error occurred an IOEx will be thrown.
     * @throws UserNotFoundException if no user was found with the given identifier.
     */
    @Nullable
    private static String fetchData(String identifier) throws IOException {
        ThreadChecker.asyncOnly();

        HttpGet httpGet = new HttpGet(String.format(getUrlByIdIdentifier(identifier), identifier));

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse response = httpClient.execute(httpGet);

            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                ProxyServer.getInstance().getLogger().log(Level.WARNING,
                        "Did not get response code as expected (mojang profile fetcher) (" + responseCode + ")");
                return null;
            }

            JsonElement value = getFromJson(EntityUtils.toString(response.getEntity()), getKeyByIdentifier(identifier));
            response.close();

            if (value == null) {
                throw new UserNotFoundException("User with identifier (uuid or name): " + identifier + " not found. Error Message: " + getFromJson(EntityUtils.toString(response.getEntity()), "errorMessage"));
            }

            return value.getAsString();
        }
    }

    /**
     * This fetches the name or uuid and handles the exception so the user gets a name safely.
     *
     * @param identifier the name or uuid of the user. You get the opposite identifier.
     * @return the found identifier or "/" if no user was found or an exception was thrown. The exception will be printed to the console.
     */
    public static String fetchDataSafely(String identifier) {
        try {
            return fetchData(identifier);
        } catch (Exception e) {
            Logger.logError("Error while fetching mojang. Used identifier: " + e.getMessage());
            return "/";
        }
    }

    /**
     * This fetches the name or uuid safely too, but it also returns null of the user was not found.
     * This method only prints out the error if it is a real error and not just a user that wasn't found.
     * Use this method if you are not sure if the user with the given uuid exists, but you still want to get the name
     * if the user exists.
     *
     * @param identifier the uuid of the user.
     * @return the name as string or null if no user was found.
     */
    @Nullable
    public static String fetchDataOrNull(String identifier) {
        String name;
        try {
            name = fetchData(identifier);
        } catch (UserNotFoundException e) {
            name = null;
        } catch (Exception e) {
            Logger.logError("Error while fetching mojang. Used identifier: " + e.getMessage());
            name = null;
        }

        return name;
    }

    private static JsonElement getFromJson(String json, String key) {
        return StringUtils.fromJsonString(json).getAsJsonObject().get(key);
    }

    private static String getUrlByIdIdentifier(String identifier) {
        if (isUuid(identifier)) {
            return UUID_URL;
        } else return NAME_URL;
    }

    private static String getKeyByIdentifier(String identifier) {
        if (isUuid(identifier)) {
            return "name";
        } else return "id";
    }

    private static boolean isUuid(String identifier) {
        try {
            //If the exception is thrown it's a name.
            UUID.fromString(identifier);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
