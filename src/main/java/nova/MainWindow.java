package nova;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Nova nova;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/Charlie.png"));
    private Image novaImage = new Image(this.getClass().getResourceAsStream("/images/IVE.png"));

    /**
     * Initializes the GUI by binding the scroll pane to the dialog container
     * and displaying a welcome message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        displayWelcome();
    }

    /**
     * Displays the welcome message when the GUI initializes.
     */
    private void displayWelcome() {
        String welcomeMessage = "Hello! I'm Nova\nWhat can I do for you?";
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(welcomeMessage, novaImage)
        );
    }

    /** Injects the nova.Nova instance */
    public void setNova(Nova n) {
        nova = n;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        assert nova != null : "nova.Nova instance not injected. Call setNova() before user input.";
        String input = userInput.getText();
        assert input != null : "TextField.getText() should not return null";
        String response = nova.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, novaImage)
        );
        userInput.clear();

        // If user typed "bye", exit after 2 seconds
        if (input.trim().equalsIgnoreCase("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}

