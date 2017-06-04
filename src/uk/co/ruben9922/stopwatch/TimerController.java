package uk.co.ruben9922.stopwatch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.util.Duration;

// TODO: Possibly remember last valid values
public class TimerController {
    @FXML
    private Spinner<Integer> hoursSpinner;
    @FXML
    private Spinner<Integer> minutesSpinner;
    @FXML
    private Spinner<Integer> secondsSpinner;
    @FXML
    private Label hoursLeftLabel;
    @FXML
    private Label minutesLeftLabel;
    @FXML
    private Label secondsLeftLabel;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;

    private int hoursLeft;
    private int minutesLeft;
    private int secondsLeft;
    private boolean running = false;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), (actionEvent) -> updateTimeLeft())); // Using Timeline as Timer doesn't work when changing UI elements

    public void initialize() {
        // Initialisation for Timeline
        timeline.setCycleCount(Timeline.INDEFINITE);

        hoursSpinner.valueProperty().addListener((observable, oldValue, newValue) -> checkTimeIsNotZero());
        minutesSpinner.valueProperty().addListener((observable, oldValue, newValue) -> checkTimeIsNotZero());
        secondsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> checkTimeIsNotZero());
    }

    public void startButtonAction() {
        if (!running) { // If not already running (i.e. Reset, not Stop, button was last button pressed), reset time left & update UI
            resetTimeLeft();

            running = true;
            updateUIState();
            updateTimeLeftLabels();
        }

        timeline.play(); // Start/resume timer
    }

    public void stopButtonAction() {
        startButton.setVisible(true); // TODO: Possibly extract into separate method
        startButton.setManaged(true);

        stopButton.setVisible(false);
        stopButton.setManaged(false);

        timeline.pause();
    }

    public void resetButtonAction() {
        running = false;
        updateUIState();

        timeline.stop();
    }

    private void checkTimeIsNotZero() {
        startButton.setDisable(hoursSpinner.getValue() == 0 && minutesSpinner.getValue() == 0
                && secondsSpinner.getValue() == 0);
    }

    private void updateUIState() {
        // "Toggle" UI elements based on whether timer is running
        startButton.setVisible(!running);
        startButton.setManaged(!running);

        stopButton.setVisible(running);
        stopButton.setManaged(running);

        hoursSpinner.setVisible(!running);
        minutesSpinner.setVisible(!running);
        secondsSpinner.setVisible(!running);
        hoursSpinner.setManaged(!running);
        minutesSpinner.setManaged(!running);
        secondsSpinner.setManaged(!running);

        hoursLeftLabel.setVisible(running);
        minutesLeftLabel.setVisible(running);
        secondsLeftLabel.setVisible(running);
        hoursLeftLabel.setManaged(running);
        minutesLeftLabel.setManaged(running);
        secondsLeftLabel.setManaged(running);
    }

    private void updateTimeLeftLabels() {
        // Update labels with time left values
        hoursLeftLabel.setText(Integer.toString(hoursLeft));
        minutesLeftLabel.setText(Integer.toString(minutesLeft));
        secondsLeftLabel.setText(Integer.toString(secondsLeft));
    }

    private void resetTimeLeft() {
        hoursLeft = hoursSpinner.getValue();
        minutesLeft = minutesSpinner.getValue();
        secondsLeft = secondsSpinner.getValue();
    }

    private void updateTimeLeft() {
        if (secondsLeft == 0) {
            if (minutesLeft == 0) {
                if (hoursLeft != 0) {
                    secondsLeft = 59;
                    minutesLeft = 59;
                    hoursLeft--;
                }
            } else {
                secondsLeft = 59;
                minutesLeft--;
            }
        } else {
            secondsLeft--;

            if (secondsLeft == 0 && minutesLeft == 0 && hoursLeft == 0) {
                // Timer is done
                // Stop timeline (otherwise this method continues to be run and multiple alerts are shown)
                timeline.stop();

                // "Toggle" UI elements
                running = false;
                updateUIState();

                // Display alert
                displayDoneAlert();
            }
        }

        updateTimeLeftLabels();
    }

    private void displayDoneAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Timer is done!");
        alert.show();
    }
}
