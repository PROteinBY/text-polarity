package by.bstu.feis.ii12.core;

import org.springframework.stereotype.Component;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class SvmPolarityDetector implements TextPolarityDetector{

    private String MODEL_NAME = "model.pb";
    private ClassLoader classLoader = getClass().getClassLoader();

    private static String cleanText(String text) {
        return text
                .replaceAll("(<\\w+ \\/>)", " ")
                .replaceAll("[\\W\\d\\s&&[^']]", " ")
                .replaceAll(" +", " ")
                .toLowerCase();
    }

    private Tensor<Float> createInputVector(String text, String vocabPath) throws IOException {

        List<String> vocab = Files.readAllLines(Paths.get(classLoader.getResource(vocabPath).getFile()));

        float[] input = new float[vocab.size()];

        String[] words = cleanText(text).split(" ");

        for(String word : words){
            int index = vocab.indexOf(word);
            if(index != -1){
                input[index] += 1;
            }
        }

        long[] shape = {1, vocab.size()};

        ByteBuffer buf = ByteBuffer.allocateDirect(input.length * 4);
        buf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = buf.asFloatBuffer();
        buffer.put(input);
        buffer.position(0);

        return Tensor.create(shape, FloatBuffer.wrap(input));
    }

    @Override
    public boolean isPositive(String text) {

        byte[] graphDef = readAllBytesOrExit(Paths.get(classLoader.getResource(MODEL_NAME).getFile()));

        try (Graph g = new Graph()) {
            g.importGraphDef(graphDef);
            Session s = new Session(g);

            Tensor result = s.runner().feed("x:0", createInputVector(text,"imdb.vocab")).fetch("y_pred:0").run().get(0);
            final long[] rshape = result.shape();
            int nlabels = (int) rshape[1];

            float[][] transformedOutput = new float[1][nlabels];
            result.copyTo(transformedOutput);


            boolean output = transformedOutput[0][0] == 1.;
            s.close();
            return output;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static byte[] readAllBytesOrExit(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
