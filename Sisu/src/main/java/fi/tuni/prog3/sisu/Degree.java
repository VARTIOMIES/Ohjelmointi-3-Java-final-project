package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Degree {
    private final String id;
    private final String code;
    private final String language;
    private final String groupId;
    private final String name;
    private final int creditsMin;

    private ArrayList<Course> courses;



    public Degree(String id, String code, String language, String groupId, String name, int creditsMin) {
        this.id = id;
        this.code = code;
        this.language = language;
        this.groupId = groupId;
        this.name = name;
        this.creditsMin = creditsMin;


        courses = new ArrayList<>();
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


    public ArrayList<Course> getCourses() {
        return courses;
    }
}
