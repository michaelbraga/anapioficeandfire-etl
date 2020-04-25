package sample.apiextractor.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class ApiUtils {
    private final static Client client = Client.create();

    public static String get(String url){
        ClientResponse response = client.resource(url)
                .header("User-Agent", "Mozilla/5.0")
                .get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        return response.getEntity(String.class);
    }
}
