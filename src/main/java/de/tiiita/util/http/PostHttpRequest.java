package de.tiiita.util.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;

public class PostHttpRequest extends HttpRequest {

    public PostHttpRequest(String url) {
        super(url);
    }

    @Override
    protected void configureConnection() throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
    }

    public void sendPostData(String postData) {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            os.write(postDataBytes);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error writing POST data", e);
        }
    }
}
