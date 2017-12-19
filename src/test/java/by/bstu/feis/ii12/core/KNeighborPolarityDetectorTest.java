package by.bstu.feis.ii12.core;

import by.bstu.feis.ii12.core.model.BiGram;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Ignore
public class KNeighborPolarityDetectorTest {

    private KNeighborPolarityDetector polarityDetector;
    private ObjectMapper mapper;

    private static String POSITIVE_TEXT = "Bromwell High is a cartoon comedy. It ran at the same time as some other programs about school life, such as \"Teachers\". My 35 years in the teaching profession lead me to believe that Bromwell High's satire is much closer to reality than is \"Teachers\". The scramble to survive financially, the insightful students who can see right through their pathetic teachers' pomp, the pettiness of the whole situation, all remind me of the schools I knew and their students. When I saw the episode in which a student repeatedly tried to burn down the school, I immediately recalled ......... at .......... High. A classic line: INSPECTOR: I'm here to sack one of your teachers. STUDENT: Welcome to Bromwell High. I expect that many adults of my age think that Bromwell High is far fetched. What a pity that it isn't!";
    private static String NEGATIVE_TEXT = "Story of a man who has unnatural feelings for a pig. Starts out with a opening scene that is a terrific example of absurd comedy. A formal orchestra audience is turned into an insane, violent mob by the crazy chantings of it's singers. Unfortunately it stays absurd the WHOLE time with no general narrative eventually making it just too off putting. Even those from the era should be turned off. The cryptic dialogue would make Shakespeare seem easy to a third grader. On a technical level it's better than you might think with some good cinematography by future great Vilmos Zsigmond. Future stars Sally Kirkland and Frederic Forrest can be seen briefly.";

