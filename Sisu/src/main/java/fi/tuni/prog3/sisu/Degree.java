package fi.tuni.prog3.sisu;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Degree {
    private final String id;
    private final String code;
    private final String language;
    private final String groupId;
    private final String name;
    private final int creditsMin;

    public Degree(String id, String code, String language, String groupId, String name, int creditsMin) {
        this.id = id;
        this.code = code;
        this.language = language;
        this.groupId = groupId;
        this.name = name;
        this.creditsMin = creditsMin;
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public int getCreditsMin() {
        return creditsMin;
    }

    public String getCode() {
        return code;
    }

    /*
    public HttpClient getClient() {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .uri(URI.create("https://sis-tuni.funidata.fi/kori/api/modules/otm-47b7d28d-e5c3-4dd1-bbf1-2e57fe913978"))
                    .build();
    }
*/

}
