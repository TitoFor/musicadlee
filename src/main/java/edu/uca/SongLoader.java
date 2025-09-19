package edu.uca;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class SongLoader {
    private SongLoader() {
    }

    public static List<Song> load(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new IOException("No se encontró el archivo de canciones: " + path);
        }

        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) {
            throw new IOException("El archivo de canciones está vacío: " + path);
        }

        List<Song> songs = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isBlank()) {
                continue;
            }
            if (i == 0 && line.toLowerCase().contains("titulo")) {
                continue; // encabezado
            }
            songs.add(parseLine(line, i + 1));
        }

        if (songs.isEmpty()) {
            throw new IOException("No se pudieron cargar canciones desde: " + path);
        }

        return songs;
    }

    private static Song parseLine(String line, int lineNumber) throws IOException {
        String[] parts = line.split(";");
        if (parts.length < 4) {
            throw new IOException("Formato inválido en la línea " + lineNumber + ". Se esperaban al menos 4 columnas separadas por ';'.");
        }

        String title = parts[0].trim();
        String album = parts[1].trim();
        int year;
        try {
            year = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IOException("El año debe ser un número en la línea " + lineNumber + ".", e);
        }
        List<String> artists = parseList(parts[3]);
        List<String> featuring = parts.length >= 5 ? parseList(parts[4]) : List.of();

        return new Song(title, album, year, artists, featuring);
    }

    private static List<String> parseList(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split("\\|"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
