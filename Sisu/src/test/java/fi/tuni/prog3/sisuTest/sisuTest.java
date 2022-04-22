package fi.tuni.prog3.sisuTest;
/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.prog3.sisu.Main;
import javafx.application.Application;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;

public class sisuTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        try{
            Application sisu = new Main();
            sisu.start(stage);
        }
        catch(Exception e) {
            System.out.println("Problem with IO");

        }


    }



}
