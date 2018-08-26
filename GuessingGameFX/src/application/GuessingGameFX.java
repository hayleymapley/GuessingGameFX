package application;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This program is a simple guessing game where a number between 1-100 is chosen and
 * the user makes guesses. They are told whether their guess needs to be higher or lower
 * and a congratulations when they guess correctly. When the number is guessed, if the
 * guesses amount is fewer than the current high score (set by default at max. (10) on launch)
 * then the high score is set to that.
 * 
 * @author mapleyhayl
 *
 */

public class GuessingGameFX extends Application {

	private int score = 10; //default high score
	private int guesses = 0; //current number of guesses made
	private int target = 0; //number the user is trying to guess
	private int range = 100;

	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 500;

	private ArrayList<Integer> guessedNums = new ArrayList<>();

	private BackgroundImage background = new BackgroundImage(new Image("/guessing.jpg"),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT);

	private GridPane pane = new GridPane();
	
	private final Text welcomeText = new Text();
	private final Text rangeText = new Text();
	private final Text guessText = new Text();
	private Text scoreDisplay = new Text();
	
	private final TextField textField = new TextField();
	
	private Button newRound = new Button();
	private Button quit = new Button();
	private Button guess = new Button();

	/**
	 * Standard start method for JavaFX
	 */
	@Override
	public void start(Stage primaryStage) {

		//randomly generates target number
		target = (int)Math.ceil(Math.random()*range);

		//		For testing - to see the target: 
		System.out.println("Target: " + target);

		//displays menu
		initialisePane();
		
		//		restricts input to textField so it only allows integers
		//		inspiration: https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx#
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("^\\d{0,3}$")) {
					textField.setText(oldValue);
				}
			}
		});
		
		// Set newRound button handler
		newRound.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textField.setVisible(true);
				guessText.setFill(Color.WHITE);
				target = (int) Math.ceil(Math.random()*100);
				guesses = 0;
				guessedNums.clear();
				rangeText.setText("\nCan you guess the secret number? (1-" + range +")");
				guessText.setText("Target has been reset - enter your guess!"); //change color back to black
			}
		});

		// Set quit button handler
		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.close();
			}
		});

		// Set guess button handler
		guess.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				guessText.setFill(Color.WHITE);
				guesses++;
				int guess = Integer.parseInt(textField.getText());
				textField.clear();
				guessCheck(guess);
			}
		});

		// Set stage attributes
		primaryStage.setTitle("Guessing Game");
		primaryStage.setScene(new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT));
		primaryStage.show();
	}

	/**
	 * Sets attributes of all visual and interactive elements
	 * and the main pane.
	 */
	public void initialisePane() {
		// Set text attributes
		welcomeText.setText("Welcome to the Guessing Game!\n");
		welcomeText.setFont(Font.font(30));
		GridPane.setConstraints(welcomeText, 1, 0);

		rangeText.setText("\nCan you guess the secret number in 10 guesses? (1-100)");
		rangeText.setFill(Color.WHITE);
		GridPane.setConstraints(rangeText, 1, 4);
		
		guessText.setText("Enter your guess:");
		guessText.setFill(Color.WHITE);
		GridPane.setConstraints(guessText, 1, 5);
		
		scoreDisplay.setFill(Color.WHITE);
		scoreDisplay.setText("Fewest Guesses: " + score);
		GridPane.setConstraints(scoreDisplay, 2, 8);
		
		// Set TextField attributes
		GridPane.setConstraints(textField, 1, 6);
		
		// Set button attributes
		newRound.setText("New round");
		GridPane.setConstraints(newRound, 2, 6);
		newRound.setFocusTraversable(false); //stops new game button being highlighted after every num input
		newRound.setPrefSize(80, 25);
		
		quit.setText("Quit");
		GridPane.setConstraints(quit, 2, 7);
		quit.setFocusTraversable(false);
		quit.setPrefSize(80, 25);
		
		guess.setText("Guess");
		guess.setDefaultButton(true); //sets guess button as default button
		GridPane.setConstraints(guess, 1, 7);
//		inspiration: https://stackoverflow.com/questions/20264480/javafx-bind-to-multiple-properties
		BooleanBinding booleanBinding = textField.textProperty().isEqualTo("");
		guess.disableProperty().bind(booleanBinding);
		
		// Set pane attributes
		pane.setBackground(new Background(background));
		pane.setPadding(new Insets(25,25,25,25));
		pane.setVgap(10);
		pane.setHgap(25);
		pane.setFocusTraversable(true);

		// Add all elements to pane
		pane.getChildren().addAll(welcomeText, rangeText, textField, guessText, guess, newRound, quit, scoreDisplay);
		pane.setAlignment(Pos.CENTER);
	}

	/**
	 * Takes the textField entry as an integer and checks it
	 * @param guess
	 */
	public void guessCheck(int guess) {
		System.out.println(target);
		if (guess < 1 || guess > range) { // if guess is invalid
			guessText.setFill(Color.RED);
			guessText.setText("Please provide a valid guess: (Between 1-" + range +")");
		} else if (guess == target) { // if guess is correct
			guessedNums.add(guess);
			guessText.setFill(Color.WHITE);
			guessText.setText("Congratulations! You guessed correctly!\nClick the New Round button to play again!");
			if (guesses < score) { 		// if guess is less than high score, update score
				score = guesses;
				guesses = 0;
			}
			scoreDisplay.setText("Fewest Guesses: " + score); 
			textField.setVisible(false);
		} else if (guess < target) {
			guessText.setText("Guess higher - "  + (10-guesses) + " guesses remaining:");
			guessedNums.add(guess);
		} else if (guess > target) {
			guessText.setText("Guess lower - "  + (10-guesses) + " guesses remaining:");
			guessedNums.add(guess);
		}
		if (guesses == 10) {
			guessText.setText("Game over! You ran out of guesses.\nClick the New Round button to play again!");
			guessedNums.add(guess);
			textField.setVisible(false);
		}
		if (!guessedNums.isEmpty()) {
			rangeText.setText("\n" + guessedNums.toString());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
