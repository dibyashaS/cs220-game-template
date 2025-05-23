package game.template;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordleGameplay {
    private static final int WORD_LENGTH = 5;
    protected final List<String> wordList;
    private String targetWord;
    
    public WordleGameplay() {
        this.wordList = new ArrayList<>();
        loadWordList();
        this.targetWord = getRandomWord();
    }
    
    private void loadWordList() {
        try (InputStream in = new FileInputStream("wordfiles/words.txt");
             Scanner scanner = new Scanner(in)) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                if (word.length() == WORD_LENGTH) {
                    wordList.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading word list: " + e.getMessage());
            // Add some default words in case file loading fails
            wordList.add("squid");
            wordList.add("apple");
            wordList.add("house");
            wordList.add("water");
            wordList.add("music");
        }
    }
    
    public String getWord() {
        return targetWord;
    }
    
    private String getRandomWord() {
        if (wordList.isEmpty()) {
            throw new IllegalStateException("Word list is empty");
        }
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.size()));
    }
    
    public boolean wordExists(String word) {
        if (word == null || word.length() != WORD_LENGTH) {
            return false;
        }
        return wordList.contains(word.toLowerCase());
    }
    
    public String generateFeedback(String guess, String target) {
        if (guess == null || target == null || 
            guess.length() != WORD_LENGTH || target.length() != WORD_LENGTH) {
            throw new IllegalArgumentException("Both guess and target must be " + WORD_LENGTH + " letters long");
        }
        
        char[] feedback = new char[WORD_LENGTH];
        boolean[] guessed = new boolean[WORD_LENGTH];
        
        // Check for correct letters in correct positions
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                feedback[i] = guess.charAt(i);
                guessed[i] = true;
            } else {
                feedback[i] = '-';
            }
        }
        
        // Check for correct letters in wrong positions
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (feedback[i] != '-') continue;
            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!guessed[j] && guess.charAt(i) == target.charAt(j)) {
                    feedback[i] = Character.toLowerCase(guess.charAt(i));
                    guessed[j] = true;
                    break;
                }
            }
        }
        
        return new String(feedback);
    }
    
    public List<String> getWordList() {
        return new ArrayList<>(wordList);
    }
}
