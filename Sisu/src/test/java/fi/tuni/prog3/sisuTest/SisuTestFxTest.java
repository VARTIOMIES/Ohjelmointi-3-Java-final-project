package fi.tuni.prog3.sisuTest;
/*
    @author - Onni Merila , onni.merila@tuni.fi , H299725
 */

import fi.tuni.prog3.sisu.Main;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.robot.Motion;
import org.testfx.util.WaitForAsyncUtils;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class SisuTestFxTest extends ApplicationTest {

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

        FxRobot robot = new FxRobot();
        goToMainMenu(robot);
        WaitForAsyncUtils.waitForFxEvents();

        robot.clickOn("#designTab");
        verifyThat("#designTab",Node::isVisible);

        robot.clickOn("#courseTab");
        verifyThat("#courseTab",Node::isVisible);

        robot.clickOn("#personalTab");
        verifyThat("#personalTab",Node::isVisible);

    }

    private void goToMainMenu(FxRobot robot){
        robot.clickOn("#newStudentButton");

        robot.clickOn("#nameField");
        robot.write("Teemu Teekkari",1);

        robot.clickOn("#studentNumberField");
        robot.write("69696969",1);

        robot.clickOn("#startingYearField");
        robot.write("1999",1);

        robot.clickOn("#degreeComboBox", Motion.DIRECT);
        robot.moveBy(0,50);
        robot.scroll(30,VerticalDirection.DOWN);
        robot.clickOn(MouseButton.PRIMARY);

        robot.clickOn("#nextButton");
    }

    @Test
    public void testLogOut(){
        FxRobot robot = new FxRobot();
        goToMainMenu(robot);
        robot.clickOn("#personalTab");
        robot.clickOn("#logOutLabel");
        verifyThat("#logInLabel",Node::isVisible);
    }

    @ParameterizedTest
    @MethodSource("goodInputArgumentProvider")
    public void testPersonalTabElement(String s1,String s2,String s3){
        FxRobot robot = new FxRobot();
        createNewStudentWithUi(s1,s2,s3,robot);
        clickOn("#personalTab");
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#nameInfoLabel",hasText("Koko nimi"));
        verifyThat("#nameLabel",hasText(s1));
        verifyThat("#studentNumberInfoLabel",hasText("Opiskelijanumero"));
        verifyThat("#studentNumberLabel",hasText(s2));
        verifyThat("#emailInfoLabel",hasText("Sähköpostiosoite"));
        // TODO:
        //  verifyThat("#emailLabel",hasText(<email>));

    }




    //TODO: Robottitestit
    @ParameterizedTest
    @MethodSource("badInputArgumentProvider")
    public void testInputsNewStudentScene(String s1,String s2,String s3){
        FxRobot robot = new FxRobot();
        createNewStudentWithUi(s1,s2,s3,robot);

        if ("".equals(s1) || "".equals(s2)  || "".equals(s3)){
            verifyThat("#notFilledAllInfoLabel",Node::isVisible);
        }

        else {
            verifyThat("#greetinLabel",Node::isVisible);

        }

    }
    private void createNewStudentWithUi(String name,String studentNumber,
                                        String startingYear,FxRobot robot){
        robot.clickOn("#newStudentButton");

        robot.clickOn("#nameField");
        robot.write(name);

        robot.clickOn("#studentNumberField");
        robot.write(studentNumber);

        robot.clickOn("#startingYearField");
        robot.write(startingYear);

        robot.clickOn("#degreeComboBox", Motion.DIRECT);
        robot.moveBy(0,50);
        robot.scroll(VerticalDirection.DOWN);
        robot.clickOn(MouseButton.PRIMARY);

        robot.clickOn("#nextButton");

        // Made the robot to wait for a bit because basic wait()
        // method did not work as intended
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testAllElementsOneByOne(){
        FxRobot robot = new FxRobot();

    }

    static Stream<Arguments> badInputArgumentProvider(){
        return Stream.of(
                arguments("Onni Merilä","H299324","2020"),
                arguments("","",""),
                arguments("Onni","","2020"),
                arguments("Onni","12342678","")
        );
    }


    static Stream<Arguments> goodInputArgumentProvider(){
        return Stream.of(
                arguments("Onni Merilä","H299725","2020"),
                arguments("Pertti Perälä","12222222","1982"),
                arguments("Jeesus Kristus","66642069","2001")
        );
    }



    public static void main(String[] args) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);
    }




}