    @Before
    public void init() {
        polarityDetector = new KNeighborPolarityDetector(15);
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void createIndex() throws Exception {
//        TypeReference<HashMap<String, Integer>> typeRef = new TypeReference<HashMap<String, Integer>>() {};
//        //Map<String, Integer> negWords = mapper.readValue(new File("trainNegWordCount.json"), typeRef);
//        Map<String, Integer> posWords = mapper.readValue(new File("trainWordCount.json"), typeRef);
//
//        HashMap<String, HashMap<String, Integer>> defMap = new HashMap<>(90000);
//        HashMap<String, HashMap<String, Double>> normalizedMap = new HashMap<>();
//
//        File negFolder = new File("aclImdb/train/neg");
//        File posFolder = new File("aclImdb/train/pos");
//
//        System.out.println("counting");
//
//        int c = 0;
//
//        for (File file : posFolder.listFiles()) {
//            String text = cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath()))));
//            String[] words = text.split("\\s");
//
//            for (int i = 0; i < words.length - 1; i++) {
//                BiGram biGram = new BiGram(words[i], words[i + 1], 1d);
//
//                if (defMap.get(words[i]) != null) {
//                    defMap.get(words[i]).merge(words[i + 1], 1, (a, b) -> a + b);
//                } else {
//                    HashMap<String, Integer> innerMap = new HashMap<>();
//                    innerMap.put(words[i + 1], 1);
//                    defMap.put(words[i], innerMap);
//                }
//            }
//            System.out.println(c++);
//        }
//
//        c = 0;
//
//        for (File file : negFolder.listFiles()) {
//            String text = cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath()))));
//            String[] words = text.split("\\s");
//
//            for (int i = 0; i < words.length - 1; i++) {
//                BiGram biGram = new BiGram(words[i], words[i + 1], 1d);
//
//                if (defMap.get(words[i]) != null) {
//                    defMap.get(words[i]).merge(words[i + 1], 1, (a, b) -> a + b);
//                } else {
//                    HashMap<String, Integer> innerMap = new HashMap<>();
//                    innerMap.put(words[i + 1], 1);
//                    defMap.put(words[i], innerMap);
//                }
//            }
//            System.out.println(c++);
//        }
//
//        System.out.println("normalize");
//        c = 0;
//
//        defMap.keySet().forEach(word1 -> {
//            defMap.get(word1).keySet().forEach(word2 -> {
//                if (posWords.get(word1) != null && posWords.get(word2) != null) {
//                    if (normalizedMap.get(word1) != null) {
//                        normalizedMap.get(word1).put(word2, (double) defMap.get(word1).get(word2) / (double) posWords.get(word1));
//                    } else {
//                        HashMap<String, Double> innerMap = new HashMap<>();
//                        innerMap.put(word2, (double) defMap.get(word1).get(word2) / (double) posWords.get(word1));
//
//                        normalizedMap.put(word1, innerMap);
//                    }
//                }
//            });
//        });
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        mapper.writeValue(out, normalizedMap);
//        byte[] data = out.toByteArray();
//
//        FileOutputStream fos = new FileOutputStream("normalized.json");
//        fos.write(data);
//        fos.close();
//
//        System.out.println("deb");

        TypeReference<HashMap<String, HashMap<String, Double>>> typeRef = new TypeReference<HashMap<String, HashMap<String, Double>>>() {};

        HashMap<String, HashMap<String, Double>> normalizedNeg = mapper.readValue(new File("normalized.json"), typeRef);
        //HashMap<String, HashMap<String, Double>> normalizedPos = mapper.readValue(new File("normalizedPositive.json"), typeRef);

        File negFolder = new File("aclImdb/train/neg");
        File posFolder = new File("aclImdb/train/pos");

        List<KNeighborPolarityDetector.NGram> bGrams = new ArrayList<>(25000);

        for (File file : negFolder.listFiles()) {
            List<BiGram> biGrams = new ArrayList<>();

            String text = cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath()))));
            String[] words = text.split("\\s");

            for (int i = 0; i < words.length - 1; i++) {
                if (normalizedNeg.get(words[i]) != null) {
                    if (normalizedNeg.get(words[i]).get(words[i + 1]) != null) {
                        biGrams.add(new BiGram(words[i], words[i + 1], normalizedNeg.get(words[i]).get(words[i + 1])));
                    }
                }
            }

            bGrams.add(new KNeighborPolarityDetector.NGram(false, biGrams));
        }

        for (File file : posFolder.listFiles()) {
            List<BiGram> biGrams = new ArrayList<>();

            String text = cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath()))));
            String[] words = text.split("\\s");

            for (int i = 0; i < words.length - 1; i++) {
                if (normalizedNeg.get(words[i]) != null) {
                    if (normalizedNeg.get(words[i]).get(words[i + 1]) != null) {
                        biGrams.add(new BiGram(words[i], words[i + 1], normalizedNeg.get(words[i]).get(words[i + 1])));
                    }
                }
            }

            bGrams.add(new KNeighborPolarityDetector.NGram(true, biGrams));
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        mapper.writeValue(out, bGrams);
        byte[] data = out.toByteArray();

        FileOutputStream fos = new FileOutputStream("bGramsIndex2.json");
        fos.write(data);
        fos.close();

        System.out.println("deb");
    }

    @Test
    public void fullTest() throws Exception {
        File negFolder = new File("aclImdb/train/neg");
        File posFolder = new File("aclImdb/train/pos");

        System.out.println("kek");

        List<String> neg = new ArrayList<>(12500);
        List<String> pos = new ArrayList<>(12500);

        for (File file : negFolder.listFiles()) {
            neg.add(cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath())))));
        }

        for (File file : posFolder.listFiles()) {
            pos.add(cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath())))));
        }

        int rightNeg = 0;
        int rightPos = 0;

        int dotest = 100;

        System.out.println("NEGATIVE TEST");

        for (int i = 0; i < dotest; i++) {
            String str = neg.get(i);

            if (!polarityDetector.isPositive(str)) rightNeg++;
            System.out.println("test. r: " + rightNeg);
        }

        System.out.println("POSITIVE TEST");

        for (int i = 0; i < dotest; i++) {
            String str = pos.get(i);

            if (polarityDetector.isPositive(str)) rightPos++;
            System.out.println("test. r: " + rightPos);
        }

        System.out.println("NEG: " + rightNeg + " / " + dotest);
        System.out.println("POS: " + rightPos + " / " + dotest);
    }

    @Test
    public void test() {
        System.out.println(polarityDetector.isPositive(NEGATIVE_TEXT));
    }

    @Test
    public void testDetector() throws Exception {
//        int c = 0;
//        File negFolder = new File("aclImdb/train/neg");
//        File posFolder = new File("aclImdb/train/pos");
//
//        List<String> neg = new ArrayList<>();
//        List<String> pos = new ArrayList<>();
//        List<String> vocab = new ArrayList<>(Files.readAllLines(Paths.get("aclImdb/imdb.vocab")));
//
//        for (File file : negFolder.listFiles()) {
//            neg.add(cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath())))));
//        }
//
//        for (File file : posFolder.listFiles()) {
//            pos.add(cleanText(String.join("\n", Files.readAllLines(Paths.get(file.getAbsolutePath())))));
//        }
//
//        List<KNeighborPolarityDetector.NGram> nGrams = new ArrayList<>();
//
//        for (String txt : neg) {
//            String[] words = txt.split("\\s");
//            Map<String, Integer> nGram = new HashMap<>();
//
//            for (String word : words) {
//                int index = vocab.indexOf(word);
//
//                if (index != -1) {
//                    nGram.merge(word, 1, (a, b) -> a + b);
//                }
//            }
//
//            nGrams.add(new KNeighborPolarityDetector.NGram(false, nGram));
//            System.out.println(c++);
//        }
//
//        c = 0;
//
//        for (String txt : pos) {
//            String[] words = txt.split("\\s");
//            Map<String, Integer> nGram = new HashMap<>();
//
//            for (String word : words) {
//                int index = vocab.indexOf(word);
//
//                if (index != -1) {
//                    nGram.merge(word, 1, (a, b) -> a + b);
//                }
//            }
//
//            nGrams.add(new KNeighborPolarityDetector.NGram(true, nGram));
//            System.out.println(c++);
//        }
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        mapper.writeValue(out, nGrams);
//        byte[] data = out.toByteArray();
//
//        FileOutputStream fos = new FileOutputStream("data_ngm3.json");
//        fos.write(data);
//        fos.close();

        System.out.println("kek");
    }

    private static String cleanText(String text) {
        return text
                .replaceAll("(<\\w+ \\/>)", " ")
                .replaceAll("[\\W\\d\\s&&[^']]", " ")
                .replaceAll(" +", " ").toLowerCase();
    }

}
