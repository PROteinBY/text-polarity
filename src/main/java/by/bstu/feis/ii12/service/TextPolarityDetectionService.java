package by.bstu.feis.ii12.service;

import by.bstu.feis.ii12.core.TextPolarityDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextPolarityDetectionService {

    @Autowired
    private List<TextPolarityDetector> detectors;

    public boolean getTextPolarity(String text) {
        Integer results =
                detectors
                        .stream()
                        .mapToInt(detector -> detector.isPositive(text) ? 1 : 0)
                        .sum();

        return results > detectors.size() / 2;
    }
}
