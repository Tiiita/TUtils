package de.tiiita.util.http.impl;

import de.tiiita.util.http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;

public class PostHttpRequest extends HttpRequest {

    private boolean postDataSent = false;
    public PostHttpRequest(String url) {
        super(url);
    }

    @Override
    protected void configureConnection() throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
    }

    public void sendBodyData(String postData) {
        if (postDataSent)
            throw new IllegalStateException("Body data has already been sent. Data can only be sent once");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            os.write(postDataBytes);
            os.flush();
            postDataSent = true;
        } catch (IOException e) {
            throw new RuntimeException("Error writing POST data", e);
        }
    }
    public void sendBodyData(String key, String value) {
        sendBodyData("{\"" + key + "\":\"" + value + "\"}");
    }

}
