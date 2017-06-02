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
        startButton.setVisible(false);
        startButton.setManaged(false);

        stopButton.setVisible(true);
        stopButton.setManaged(true);

        hoursSpinner.setVisible(false);
        minutesSpinner.setVisible(false);
        secondsSpinner.setVisible(false);
        hoursSpinner.setManaged(false);
        minutesSpinner.setManaged(false);
        secondsSpinner.setManaged(false);

        hoursLeftLabel.setVisible(true);
        minutesLeftLabel.setVisible(true);
        secondsLeftLabel.setVisible(true);
        hoursLeftLabel.setManaged(true);
        minutesLeftLabel.setManaged(true);
        secondsLeftLabel.setManaged(true);
    }

    public void stopButtonAction() {
        stopButton.setVisible(false);
        stopButton.setVisible(false);

        startButton.setVisible(true);
        startButton.setVisible(true);

        hoursSpinner.setVisible(true);
        minutesSpinner.setVisible(true);
        secondsSpinner.setVisible(true);
        hoursSpinner.setManaged(true);
        minutesSpinner.setManaged(true);
        secondsSpinner.setManaged(true);

        hoursLeftLabel.setVisible(false);
        minutesLeftLabel.setVisible(false);
        secondsLeftLabel.setVisible(false);
        hoursLeftLabel.setManaged(false);
        minutesLeftLabel.setManaged(false);
        secondsLeftLabel.setManaged(false);
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
