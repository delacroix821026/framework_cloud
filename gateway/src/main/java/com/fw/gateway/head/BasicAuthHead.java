package com.fw.gateway.head;

import java.nio.charset.Charset;

import static feign.Util.ISO_8859_1;
import static feign.Util.checkNotNull;

public class BasicAuthHead {
    public BasicAuthHead(String username, String password) {
        this(username, password, ISO_8859_1);
    }

    private final String headerValue;
    /**
     * Creates an interceptor that authenticates all requests with the specified username and password
     * encoded using the specified charset.
     *
     * @param username the username to use for authentication
     * @param password the password to use for authentication
     * @param charset  the charset to use when encoding the credentials
     */
    public BasicAuthHead(String username, String password, Charset charset) {
        checkNotNull(username, "username");
        checkNotNull(password, "password");
        this.headerValue = "Basic " + base64Encode((username + ":" + password).getBytes(charset));
    }

    public String getHeaderValue() {
        return this.headerValue;
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.encode(bytes);
    }
}
