package by.bstu.feis.ii12.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BayesTextPolarityDetector implements TextPolarityDetector {

    private TrainData trainData = loadDataSet();

    @Override
    public boolean isPositive(final String text) {
        List<String> words = Arrays.asList(cleanText(text).split(" "));

        return calculatePositive(words) > calculateNegative(words);
    }

    private double calculateNegative(List<String> words) {
        return Math.log((double) trainData.classes.neg / (double) (trainData.classes.neg + trainData.classes.neg)) +
                words.stream().mapToDouble(word -> Math.log((trainData.dataset.getOrDefault(word, new Pair(0, 0)).neg + 1.0) / (double) (trainData.dataset.size() + trainData.words.neg))).sum();
    }

    private double calculatePositive(List<String> words) {
        return Math.log((double) trainData.classes.pos / (double) (trainData.classes.pos + trainData.classes.neg)) +
                words.stream().mapToDouble(word -> Math.log((trainData.dataset.getOrDefault(word, new Pair(0, 0)).pos + 1.0) / (double) (trainData.dataset.size() + trainData.words.pos))).sum();
    }

    private static String cleanText(String text) {
        return text.replaceAll("(<\\w+ \\/>)", " ").replaceAll("[\\W\\d\\s&&[^']]", " ").replaceAll(" +", " ").toLowerCase();
    }

    private static TrainData loadDataSet() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(BayesTextPolarityDetector.class.getClassLoader().getResource("train.json"), TrainData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class TrainData {
        private Pair classes;
        private Pair words;
        private Map<String, Pair> dataset;

        public Pair getClasses() {
            return classes;
        }

        public void setClasses(final Pair classes) {
            this.classes = classes;
        }

        public Pair getWords() {
            return words;
        }

        public void setWords(final Pair words) {
            this.words = words;
        }

        public Map<String, Pair> getDataset() {
            return dataset;
        }

        public void setDataset(final Map<String, Pair> dataset) {
            this.dataset = dataset;
        }
    }

    private static class Pair {
        private int neg;
        private int pos;

        public Pair() {
        }

        public Pair(final int neg, final int pos) {
            this.neg = neg;
            this.pos = pos;
        }

        public int getNeg() {
            return neg;
        }

        public void setNeg(final int neg) {
            this.neg = neg;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(final int pos) {
            this.pos = pos;
        }
    }
}
