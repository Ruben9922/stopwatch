package uk.co.ruben9922.stopwatch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.util.Duration;

public class Controller { // TODO: refactor so that tabs have own controllers
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
    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), (actionEvent) -> updateTimeLeft())); // Using Timeline as Timer doesn't work when changing UI elements

    public void initialize() {
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void startButtonAction() {
        updateUIState(true);

        hoursLeft = hoursSpinner.getValue();
        minutesLeft = minutesSpinner.getValue();
        secondsLeft = secondsSpinner.getValue();

        updateTimeLeftLabels();

        startTimer();
    }

    public void stopButtonAction() {
        updateUIState(false);

        // Stop timer
        timeline.stop();
    }

    private void updateUIState(boolean timerStarted) {
        startButton.setVisible(!timerStarted);
        startButton.setManaged(!timerStarted);

        stopButton.setVisible(timerStarted);
        stopButton.setManaged(timerStarted);

        hoursSpinner.setVisible(!timerStarted);
        minutesSpinner.setVisible(!timerStarted);
        secondsSpinner.setVisible(!timerStarted);
        hoursSpinner.setManaged(!timerStarted);
        minutesSpinner.setManaged(!timerStarted);
        secondsSpinner.setManaged(!timerStarted);

        hoursLeftLabel.setVisible(timerStarted);
        minutesLeftLabel.setVisible(timerStarted);
        secondsLeftLabel.setVisible(timerStarted);
        hoursLeftLabel.setManaged(timerStarted);
        minutesLeftLabel.setManaged(timerStarted);
        secondsLeftLabel.setManaged(timerStarted);
    }

    private void updateTimeLeftLabels() {
        // Update labels with time left values
        hoursLeftLabel.setText(Integer.toString(hoursLeft));
        minutesLeftLabel.setText(Integer.toString(minutesLeft));
        secondsLeftLabel.setText(Integer.toString(secondsLeft));
    }

    private void startTimer() {
        timeline.play();
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
