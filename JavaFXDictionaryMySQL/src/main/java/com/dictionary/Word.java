package com.dictionary;

public class Word {
    private int id;
    private String word;
    private String meaning;
    private String partOfSpeech;
    private String exampleSentence;

    public Word(int id, String word, String meaning,
                String partOfSpeech, String exampleSentence) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.partOfSpeech = partOfSpeech;
        this.exampleSentence = exampleSentence;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }
}
