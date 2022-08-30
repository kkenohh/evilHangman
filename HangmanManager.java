// Ken Oh
// CSE 143 AO with Ashley Liao
// Homework 4
// A HangmanManager allows the client to manage a game of
// Evil Hangman. It will keep track of and update the 
// guesses left, letters guessed, the current pattern of letters,
// and the current word family that is being considered. 
import java.util.*;
public class HangmanManager {
   // keeps track of guesses left
   private int guesses;
   
   // keeps track of the guessed letters
   private Set<Character> guessedChars;
   
   // keeps track of the current word family
   private Set<String> currentFamily;
   
   // keeps track of the current pattern
   private String pattern;
   
   // pre : length > 1 and max > 0 (throws 
   //       IllegalArgumentException otherwise)
   // post: makes a word family of all the words in the dictionary
   //       of the given length, not counting any duplicates.
   //       sets the number of guesses left to the given max value
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      guesses = max;
      guessedChars = new TreeSet<>();
      currentFamily = new TreeSet<>();
      String word = "";
      for (int i = 0; i < length; i++) {
         word += "-";
      }
      pattern = updatePattern(word);
      for (String s : dictionary) {
         if (s.length() == length && !currentFamily.contains(s)) {
            currentFamily.add(s);
         }   
      }
   }
   
   // returns the current word family being considered 
   // by the manager
   public Set<String> words() {
      return currentFamily;
   }
   
   // returns the number of guesses left
   public int guessesLeft() {
      return guesses;
   }
   
   // returns the letters guessed so far
   public Set<Character> guesses() {
      return guessedChars;
   }
   
   // pre : the current word family must not be empty 
   //       (throws IllegalStateException otherwise)
   // post: returns the current pattern, replacing
   //       not guessed words with dashes, and putting spaces
   //       between each character
   public String pattern() {
      if (currentFamily.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern;
   }
   
   // pre : at least 1 guess left and the current word family
   //       must not be empty (throws IllegalStateException
   //       otherwise
   // pre : as long as IllegalStateException was not thrown,
   //       the guess must not be a previous guess (throws
   //       IllegalArgumentException otherwise)
   // post: takes a guess and returns how many times
   //       the guess appears in the words of the current
   //       word family
   // post: chooses which word family is being considered by
   //       the manager
   public int record(char guess) {
      if (guesses < 1 || currentFamily.isEmpty()) {
         throw new IllegalStateException();
      }
      if (guessedChars.contains(guess)) {
         throw new IllegalArgumentException();
      }
      guessedChars.add(guess);
      Map<String, Set<String>> families = createMap();
      findLargest(families);
      return checkString(guess);
   }
   
   // takes a map and updates which word family the manager
   // is considering at the moment
   private void findLargest(Map<String, Set<String>> families) {
      for (String s : families.keySet()) {
         Set<String> patternSet = families.get(pattern);
         Set<String> pickedPattern = families.get(s);
         if (patternSet == null || pickedPattern.size() > patternSet.size()) {
            currentFamily = pickedPattern;
            pattern = s;
         }
         if (patternSet != null && pickedPattern.size() == patternSet.size()) {
            currentFamily = patternSet;
         }
      }
   }
   
   // takes a guess and a word, and returns a String of the word,
   // replacing any not guessed letters with dashes, and spaces
   // between the characters
   private String updatePattern(String word) {
      String pattern = "";
      int length = word.length();
      for (int i = 0; i < length; i++) {
         char ch = word.charAt(i);
         if (guessedChars.contains(ch)) {
            pattern += " " + ch;
         } else {
            pattern += " -";
         }
      }
      return pattern.substring(1);
   }
   
   // checks if the given word contains the guess and returns
   // true if it does, false otherwise.
   // updates count and guesses
   private int checkString(char guess) {
      int count = 0;
      for (int i = 0; i < pattern.length(); i++) {
         if (pattern.charAt(i) == guess) {
            count++;
         }
      }
      if (count == 0) {
         guesses--;
      }
      return count;
   }
   
   // takes a guess and returns a map that holds all the 
   // possible word families still remaining
   private Map<String, Set<String>> createMap() {
      Map<String, Set<String>> families = new TreeMap<>();
      for (String s : currentFamily) {
         String currentPattern = updatePattern(s);
         if (!families.containsKey(currentPattern)) {
            Set<String> newSet = new TreeSet<>();
            newSet.add(s);
            families.put(currentPattern, newSet);
         } else {
            families.get(currentPattern).add(s);
         }
      }
      return families;
   }
}