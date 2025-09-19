package gui;

import chungus.app.Chungus;
import javafx.animation.PauseTransition;
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

    private Chungus chungus;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image chungusImage = new Image(this.getClass().getResourceAsStream("/images/chungus.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Chungus instance */
    public void setChungus(Chungus c) {
        chungus = c;
        // Show welcome message when Chungus is initialized
        String welcomeMessage = "Hello! I'm Chungus!\nWhat can I do for you?\n\n"
                + "💡 Type 'help' to see all available commands!";
        dialogContainer.getChildren().addAll(
                DialogBox.getChungusDialog(welcomeMessage, chungusImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing
     * Chungus's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = chungus.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getChungusDialog(response, chungusImage));
        userInput.clear();

        // Check if user wants to exit
        if (chungus.isExitCommand(input)) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> javafx.application.Platform.exit());
            delay.play();
        }
    }
}
