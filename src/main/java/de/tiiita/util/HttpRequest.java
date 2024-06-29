package de.tiiita.util;

import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HttpRequest {
    private final String requestMethod;
    private final HttpURLConnection connection;

    public HttpRequest(String requestMethod, URL url) {
        this.requestMethod = requestMethod;
        try {
            this.connection = (HttpURLConnection) url.openConnection();
            configureConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configureConnection() throws ProtocolException {
        connection.setRequestMethod(requestMethod);
        connection.setReadTimeout(3500);
    }

    public JsonElement getJsonValue(String key) {
        if (connection == null)
            throw new IllegalStateException("Request cannot be sent when no connection is available.");

        try {
            String response = StringUtils.asString(connection.getInputStream());
            return StringUtils.toJson(response).getAsJsonObject().get(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public HttpURLConnection getConnection() {
        return connection;
    }
}
