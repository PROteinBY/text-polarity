package by.bstu.feis.ii12.core;

import by.bstu.feis.ii12.core.model.BiGram;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class KNeighborPolarityDetector implements TextPolarityDetector {

    private Integer k;
    private List<NGram> nGrams;

    public KNeighborPolarityDetector() {
        this(3);
    }

    public KNeighborPolarityDetector(Integer k) {
        this.k = k;

        try {
            ObjectMapper mapper = new ObjectMapper();
            nGrams = mapper.readValue(new File("bGramsIndex.json"), mapper.getTypeFactory().constructCollectionType(Vector.class, NGram.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<BiGram> getBiGrams(String text) {
        String[] words = text.split("\\s");
        List<BiGram> biGrams = new ArrayList<>();

        for (int i = 0; i < words.length - 1; i++) {
            biGrams.add(new BiGram(words[i], words[i + 1], 1d));
        }

        return biGrams;
    }

    @Override
    public boolean isPositive(String inputText) {
        List<BiGram> biGrams = getBiGrams(cleanText(inputText));

        List<Pair<Double, NGram>> objects = nGrams
                .parallelStream()
                .map(gram -> {
                    return new Pair<Double, NGram>(gram.getSimilarityMark(biGrams), gram);
                })
                .sorted((o1, o2) -> o2.getKey().compareTo(o1.getKey()))
                .collect(Collectors.toList());

        int balance = 0;

        for (int i = 0; i < k; i++) {
            if (objects.get(i).getValue().isPositive) {
                balance++;
            } else {
                balance--;
            }
        }

        return balance > 0;
    }

    private static String cleanText(String text) {
        return text
                .replaceAll("(<\\w+ \\/>)", " ")
                .replaceAll("[\\W\\d\\s&&[^']]", " ")
                .replaceAll(" +", " ")
                .toLowerCase();
    }

    public static class NGram {
        private Boolean isPositive;
        private List<BiGram> nGrams;

        public NGram() {
        }

        public NGram(Boolean isPositive, List<BiGram> nGrams) {
            this.isPositive = isPositive;
            this.nGrams = nGrams;
        }

        public Double getSimilarityMark(List<BiGram> biGrams) {
            List<BiGram> uniqBiGrams = new ArrayList<>();

            for (BiGram biGram : nGrams) {
                if (biGrams.contains(biGram) && !uniqBiGrams.contains(biGram)) {
                    uniqBiGrams.add(biGram);
                }
            }

            return uniqBiGrams
                    .stream()
                    .mapToDouble(BiGram::getWeight)
                    .sum();
        }

        public Boolean getPositive() {
            return isPositive;
        }

        public void setPositive(Boolean positive) {
            isPositive = positive;
        }

        public List<BiGram> getnGrams() {
            return nGrams;
        }

        public void setnGrams(List<BiGram> nGrams) {
            this.nGrams = nGrams;
        }
    }

}
