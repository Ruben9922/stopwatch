package uk.co.ruben9922.stopwatch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.util.Duration;

// TODO: Possibly remember last valid values
// TODO: Fix layout when window resized
// TODO: Optionally play sound when finished
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

    private IntegerProperty totalHours = new SimpleIntegerProperty(0);
    private IntegerProperty totalMinutes = new SimpleIntegerProperty(0);
    private IntegerProperty totalSeconds = new SimpleIntegerProperty(0);
    private IntegerProperty hoursLeft = new SimpleIntegerProperty(0);
    private IntegerProperty minutesLeft = new SimpleIntegerProperty(0);
    private IntegerProperty secondsLeft = new SimpleIntegerProperty(0);
    private BooleanProperty started = new SimpleBooleanProperty(false);
    private BooleanProperty running = new SimpleBooleanProperty(false);
    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), (actionEvent) -> decrementTimeLeft())); // Using Timeline as Timer doesn't work when changing UI elements

    public void initialize() {
        // Initialisation for Timeline
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Bind visible & managed properties of spinners and labels
        hoursSpinner.visibleProperty().bind(started.not());
        minutesSpinner.visibleProperty().bind(started.not());
        secondsSpinner.visibleProperty().bind(started.not());
        hoursSpinner.managedProperty().bind(started.not());
        minutesSpinner.managedProperty().bind(started.not());
        secondsSpinner.managedProperty().bind(started.not());
        hoursLeftLabel.visibleProperty().bind(started);
        minutesLeftLabel.visibleProperty().bind(started);
        secondsLeftLabel.visibleProperty().bind(started);
        hoursLeftLabel.managedProperty().bind(started);
        minutesLeftLabel.managedProperty().bind(started);
        secondsLeftLabel.managedProperty().bind(started);

        // Bind visible & managed properties of start & stop buttons
        startButton.visibleProperty().bind(running.not());
        startButton.managedProperty().bind(running.not());
        stopButton.visibleProperty().bind(running);
        stopButton.managedProperty().bind(running);

        // Bind total time fields
        totalHours.bind(hoursSpinner.valueProperty());
        totalMinutes.bind(minutesSpinner.valueProperty());
        totalSeconds.bind(secondsSpinner.valueProperty());

        // Bind time left labels
        hoursLeftLabel.textProperty().bind(hoursLeft.asString());
        minutesLeftLabel.textProperty().bind(minutesLeft.asString());
        secondsLeftLabel.textProperty().bind(secondsLeft.asString());

        // Disable start button when all spinners set to zero
        BooleanBinding allZero = totalHours.isEqualTo(0).and(totalMinutes.isEqualTo(0)).and(totalSeconds.isEqualTo(0));
        startButton.disableProperty().bind(allZero);

        // Bind progress bar
        NumberBinding totalTimeSeconds = totalHours.multiply(3600).add(totalMinutes.multiply(60)).add(totalSeconds);
        NumberBinding timeLeftSeconds = hoursLeft.multiply(3600).add(minutesLeft.multiply(60)).add(secondsLeft);
        NumberBinding timeElapsedSeconds = totalTimeSeconds.subtract(timeLeftSeconds);
        NumberBinding progress = new When(started)
                .then(timeElapsedSeconds.multiply(1.0).divide(totalTimeSeconds)) // .multiply(1.0) is to force double division
                .otherwise(0);
        progressBar.progressProperty().bind(progress);
    }

    public void startButtonAction() {
        if (!started.get()) { // If not already started (i.e. Reset, not Stop, button was last button pressed), reset time left
            resetTimeLeft();

            started.set(true);
        }

        running.set(true);

        timeline.play(); // Start/resume timer
    }

    public void stopButtonAction() {
        running.set(false);

        timeline.pause();
    }

    public void resetButtonAction() {
        started.set(false);
        running.set(false);

        timeline.stop();
    }

    private void resetTimeLeft() {
        hoursLeft.set(totalHours.get());
        minutesLeft.set(totalMinutes.get());
        secondsLeft.set(totalSeconds.get());
    }

    private void decrementTimeLeft() { // Possibly refactor
        if (secondsLeft.get() == 0) {
            if (minutesLeft.get() == 0) {
                if (hoursLeft.get() != 0) {
                    secondsLeft.set(59);
                    minutesLeft.set(59);
                    hoursLeft.set(hoursLeft.get() - 1);
                }
            } else {
                secondsLeft.set(59);
                minutesLeft.set(minutesLeft.get() - 1);
            }
        } else {
            secondsLeft.set(secondsLeft.get() - 1);

            if (secondsLeft.get() == 0 && minutesLeft.get() == 0 && hoursLeft.get() == 0) {
                // Timer is done
                timeline.stop(); // Stop timeline (otherwise this method continues to be run and multiple alerts are shown)

                Platform.runLater(() -> {
                    displayDoneAlert(); // Display alert

                    started.set(false);
                    running.set(false);
                });
            }
        }
    }

    private void displayDoneAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Timer is done!");
        alert.showAndWait();
    }
}
