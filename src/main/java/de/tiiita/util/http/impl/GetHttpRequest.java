package de.tiiita.util.http.impl;

import de.tiiita.util.http.HttpRequest;

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
