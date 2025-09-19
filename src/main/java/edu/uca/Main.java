package edu.uca;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path songsPath = args.length > 0 ? Path.of(args[0]) : Path.of("songs.csv");

        try {
            SongGame game = SongGame.fromFile(songsPath);
            System.out.println("Archivo de canciones cargado desde: " + songsPath.toAbsolutePath());
            System.out.println("Escribe 'salir' en cualquier momento para terminar.\n");
            game.play();
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el juego: " + e.getMessage());
            System.err.println("Verifica la ruta del archivo o su formato.");
        }
    }
}
