package fi.tuni.prog3.sisuTest;
/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.prog3.sisu.Main;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

public class SisuTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Main().start(stage);

    }

    @Override public void stop() {

    }

    //TODO: Klikkeritestit

    //TODO: Tiedustelutestit
    @Test
    public void testLogInStageElements(){

    }

    @Test
    public void testSignInStageElements(){

    }



    //TODO: Robottitestit


    public static void main(String[] args) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
    }




}
