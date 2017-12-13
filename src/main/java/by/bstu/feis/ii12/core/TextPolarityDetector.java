package by.bstu.feis.ii12.core;

public interface TextPolarityDetector {

    /**
     * Detect polarity of text
     * @param text input text
     * @return true if text is positive, false if not
     */
    boolean isPositive(String text);

}