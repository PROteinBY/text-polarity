package by.bstu.feis.ii12.core.model;

import java.util.Objects;

public class BiGram {

    private String leftWord;
    private String rightWord;
    private Double weight;

    public BiGram() {

    }

    public BiGram(String leftWord, String rightWord) {
        this.leftWord = leftWord;
        this.rightWord = rightWord;
    }

    public BiGram(String leftWord, String rightWord, Double weight) {
        this.leftWord = leftWord;
        this.rightWord = rightWord;
        this.weight = weight;
    }

    public String getLeftWord() {
        return leftWord;
    }

    public void setLeftWord(String leftWord) {
        this.leftWord = leftWord;
    }

    public String getRightWord() {
        return rightWord;
    }

    public void setRightWord(String rightWord) {
        this.rightWord = rightWord;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiGram biGram = (BiGram) o;
        return Objects.equals(leftWord, biGram.leftWord) &&
                Objects.equals(rightWord, biGram.rightWord);
    }

    @Override
    public int hashCode() {

        return Objects.hash(leftWord, rightWord);
    }
}
