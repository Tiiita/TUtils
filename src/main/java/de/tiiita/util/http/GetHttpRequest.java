package de.tiiita.util.http;

import java.net.ProtocolException;

public class GetHttpRequest extends HttpRequest {
    public GetHttpRequest(String url) {
        super(url);
    }

    @Override
    protected void configureConnection() throws ProtocolException {
        connection.setRequestMethod("GET");
    }
}
