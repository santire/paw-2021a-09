package ar.edu.itba.paw.service.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class UriBuilder {

    private String baseUri;
    private StringBuilder pathBuilder;
    private Map<String, String> queryParams;

    private UriBuilder(String baseUri) {
        this.baseUri = baseUri;
        this.pathBuilder = new StringBuilder();
        this.queryParams = new LinkedHashMap<>();
    }

    public static UriBuilder fromUri(String baseUri) {
        return new UriBuilder(baseUri);
    }

    public static void main(String[] args) throws URISyntaxException {
        // Example usage:
        URI uri = UriBuilder.fromUri("https://example.com")
                .path("path")
                .queryParam("param1", "value1")
                .queryParam("param2", "value2")
                .build();

        System.out.println(uri.toString());
    }

    public UriBuilder path(String pathSegment) {
        pathBuilder.append("/").append(pathSegment);
        return this;
    }

    public UriBuilder queryParam(String key, String value) {
        queryParams.put(key, value);
        return this;
    }

    public URI build() {
        StringBuilder uriBuilder = new StringBuilder(baseUri);

        if (pathBuilder.length() > 0) {
            uriBuilder.append(pathBuilder);
        }

        if (!queryParams.isEmpty()) {
            uriBuilder.append("?");
            queryParams.forEach((key, value) -> {
                try {
                    uriBuilder.append(URLEncoder.encode(key, "UTF-8"))
                            .append("=")
                            .append(URLEncoder.encode(value, "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    // Handle the exception as needed
//                    e.printStackTrace();
                }
            });

            // Remove the trailing "&"
            uriBuilder.deleteCharAt(uriBuilder.length() - 1);
        }
        try {
            return new URI(uriBuilder.toString());
        } catch (URISyntaxException e) {
            return null;
        }

    }
}