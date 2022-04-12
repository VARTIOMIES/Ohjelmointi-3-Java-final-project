package fi.tuni.prog3.sisu;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;




public class Main extends Application {


    @Override
    public void start(Stage stage) {

        var label = new Label("SISU");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {

        JSONread();

        launch();
    }




    public static void JSONread() throws IOException {
        String stringURL = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";


        URL url = new URL(stringURL);

        URLConnection request = url.openConnection();

        // Importing Gson to parse JSON data

        JsonElement element = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject elementObject = element.getAsJsonObject();
        JsonArray degreePrograms = elementObject.get("searchResults").getAsJsonArray();

        // Initializing Degree objects
        for(var degree : degreePrograms) {
            var id = degree.getAsJsonObject().get("id").getAsString();
            var code = degree.getAsJsonObject().get("code").getAsString();
            var language = degree.getAsJsonObject().get("lang").getAsString();
            var groupId = degree.getAsJsonObject().get("groupId").getAsString();
            var name = degree.getAsJsonObject().get("name").getAsString();
            var creditsMin = degree.getAsJsonObject().get("credits").toString();

            var newDegree = new Degree(id,code,language,groupId,name,creditsMin);
            System.out.println(newDegree.getName());

        }

    }


}
