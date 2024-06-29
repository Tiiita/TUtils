package de.tiiita.util.http;

import com.google.gson.JsonElement;
import de.tiiita.util.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public abstract class HttpRequest {
    protected final HttpURLConnection connection;

    public HttpRequest(String url) {
        try {
            this.connection = (HttpURLConnection) new URL(url).openConnection();
            configureConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addRequestProperty(String key, String value) {
        connection.addRequestProperty(key, value);
    }

    protected abstract void configureConnection() throws ProtocolException;

    public JsonElement getJsonValue(String key) {
        if (connection == null)
            throw new IllegalStateException("Request cannot be sent when no connection is available.");

        try {
            String response = StringUtils.asString(connection.getInputStream());
            return StringUtils.toJson(response).getAsJsonObject().get(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    public HttpURLConnection getConnection() {
        return connection;
    }
}
