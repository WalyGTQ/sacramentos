import org.testfx.framework.junit.ApplicationTest;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class MiPruebaFX extends ApplicationTest {
     private Button btn;
    @Override
    public void start(Stage stage) {
        btn = new Button("Haz clic en mí");
        btn.setOnAction(e -> btn.setText("¡Gracias por hacer clic!"));
        stage.setScene(new Scene(btn, 200, 100));
        stage.show();
    }

    @Test
    public void testButtonClick() {
        // Simula un clic en el botón
        clickOn("Haz clic en mí");
        
        // Verifica que el texto del botón haya cambiado
        verifyThat(".button", hasText("¡Gracias por hacer clic!"));
    }
    
    // Prueba que verifica que el botón es visible
    @Test
    public void testButtonIsVisible() {
        assertTrue(btn.isVisible());
    }

    // Prueba que verifica el texto inicial del botón
    @Test
    public void testInitialButtonText() {
        verifyThat(".button", hasText("Haz clic en mí"));
    }

    // Prueba que simula hacer doble clic en el botón
    @Test
    public void testDoubleButtonClick() {
        // Simula un doble clic en el botón
        doubleClickOn("Haz clic en mí");
        
        // Verifica que el texto del botón haya cambiado
        verifyThat(".button", hasText("¡Gracias por hacer clic!"));
    }

    // Prueba que verifica que el botón no ha sido desactivado
    @Test
    public void testButtonIsNotDisabled() {
        assertFalse(btn.isDisabled());
    }
}
