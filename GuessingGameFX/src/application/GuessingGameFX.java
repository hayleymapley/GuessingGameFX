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
 * Some issues: Runtime errors (excluding negatives, decimals, text, and empty field)
 * Solved: Made the textField allow only numerical input using a listener
 *		 : Disallowed empty field input by disabling the button using a disable property bind
 *
 * Issue: new game button highlighted after every text input meaning that the user has to click on textfield
 * Solved: set new game button's 'focus traversable' property to false
 *
 * Issue: Users can continue guessing after game has ended
 * Solved: Set textfield to be invisible once game has ended until New Round button is pressed
 *
 * Issue: Program has runtime error when user enters many digits
 * Solved: add regex quantifier to textField change listener
 *
 * @author mapleyhayl
 *
 */

public class GuessingGameFX extends Application {

	private int score = 10; //default high score
	private int guesses = 0; //current number of guesses made
	private int target = 0; //number the user is trying to guess

	private static final int DEFAULT_WIDTH = 600;
	private static final int DEFAULT_HEIGHT = 500;

	private ArrayList<Integer> guessedNums = new ArrayList<>();

	private BackgroundImage background = new BackgroundImage(new Image("/guessing.jpg"),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT);

	private GridPane pane = new GridPane();
	
	private final Text welcome = new Text();
	private final Text range = new Text();
	private final Text guessText = new Text();
	private Text scoreDisplay = new Text();
	
	private final TextField textField = new TextField();
	
	private Button newRound = new Button();
	private Button quit = new Button();
	private Button guess = new Button();

	@Override
	public void start(Stage primaryStage) {

		//randomly generates target number
		target = (int)Math.ceil(Math.random()*100);

		//		For testing - to see the target: 
		System.out.println("Target: " + target);

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
				range.setText("\nCan you guess the secret number? (1-100)");
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

	public void initialisePane() {
		// Set text attributes
		welcome.setText("Welcome to the Guessing Game!\n");
		welcome.setFont(Font.font(30));
		GridPane.setConstraints(welcome, 1, 0);

		range.setText("\nCan you guess the secret number in 10 guesses? (1-100)");
		range.setFill(Color.WHITE);
		GridPane.setConstraints(range, 1, 1);
		
		guessText.setText("Enter your guess:");
		guessText.setFill(Color.WHITE);
		GridPane.setConstraints(guessText, 1, 2);
		
		scoreDisplay.setFill(Color.WHITE);
		scoreDisplay.setText("Fewest Guesses: " + score);
		GridPane.setConstraints(scoreDisplay, 2, 5);
		
		// Set TextField attributes
		GridPane.setConstraints(textField, 1, 3);
		
		// Set button attributes
		newRound.setText("New round");
		GridPane.setConstraints(newRound, 2, 3);
		newRound.setFocusTraversable(false); //stops new game button being highlighted after every num input
		newRound.setPrefSize(80, 25);
		
		quit.setText("Quit");
		GridPane.setConstraints(quit, 2, 4);
		quit.setFocusTraversable(false);
		quit.setPrefSize(80, 25);
		
		guess.setText("Guess");
		guess.setDefaultButton(true); //sets guess button as default button
		GridPane.setConstraints(guess, 1, 4);
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
		pane.getChildren().add(welcome);
		pane.getChildren().add(range);
		pane.getChildren().add(textField);
		pane.getChildren().add(guessText);
		pane.getChildren().add(guess);
		pane.getChildren().add(newRound);
		pane.getChildren().add(quit);
		pane.getChildren().add(scoreDisplay);
		pane.setAlignment(Pos.CENTER);
	}

	public void guessCheck(int guess) {
		if (guess < 1 || guess > 100) { // if guess is invalid
			guessText.setFill(Color.RED);
			guessText.setText("Please provide a valid guess: (Between 1-100)");
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
			range.setText("\n" + guessedNums.toString());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
