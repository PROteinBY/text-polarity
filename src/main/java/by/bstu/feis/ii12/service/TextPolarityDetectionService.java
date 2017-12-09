package by.bstu.feis.ii12.service;

import by.bstu.feis.ii12.core.TextPolarityDetector;

import java.util.ArrayList;
import java.util.List;

public class TextPolarityDetectionService {

    List<TextPolarityDetector> detectors = new ArrayList<TextPolarityDetector>();

    public boolean getTextPolarity(String text) {
         Integer results =
                detectors
                    .stream()
                    .mapToInt(detector -> {
                        if (detector.isPositive(text)) return 1;
                        else return 0;
                    })
                    .sum();

        return results > detectors.size() / 2;
    }
}
