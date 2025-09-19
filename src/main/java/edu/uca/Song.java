package edu.uca;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Song {
    private final String title;
    private final String album;
    private final int year;
    private final List<String> artists;
    private final List<String> featuring;

    public Song(String title, String album, int year, List<String> artists, List<String> featuring) {
        this.title = title;
        this.album = album;
        this.year = year;
        this.artists = List.copyOf(artists);
        this.featuring = List.copyOf(featuring);
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public int getYear() {
        return year;
    }

    public List<String> getArtists() {
        return Collections.unmodifiableList(artists);
    }

    public List<String> getFeaturing() {
        return Collections.unmodifiableList(featuring);
    }

    public String normalizedTitle() {
        return normalize(title);
    }

    private static String normalize(String value) {
        return value.toLowerCase().trim();
    }

    public boolean matchesTitle(String otherTitle) {
        return normalizedTitle().equals(normalize(otherTitle));
    }

    public boolean isNear(Song other) {
        if (other == null) {
            return false;
        }

        boolean sameAlbum = !album.isBlank() && album.equalsIgnoreCase(other.album);
        boolean sharedArtist = intersects(artists, other.artists);
        boolean sharedFeature = intersects(featuring, other.featuring) || intersects(artists, other.featuring)
                || intersects(featuring, other.artists);
        int yearDifference = Math.abs(year - other.year);

        return sameAlbum || sharedArtist || sharedFeature || yearDifference <= 1;
    }

    private boolean intersects(List<String> first, List<String> second) {
        for (String a : first) {
            for (String b : second) {
                if (normalize(a).equals(normalize(b))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return normalizedTitle().equals(song.normalizedTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalizedTitle());
    }
}
