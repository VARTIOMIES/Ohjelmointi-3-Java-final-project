package fi.tuni.prog3.sisuTest;
/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.prog3.sisu.Main;
import fi.tuni.prog3.sisu.MainStage;
import fi.tuni.prog3.sisu.NewStudentScene;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.WindowAssert;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

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
        String[] queries = {"#logInLabel",
                "#studentNumberLabel",
                "#studentNumberField",
                "#nextButton",
                "#newStudentButton"
        };
        String[] texts = {"Kirjaudu sisään",
                "Opiskelijanumero",
                "",
                "Jatka",
                "Uusi oppilas"
        };
        for (int i = 0; i<queries.length;i++){
            if (!texts[i].isEmpty()) {
                verifyThat(queries[i], hasText(texts[i]));
            }
        }
    }

    @Test
    public void testNewStudentSceneElements(){
        // First we must click on the button to get into the
        // NewStudentScene
        FxRobot robot = new FxRobot();
        robot.clickOn("#newStudentButton");


        String[] queries = {"#newStudentLabel",
                "#nameLabel",
                "#nameField",
                "#studentNumberLabel",
                "#studentNumberField",
                "#startingYearLabel",
                "#startingYearField",
                "#degreeLabel",
                "#previousButton",
                "#nextButton"
        };

        String[] texts = {"Uusi oppilas",
                "Koko nimi",
                "",
                "Opiskelijanumero",
                "",
                "Opintojen aloitusvuosi",
                "",
                "Valitse tutkinto (voit vaihtaa tämän myöhemmin)",
                "Takaisin",
                "Jatka"
        };

        for (int i = 0; i<queries.length;i++){
            if (!texts[i].isEmpty()) {
                verifyThat(queries[i], hasText(texts[i]));
            }
        }
    }

    @Test
    public void testCourseTabElements(){

    }




    //TODO: Robottitestit
    @ParameterizedTest
    @MethodSource("argumentProvider")
    public void testInputsNewStudentScene(String s1,String s2,String s3){
        FxRobot robot = new FxRobot();
        robot.clickOn("#newStudentButton");

        robot.clickOn("#nameField");
        robot.write(s1,20);

        robot.clickOn("#studentNumberField");
        robot.write(s2,20);

        robot.clickOn("#startingYearField");
        robot.write(s3,20);

        robot.clickOn("#nextButton");
        // Jotain tapahtuu mitä ei pitäisi tapahtua
        var notNull1 = fromAll().lookup("#alreadyUsedInfoLabel");
        var notNull2 = fromAll().lookup("#notFilledAllInfoLabel");


        assertNotNull(notNull1);
        assertNotNull(notNull2);

    }
    @Test
    public void testAllElementsOneByOne(){
        FxRobot robot = new FxRobot();

    }

    static Stream<Arguments> argumentProvider(){
        return Stream.of(
                arguments("Onni Merilä","H299725","2020")
        );
    }


    static Stream<Arguments> argumentListProvider(){
        return Stream.of(
                arguments(new String[]{}
                )

        );
    }



    public static void main(String[] args) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
    }




}
