package game.template;
//Imports
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Wordle {
    private static final int MAX_ATTEMPTS = 6;
    private static final int HINT_WORD_COUNT = 10;
    
    protected final WordleGameplay wordGenerator;
    private final String correctWord;
    private final List<String> attempts;
    
    public Wordle() {
        this.wordGenerator = new WordleGameplay();
        this.correctWord = wordGenerator.getWord();
        this.attempts = new ArrayList<>();
    }

    public void makeAttempt(String attempt) {
        if (attempt == null || attempt.length() != 5) {
            throw new IllegalArgumentException("Attempt must be a 5-letter word");
        }
        
        if (attempts.size() >= MAX_ATTEMPTS) {
            throw new IllegalStateException("No more attempts allowed");
        }
        
        if (wordGenerator.wordExists(attempt)) {
            attempts.add(attempt.toLowerCase());
        } else {
            throw new IllegalArgumentException("No such word exists in the dictionary");
        }
    }

    public void giveHint() {
        if (attempts.isEmpty()) {
            displayInitialHint();
        } else if (attempts.size() < 3) {
            displayHintWithGuesses();
        } else {
            displayLetterHint();
        }
    }

    private void displayInitialHint() {
        System.out.println("Possible words to try:");
        List<String> randomWords = getRandomWords(wordGenerator.getWordList(), HINT_WORD_COUNT);
        randomWords.forEach(System.out::println);
    }

    private void displayHintWithGuesses() {
        System.out.println("Possible words to try:");
        List<String> possibleWords = wordGenerator.getWordList().stream()
                .filter(word -> !attempts.contains(word.toLowerCase()))
                .collect(Collectors.toList());
        List<String> randomWords = getRandomWords(possibleWords, HINT_WORD_COUNT);
        randomWords.forEach(System.out::println);
    }

    public void displayLetterHint() {
        Set<Character> guessedLetters = new HashSet<>();
        for (String attempt : attempts) {
            for (char c : attempt.toCharArray()) {
                guessedLetters.add(c);
            }
        }
        
        for (char c : correctWord.toCharArray()) {
            if (!guessedLetters.contains(c)) {
                System.out.println("Hint: word contains: " + c);
                return;
            }
        }
        System.out.println("Exhausted all hints");
    }

    private List<String> getRandomWords(List<String> words, int count) {
        List<String> copy = new ArrayList<>(words);
        List<String> randomWords = new ArrayList<>();
        Random rand = new Random();
        
        for (int i = 0; i < count && !copy.isEmpty(); i++) {
            int randomIndex = rand.nextInt(copy.size());
            randomWords.add(copy.remove(randomIndex));
        }
        return randomWords;
    }

    public boolean isCorrect(String attempt) {
        return attempt != null && attempt.toLowerCase().equals(correctWord.toLowerCase());
    }

    public void printAttempts() {
        System.out.println("Guesses made so far: " + attempts);
    }

    public boolean isGameOver() {
        return attempts.size() >= MAX_ATTEMPTS || 
               attempts.stream().anyMatch(this::isCorrect);
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public int getRemainingAttempts() {
        return MAX_ATTEMPTS - attempts.size();
    }

    public String getFeedback(String guess) {
        return wordGenerator.generateFeedback(guess, correctWord);
    }

    public static void main(String[] args) {
        Wordle w = new Wordle();
        Scanner sc = new Scanner(System.in);

        while (!w.isGameOver()) {
            System.out.println("Enter your attempt");
            String attempt = sc.nextLine();
            try {
                w.makeAttempt(attempt);
                w.printAttempts();

                if (w.isCorrect(attempt)) {
                    System.out.println("Correct word");
                    break;
                } else {
                    w.giveHint();
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }

        if (w.isGameOver()) {
            System.out.println("Used all attempts. Correct word was: " + w.getCorrectWord());
        }
        sc.close();
    }
}