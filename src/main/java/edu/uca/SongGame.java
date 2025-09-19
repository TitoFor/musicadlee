package edu.uca;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class SongGame {
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREY = "\u001B[90m";
    private static final String RESET = "\u001B[0m";
    private static final int MAX_ATTEMPTS = 8;

    private final List<Song> songs;
    private final Random random;

    public SongGame(List<Song> songs) {
        this.songs = songs;
        this.random = new Random();
    }

    public static SongGame fromFile(Path path) throws IOException {
        return new SongGame(SongLoader.load(path));
    }

    public void play() {
        if (songs.isEmpty()) {
            System.out.println("No hay canciones para jugar.");
            return;
        }

        Song goal = songs.get(random.nextInt(songs.size()));
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Adivina la canción ===");
            System.out.println("Escribe el título exacto de la canción. Tienes " + MAX_ATTEMPTS + " intentos.\n");

            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
                System.out.print("Intento " + attempt + " de " + MAX_ATTEMPTS + ": ");
                String guessTitle = scanner.nextLine().trim();

                if (guessTitle.equalsIgnoreCase("salir")) {
                    System.out.println("Has abandonado la partida. La canción era: " + goal.getTitle());
                    return;
                }

                Optional<Song> guessSong = findByTitle(guessTitle);
                if (guessSong.isEmpty()) {
                    System.out.println("No encontré esa canción en el archivo. Revisa la ortografía y vuelve a intentarlo.\n");
                    attempt--;
                    continue;
                }

                Song song = guessSong.get();
                showSongInfo(song);

                if (song.matchesTitle(goal.getTitle())) {
                    System.out.println(GREEN + "\n¡Correcto! Adivinaste la canción." + RESET);
                    return;
                }

                if (song.isNear(goal)) {
                    System.out.println(YELLOW + "Cerca..." + RESET);
                } else {
                    System.out.println(GREY + "Lejos." + RESET);
                }

                int remainingLives = MAX_ATTEMPTS - attempt;
                System.out.println("Te quedan " + remainingLives + (remainingLives == 1 ? " vida" : " vidas") + "\n");
            }
        }

        System.out.println("\nSin vidas. La canción correcta era: " + goal.getTitle());
        showSongInfo(goal);
    }

    private void showSongInfo(Song song) {
        System.out.println("Título: " + song.getTitle());
        System.out.println("Álbum: " + song.getAlbum());
        System.out.println("Año: " + song.getYear());
        System.out.println("Artistas: " + (song.getArtists().isEmpty() ? "-" : String.join(", ", song.getArtists())));
        System.out.println("Feats: " + (song.getFeaturing().isEmpty() ? "-" : String.join(", ", song.getFeaturing())));
    }

    private Optional<Song> findByTitle(String title) {
        return songs.stream()
                .filter(song -> song.matchesTitle(title))
                .findFirst();
    }
}
