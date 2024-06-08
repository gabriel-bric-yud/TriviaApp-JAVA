import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

class Player { //Player class keeps track of name and score of each player.
	private String name;
	private int score = 0; //score gets initialized to 0.
	
	public Player(String n) { //constructor will be passed name.
		this.name = n;		
	}
	
	public String getName() { //Getter method for name.
		return name;
	}
	
	public int getScore() { //Getter method for score.
		return score;
	}
	
	public void increaseScore() { //Setter method to increase score.
		score++;
	}
	
	public String toString() { //Override toString method to to print player data easily.
		return  this.name + " has a score of " + String.valueOf(this.score) +".";
	}
}




class Book { //Book class will create book objects with title, author and release date.
	private String title;
	private String author;
	private int releaseDate;
	
	public Book(String t, String a, int d) { //Book constructor will take 3 arguments to initialize its attributes.
		this.title = t;
		this.author = a;
		this.releaseDate = d;
	}
	
	public String getTitle() { //Getter method for book title.
		return title;
	}
	
	public String getAuthor() { //Getter method for book author.
		return author;
	}
	
	public String getRelease() { //Getter method for book release date.
		return String.valueOf(releaseDate);
	}
	
	public String toString() { //Override toString method to print book data easily.
		return  this.title + " was written by " + this.author + " in " + String.valueOf(this.releaseDate);
	}
	
}


/**Question class creates trivia question from data in another object. (Book)
 * This class gets the correct answer, 3 wrong answers and randomized their position.
 * Uses a recursive function to fix any duplicate wrong answers.
 * This class checks whether a user submitted answer is correct.
 * Currently I only can create trivia questions from Book objects.
 * It is possible to create an "interface" class to convert any object into a usable trivia question. Next time!
 */

class Question { 
	private String question;
	private String answer;
	private Book trivia;
	private String[] wrongAnswers = new String[3]; //initialize empty array to store the wrong answers.
	private String[] answerList = new String[4]; //initialize empty array to store all answers for randomization and display.
	private int type = TriviaDriver.GENERATOR.nextInt(3); //Type attribute randomly decides format of which data is the question vs answer.
	
	
	public Question(Book book) { //constructor for Question gets passed a Book object.
		this.trivia = book;
		setQA(); //initialize question and answer attribute.
		setWrongAnswer(0); //initialize wrong answer 1.
		setWrongAnswer(1); //initialize wrong answer 2.
		setWrongAnswer(2); //initialize wrong answer 3.
		fixDuplicatesRecursive();//fix and duplicate possible answers.
		randomizeAnswerPostion(); //randomize the position of the answers in array to be displayed.
	}
	
	public String getQuestion() { //Getter Method for question.
		return question;
	}
	
	public Book getTrivia() { //Getter method for Book object.
		return trivia;
	}
	
	public String getCorrectAnswer() { //Getter method for answer.
		return answer;
	}
	
	public String getWrongAnswer(int num) { //Getter method for specific wrong answer in array.
		return wrongAnswers[num];
	}
	
	public void setQA() { //Setter method for question and correct answer. Randomize correct answer position.
		switch (type) {
			case 0: //type 0 makes title the question, and author the answer.
				this.question = trivia.getTitle();
				this.answer = trivia.getAuthor();
				break;
			case 1: //type 1 makes author the question, and title the answer.
				this.question = trivia.getAuthor();
				this.answer = trivia.getTitle();
				break;
			default: //type 2 makes title the question, and release date the answer.
				this.question = trivia.getTitle();
				this.answer = trivia.getRelease();
				break;
		}
		answerList[TriviaDriver.GENERATOR.nextInt(4)] = this.answer; //randomize the position of the correct answer to be displayed.
	}
	
	private void setWrongAnswer(int index) { //Setter method for specific wrong answer, which is appended to array.
		switch (type) {
			case 0: //type 0 makes random book author the answer.
				wrongAnswers[index] = TriviaDriver.BOOK_LIST[TriviaDriver.GENERATOR.nextInt(TriviaDriver.BOOK_LIST.length)].getAuthor();
				break;
			case 1: //type 1 makes random book title the answer.
				wrongAnswers[index] = TriviaDriver.BOOK_LIST[TriviaDriver.GENERATOR.nextInt(TriviaDriver.BOOK_LIST.length)].getTitle();
				break;
			default: //type 2 makes random book release date the answer.
				wrongAnswers[index] = TriviaDriver.BOOK_LIST[TriviaDriver.GENERATOR.nextInt(TriviaDriver.BOOK_LIST.length)].getRelease();
				break;
		}
	}
	

