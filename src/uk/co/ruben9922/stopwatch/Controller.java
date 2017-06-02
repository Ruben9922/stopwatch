package uk.co.ruben9922.stopwatch;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

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

    public void startButtonAction() {
        setUIState(true);
    }

    public void stopButtonAction() {
        setUIState(false);
    }

    private void setUIState(boolean timerStarted) {
        startButton.setVisible(!timerStarted);
        startButton.setVisible(!timerStarted);

        stopButton.setVisible(timerStarted);
        stopButton.setVisible(timerStarted);

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

    public void update() {
        if (secondsLeft == 0) {
            if (minutesLeft == 0) {
                if (hoursLeft == 0) {
                    // Timer done
                } else {
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
        }
    }
}
