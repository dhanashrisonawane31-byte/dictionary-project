package com.dictionary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDAO {

    public List<Word> search(String keyword) throws SQLException {
        List<Word> words = new ArrayList<>();

        String sql = """
                SELECT id, word, meaning, part_of_speech, example_sentence
                FROM words
                WHERE word LIKE ?
                ORDER BY word
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    words.add(new Word(
                            rs.getInt("id"),
                            rs.getString("word"),
                            rs.getString("meaning"),
                            rs.getString("part_of_speech"),
                            rs.getString("example_sentence")
                    ));
                }
            }
        }

        return words;
    }

    public Word findExact(String word) throws SQLException {
        String sql = """
                SELECT id, word, meaning, part_of_speech, example_sentence
                FROM words
                WHERE word = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, word);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Word(
                            rs.getInt("id"),
                            rs.getString("word"),
                            rs.getString("meaning"),
                            rs.getString("part_of_speech"),
                            rs.getString("example_sentence")
                    );
                }
            }
        }

        return null;
    }

    public void addWord(String word, String meaning,
                         String partOfSpeech, String example) throws SQLException {

        String sql = """
                INSERT INTO words(word, meaning, part_of_speech, example_sentence)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, word);
            ps.setString(2, meaning);
            ps.setString(3, partOfSpeech);
            ps.setString(4, example);
            ps.executeUpdate();
        }
    }

    public void updateWord(int id, String word, String meaning,
                            String partOfSpeech, String example) throws SQLException {

        String sql = """
                UPDATE words
                SET word = ?, meaning = ?, part_of_speech = ?, example_sentence = ?
                WHERE id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, word);
            ps.setString(2, meaning);
            ps.setString(3, partOfSpeech);
            ps.setString(4, example);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    public void deleteWord(int id) throws SQLException {
        String sql = "DELETE FROM words WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