	private void fixDuplicatesRecursive() { //Recursive method to fix any duplicate wrong answers.
		Arrays.sort(wrongAnswers); //Start by sorting the wrongAnswers array to place identical values next to each other.
		boolean notFixed = true; //initialized notFixed value to signify the data hasn't been checked.
		while (notFixed) { //loop while data is not fixed!
			for (int i = 0; i < wrongAnswers.length; i++) { //Loop through each index of sorted array.
				if (wrongAnswers[i].equals(answer)) { //if wrongAnswer at current index is identical to correct answer...
					setWrongAnswer(i); //change current value
					fixDuplicatesRecursive(); //recursively run function to check new value.
				}
				else if (i < wrongAnswers.length - 1) { //check if current index is less than last index - 1 to avoid out of bounds run-time errors.
					if (wrongAnswers[i].equals(wrongAnswers[i+1])) { //if wrongAnswer at current index is identical to wrongAnswer at index+1
						setWrongAnswer(i); //change current value
						fixDuplicatesRecursive(); //recursively run function to check new value.
					}
				}		
			}
			notFixed = false;  //All values have been checked and fixed, end loop. Recursion is finished.	
		}
		
	}

	
	public void randomizeAnswerPostion() { //Method to fill array with wrong answers in empty slots.
		int counter = 0; //counter variable keeps track of current number wrong answer being added to array
		for (int i = 0; i < 4; i++) { //loop through all values of array
			if (answerList[i] != this.answer) { //if value at index isn't filled, insert the current wrong answer.
				answerList[i] = wrongAnswers[counter];
				counter++; //counter is increased to initialize next wrong answer.
			}
		}
	}
	
	public void displayAnswers() { //Getter method to display all the answers to the user for trivia.
		for (int i = 0; i < 4; i++) {
			System.out.print((i+1) + ") ");
			System.out.println(this.answerList[i]);
		}
	}
	
	public boolean checkAnswer(int num) { //Method will check if number that user chose matches the index of the correct answer in array.
		if (answerList[num-1] == this.answer) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String toString() { //Override toString method to display the correct type question for trivia.
		String displayQuestion;
		switch (type) {
			case 0:  //type 0 makes title the question, and author the answer.
				displayQuestion = "Which author wrote " + getQuestion() + "?";
				break;
			case 1: //type 1 makes author the question, and title the answer.
				displayQuestion = "Which book did " + getQuestion() + " write?";
				break;
			default: //type 2 makes title the question, and release date the answer.
				displayQuestion = "What year was " + getQuestion() + " released?";
				break;
		}
		return displayQuestion;
	}
}






public class TriviaDriver { //Trivia driver class uses Question and Book Objects.
	
	//Static constants to use in all classes.
	public static final Scanner SCAN = new Scanner(System.in);
	public static final Random GENERATOR = new Random();
	
	//initialize all book objects into a static array.
	public static final Book BOOK_LIST[] = {new Book("I-Robot", "Isaac Asimov", 1950), new Book("Neuromancer", "William Gibson", 1984), 
			new Book("Do Androids Dream of Electric Sheep", "Phillip K. Dick", 1968), new Book("The Demolished Man", "Alfred Bester", 1953),
			new Book("War of the Worlds", "H.G. Wells", 1898), new Book("Player Piano", "Kurt Vonnegut", 1952), new Book("Dune", "Frank Herbert", 1965),
			new Book("Brave New World", "Aldous Huxley", 1932), new Book("Nineteen Eighty-Four", "George Orwell", 1949), new Book("Frankenstein", "Mary Shelley", 1818),
			new Book("Hitchhikers Guide to the Galaxy", "Douglas Adams", 1979), new Book("Childhood's End", "Arthur C. Clarke", 1953), 
			new Book("The Martian Chronicles", "Ray Bradbury", 1951), new Book("Ringworld", "Larry Niven", 1970), new Book("Enders Game", "Orson Scott Card", 1985)};

	
	
	private static boolean start(String start) { //boolean method will check if the user want to play or quit.
		if (!start.toUpperCase().equals("Q")) {
			return true; 
		}
		else {
			return false;
		}
	}
	
