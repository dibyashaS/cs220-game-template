package game.template;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordList {
    private String word;
    private List<String> wordList;
    private List<String> guessWords;

    public WordList() {
        wordList = new ArrayList<>();
        guessWords = new ArrayList<>();
        createWord();
    }

    public void loadWordList(InputStream in, InputStream in2) {
        try (Scanner sc = new Scanner(in)) {
            while (sc.hasNextLine()) {
                wordList.add(sc.nextLine());
            }
        }

        try (Scanner sc2 = new Scanner(in2)) {
            while (sc2.hasNextLine()) {
                wordList.add(sc2.nextLine());
            }
        }
    }

    public void createWord() {
        try {
            loadWordList(
                new FileInputStream("wordfiles/wordlist.txt"),
                new FileInputStream("wordfiles/guessList.txt")
            );
            int randomID = new Random().nextInt(wordList.size());
            word = wordList.get(randomID);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public String getWord() {
        return word;
    }

    public boolean existingWord(String possibleWord) {
        return wordList.contains(possibleWord.toLowerCase());
    }

    public static void main(String[] args) {
        WordList w = new WordList();
        System.out.println(w.getWord());
    }
}
