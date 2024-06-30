package de.tiiita.util.http;

import com.google.gson.JsonElement;
import de.tiiita.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public abstract class HttpRequest {
    protected final HttpURLConnection connection;
    private final String url;

    public HttpRequest(String url) {
        try {
            this.url = url;
            this.connection = (HttpURLConnection) new URL(url).openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            configureConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addHeaderProperty(String key, String value) {
        connection.addRequestProperty(key, value);
    }

    protected abstract void configureConnection() throws ProtocolException;

    public JsonElement getJsonValue(String key) {
        if (connection == null)
            throw new IllegalStateException("Request cannot be sent when no connection is available.");


        try {
            String response = StringUtils.asString(connection.getInputStream());
            return StringUtils.fromJsonString(response).getAsJsonObject().get(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    public byte[] getAsByteArray() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] data = new byte[1024];
        int bytesRead;

        try (InputStream inputStream = connection.getInputStream()) {
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

            return buffer.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public HttpURLConnection getConnection() {
        return connection;
    }
}