	private static int inputValidation(Player user) { //method will validate user input to be number from 1 -4.
		int userNum = 0; //initialize incorrect value out of range.
		while (userNum < 1 || userNum > 4) { //loop while value is not in correct range
			try {
				System.out.print("\n" +user.getName() + " answers (1 - 4): ");
				userNum = SCAN.nextInt(); //get user answer as number.
				SCAN.nextLine();
				if (userNum < 1 || userNum > 4) {
					System.out.println("Please enter an answer from 1 - 4!"); //notify player they were out of range.
				}
				
			}
			catch (Exception err) { //catch error of player not entering a number.
				System.out.println("Please enter a number!");
				SCAN.nextLine();
			}
			
		}
		return userNum; //return validated number
	}
	

	public static void main(String[] args) {
		
		SCAN.useDelimiter("\\R"); //delimiter to prevent empty inputs from user.
		
		//Introduce application and ask user if they want to start playing.
		System.out.println("Welcome to the Sceince Fiction Trivia Battle!");
		System.out.print("\nENTER to Play Game || Q to Quit: ");
		String playGame = SCAN.nextLine();
		
		while (start(playGame)) {  //Outer loop checking whether user wants to play trivia game.
			
			//Initialize players and set player names
			System.out.print("\nName for player 1: ");
			Player player1 = new Player(SCAN.nextLine());
			System.out.print("Name for player 2: ");
			Player player2 = new Player(SCAN.nextLine());
			
			//display start info to players.
			System.out.println("\n" + player1.getName() + " vs. " + player2.getName());
			System.out.print("\nPress ENTER to begin trivia battle");
			SCAN.nextLine();
			
			//set length of trivia battle.
			int totalQuestions = 10;
			//declare empty questionList to keep track of which books have been asked about.
			Question[] questionList = new Question[totalQuestions];
			
			for (int i = 1; i <= totalQuestions; i++) { //Inner loop. Begin trivia questions.
				
				int randInt = GENERATOR.nextInt(BOOK_LIST.length); //get random number to choose book
				Question currentQuestion = new Question(BOOK_LIST[randInt]); //initialize question from random book
				
				//this loop validates that the new question is unique. Could be a recursive function. Next time!
				boolean exists = true; //initialize boolean for loop, assuming the book might exist and checking if true.
				while (exists) { //this loop checks all questions in questionList and verifies current question exists.
					for (int h = 0; h < questionList.length; h++) { //loop through question array
						if (questionList[h] != null) { //check if the array at index has a question/book
							if (questionList[h].getTrivia() == currentQuestion.getTrivia()) { //if the current question matches the current index in array.
								randInt = GENERATOR.nextInt(BOOK_LIST.length); //get new random number.
								currentQuestion = new Question(BOOK_LIST[randInt]); //make new question for new random index
								h = -1; //set counter back to 0 to check books again
							}
						}
					}
					exists = false; // book didn't exist and was now made. Loops finished.
				}
				questionList[i - 1] = currentQuestion; // add new validated question to array
				
				
				System.out.println("\n"+currentQuestion); //display current question to the player.
				currentQuestion.displayAnswers(); //display all possible answers.
				
				if (i%2 == 0) {	//if i at modulus 2 is 0, then its player 2's turn
					if (currentQuestion.checkAnswer(inputValidation(player2))) { //get player2 answer and check if it is correct
						player2.increaseScore(); 
						System.out.println("\nCorrect! ");
					}
					else {
						System.out.println("\nIncorrect! ");
					}
				}
				else { // i modulus 2 !=  0 , change turn
					if (currentQuestion.checkAnswer(inputValidation(player1))) { //get player1 answer and check if it is correct
						player1.increaseScore();
						System.out.println("\nCorrect! ");
					}
					else {
						System.out.println("\nIncorrect! ");
					}
				}
				
				if (i < totalQuestions) {  //display current scores if the last question hasn't been asked.
					System.out.println(player1);
					System.out.println(player2);
					System.out.print("\nPress ENTER to continue");
					SCAN.nextLine();
				}
				else { //The last question has been asked. Display final scores and winner.
					System.out.println("\nFinal Scores:");
					System.out.println(player1);
					System.out.println(player2);
					
					if (player1.getScore() > player2.getScore()) {
						System.out.println("\n"+ player1.getName() + " Wins!");
						
					}
					else if (player1.getScore() < player2.getScore()) {
						System.out.println("\n"+ player2.getName() + " Wins!");
						
					}
					else { //(player1.getScore() == player2.getScore()) 
						System.out.println("\nYou both tied! No one wins.");
						
					}
					
				}
				
			}
			
			//Get user input if they want to do another trivia battle.
			System.out.print("\nPress ENTER to Play Again || Q to Quit: ");
			playGame = SCAN.nextLine();
		}
		
		//program ends
		System.out.print("\nProgram Terminated");
		SCAN.close();
	}

}
