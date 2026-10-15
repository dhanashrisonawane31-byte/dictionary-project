package com.dictionary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class DictionaryApp extends Application {

    private final DictionaryDAO dao = new DictionaryDAO();

    private TextField searchField;
    private TextField wordField;
    private TextField partOfSpeechField;
    private TextArea meaningArea;
    private TextArea exampleArea;
    private Label statusLabel;

    private int selectedId = -1;

    @Override
    public void start(Stage stage) {
        Label title = new Label("📖 MySQL Dictionary");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Enter a word to search...");
        searchField.setPrefWidth(380);

        Button searchButton = new Button("Search");
        Button showAllButton = new Button("Show All");

        HBox searchBox = new HBox(10, searchField, searchButton, showAllButton);
        searchBox.setAlignment(Pos.CENTER);

        wordField = new TextField();
        wordField.setPromptText("Word");

        partOfSpeechField = new TextField();
        partOfSpeechField.setPromptText("Noun / Verb / Adjective...");

        meaningArea = new TextArea();
        meaningArea.setPromptText("Meaning");
        meaningArea.setWrapText(true);
        meaningArea.setPrefRowCount(3);

        exampleArea = new TextArea();
        exampleArea.setPromptText("Example sentence");
        exampleArea.setWrapText(true);
        exampleArea.setPrefRowCount(3);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        form.add(new Label("Word:"), 0, 0);
        form.add(wordField, 1, 0);

        form.add(new Label("Part of Speech:"), 0, 1);
        form.add(partOfSpeechField, 1, 1);

        form.add(new Label("Meaning:"), 0, 2);
        form.add(meaningArea, 1, 2);

        form.add(new Label("Example:"), 0, 3);
        form.add(exampleArea, 1, 3);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button clearButton = new Button("Clear");

        HBox buttons = new HBox(10, addButton, updateButton, deleteButton, clearButton);
        buttons.setAlignment(Pos.CENTER);

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #444;");

        VBox root = new VBox(18, title, searchBox, form, buttons, statusLabel);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f7f9fc;");

        searchButton.setOnAction(e -> search());
        showAllButton.setOnAction(e -> showAll());
        searchField.setOnAction(e -> search());

        addButton.setOnAction(e -> add());
        updateButton.setOnAction(e -> update());
        deleteButton.setOnAction(e -> delete());
        clearButton.setOnAction(e -> clearForm());

        Scene scene = new Scene(root, 700, 650);

        stage.setTitle("JavaFX Dictionary");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        checkConnection();
    }

    private void checkConnection() {
        try (var con = DatabaseConnection.getConnection()) {
            statusLabel.setText("Connected to MySQL successfully.");
        } catch (SQLException e) {
            statusLabel.setText("MySQL connection failed: " + e.getMessage());
        }
    }

    private void search() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Enter a word to search.");
            return;
        }

        try {
            List<Word> results = dao.search(keyword);

            if (results.isEmpty()) {
                clearForm();
                statusLabel.setText("No word found.");
                return;
            }

            Word w = results.get(0);
            selectedId = w.getId();

            wordField.setText(w.getWord());
            partOfSpeechField.setText(w.getPartOfSpeech());
            meaningArea.setText(w.getMeaning());
            exampleArea.setText(w.getExampleSentence());

            statusLabel.setText(results.size() + " result(s) found. Showing first match.");
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void showAll() {
        try {
            List<Word> results = dao.search("");

            StringBuilder text = new StringBuilder();

            for (Word w : results) {
                text.append(w.getWord())
                    .append(" - ")
                    .append(w.getMeaning())
                    .append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("All Words");
            alert.setHeaderText("Dictionary Words");
            TextArea area = new TextArea(text.toString());
            area.setEditable(false);
            area.setWrapText(true);
            area.setPrefSize(500, 350);
            alert.getDialogPane().setContent(area);
            alert.showAndWait();

        } catch (SQLException e) {
            showError(e);
        }
    }

    private void add() {
        if (!validateForm()) return;

        try {
            dao.addWord(
                    wordField.getText().trim(),
                    meaningArea.getText().trim(),
                    partOfSpeechField.getText().trim(),
                    exampleArea.getText().trim()
            );

            statusLabel.setText("Word added successfully.");
            clearForm();
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void update() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.WARNING, "Search/select a word before updating.");
            return;
        }

        if (!validateForm()) return;

        try {
            dao.updateWord(
                    selectedId,
                    wordField.getText().trim(),
                    meaningArea.getText().trim(),
                    partOfSpeechField.getText().trim(),
                    exampleArea.getText().trim()
            );

            statusLabel.setText("Word updated successfully.");
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void delete() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.WARNING, "Search a word before deleting.");
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete '" + wordField.getText() + "'?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirm.setTitle("Confirm Delete");

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                dao.deleteWord(selectedId);
                clearForm();
                statusLabel.setText("Word deleted successfully.");
            } catch (SQLException e) {
                showError(e);
            }
        }
    }

    private boolean validateForm() {
        if (wordField.getText().trim().isEmpty()
                || meaningArea.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    "Word and Meaning are required.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        selectedId = -1;
        wordField.clear();
        partOfSpeechField.clear();
        meaningArea.clear();
        exampleArea.clear();
        searchField.clear();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showError(Exception e) {
        statusLabel.setText("Error: " + e.getMessage());

        Alert alert = new Alert(
                Alert.AlertType.ERROR,
                e.getMessage(),
                ButtonType.OK
        );
        alert.setTitle("Database Error");
        alert.setHeaderText("Something went wrong");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
