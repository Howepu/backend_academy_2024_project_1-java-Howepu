package backend.academy.hangman;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SuppressWarnings("uncommentedmain")
public class Hangman {
    private static final int MAX_ATTEMPTS = 6;

    // Частный конструктор для предотвращения создания экземпляров
    private Hangman() {
        throw new AssertionError("Не удается создать экземпляр служебного класса");
    }

    // Метод для управления игрой
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        Random random = new Random();

        log.info("Игра Виселица");

        Category category = null;
        Difficulty difficulty = null;

        while (category == null) {
            log.info("Выберите категорию (животные/фрукты/города) или оставьте пустым для случайного выбора:");
            String categoryInput = console.nextLine().toLowerCase();

            if (categoryInput.isEmpty()) {
                category = WordList.chooseRandomCategory(random);
            } else {
                category = Arrays.stream(Category.values())
                    .filter(c -> c.getName().equals(categoryInput))
                    .findFirst()
                    .orElse(null);
            }

            if (category == null) {
                log.info("Неверная категория. Попробуйте снова");
            }
        }

        while (difficulty == null) {
            log.info("Выберите уровень сложности (лёгкий/средний/сложный) или оставьте пустым для случайного выбора:");
            String difficultyInput = console.nextLine().toLowerCase();

            if (difficultyInput.isEmpty()) {
                difficulty = WordList.chooseRandomDifficulty(random);
            } else {
                difficulty = Arrays.stream(Difficulty.values())
                    .filter(d -> d.getName().equals(difficultyInput))
                    .findFirst()
                    .orElse(null);
            }

            if (difficulty == null) {
                log.info("Неверная сложность. Попробуйте снова");
            }
        }

        String word = WordList.chooseRandomWord(random, category, difficulty);
        GameLogic game = new GameLogic(word, MAX_ATTEMPTS);

        while (!game.isGameOver()) {
            log.info("Слово: " + game.getDisplayedChars());
            log.info("У вас осталось " + game.attemptsLeft() + " попыток!");
            log.info("Введите букву: ");
            String guess = console.nextLine();
            game.makeGuess(guess);
            game.drawHangman();
        }

        if (game.isWon()) {
            log.info("Поздравляем! Вы угадали слово: " + game.word());
        } else {
            log.info("Вы проиграли! Загаданное слово было: " + game.word());
        }

        console.close();
    }
}
