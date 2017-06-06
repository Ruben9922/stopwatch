package uk.co.ruben9922.stopwatch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.util.Duration;

// TODO: Possibly remember last valid values
// TODO: Fix progress bar behaviour on timer finishing
// TODO: Fix layout when window resized
// TODO: Optionally play sound when finished
// TODO: Consider using properties & binding
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
    @FXML
    private ProgressBar progressBar;

    private int totalHours;
    private int totalMinutes;
    private int totalSeconds;
    private int hoursLeft;
    private int minutesLeft;
    private int secondsLeft;
    private boolean started = false;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), (actionEvent) -> decrementTimeLeft())); // Using Timeline as Timer doesn't work when changing UI elements

    public void initialize() {
        // Initialisation for Timeline
        timeline.setCycleCount(Timeline.INDEFINITE);

        hoursSpinner.valueProperty().addListener((observable, oldValue, newValue) -> checkTimeIsNotZero());
        minutesSpinner.valueProperty().addListener((observable, oldValue, newValue) -> checkTimeIsNotZero());
        secondsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> checkTimeIsNotZero());
    }

    public void startButtonAction() {
        if (!started) { // If not already started (i.e. Reset, not Stop, button was last button pressed), update fields & update UI
            updateTotalTime();
            updateTimeLeft();

            started = true;
            updateUIState();
            updateTimeLeftLabels();
            progressBar.setProgress(0);
        } else {
            updateStartStopButtonState(false);
        }

        timeline.play(); // Start/resume timer
    }

    public void stopButtonAction() {
        updateStartStopButtonState(true);

        timeline.pause();
    }

    public void resetButtonAction() {
        started = false;
        updateUIState();
        progressBar.setProgress(0);

        timeline.stop();
    }

    private void checkTimeIsNotZero() {
        startButton.setDisable(hoursSpinner.getValue() == 0 && minutesSpinner.getValue() == 0
                && secondsSpinner.getValue() == 0);
    }

    private void updateStartStopButtonState(boolean running) { // Might change later
        startButton.setVisible(running);
        startButton.setManaged(running);

        stopButton.setVisible(!running);
        stopButton.setManaged(!running);
    }

    private void updateUIState() {
        // "Toggle" UI elements based on whether timer has been started
        updateStartStopButtonState(!started);

        hoursSpinner.setVisible(!started);
        minutesSpinner.setVisible(!started);
        secondsSpinner.setVisible(!started);
        hoursSpinner.setManaged(!started);
        minutesSpinner.setManaged(!started);
        secondsSpinner.setManaged(!started);

        hoursLeftLabel.setVisible(started);
        minutesLeftLabel.setVisible(started);
        secondsLeftLabel.setVisible(started);
        hoursLeftLabel.setManaged(started);
        minutesLeftLabel.setManaged(started);
        secondsLeftLabel.setManaged(started);
    }

    private void updateTimeLeftLabels() {
        // Update labels with time left values
        hoursLeftLabel.setText(Integer.toString(hoursLeft));
        minutesLeftLabel.setText(Integer.toString(minutesLeft));
        secondsLeftLabel.setText(Integer.toString(secondsLeft));
    }

    private void updateProgressBar() {
        int totalTimeSeconds = (totalHours * 3600) + (totalMinutes * 60) + totalSeconds;
        int timeLeftSeconds = (hoursLeft * 3600) + (minutesLeft * 60) + secondsLeft;
        int timeElapsedSeconds = totalTimeSeconds - timeLeftSeconds;
        progressBar.setProgress(((double) timeElapsedSeconds / totalTimeSeconds));
    }

    private void updateTotalTime() {
        totalHours = hoursSpinner.getValue();
        totalMinutes = minutesSpinner.getValue();
        totalSeconds = secondsSpinner.getValue();
    }

    private void updateTimeLeft() {
        hoursLeft = hoursSpinner.getValue();
        minutesLeft = minutesSpinner.getValue();
        secondsLeft = secondsSpinner.getValue();
    }

    private void decrementTimeLeft() { // Possibly refactor
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
                started = false;
                updateUIState();

                // Display alert
                Platform.runLater(this::displayDoneAlert);
            }
        }

        updateTimeLeftLabels();
        updateProgressBar();
    }

    private void displayDoneAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Timer is done!");
        alert.showAndWait();
        progressBar.setProgress(0);
    }
}
