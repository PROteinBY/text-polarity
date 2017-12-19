package by.bstu.feis.ii12.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SvmPolarityDetectorTest {

    private static final String POSITIVE_TEXT = "I went and saw this movie last night after being coaxed to by a few friends of mine. I'll admit that I was reluctant to see it because from what I knew of Ashton Kutcher he was only able to do comedy. I was wrong. Kutcher played the character of Jake Fischer very well, and Kevin Costner played Ben Randall with such professionalism. The sign of a good movie is that it can toy with our emotions. This one did exactly that. The entire theater (which was sold out) was overcome by laughter during the first half of the movie, and were moved to tears during the second half. While exiting the theater I not only saw many women in tears, but many full grown men as well, trying desperately not to let anyone see them crying. This movie was great, and I suggest that you go see it before you judge.";
    private static final String NEGATIVE_TEXT = "Alas, another Costner movie that was an hour too long. Credible performances, but the script had no where to go and was in no hurry to get there. First we are offered an unrelated string of events few of which further the story. Will the script center on Randall and his wife? Randall and Fischer? How about Fischer and Thomas? In the end, no real front story ever develops and the characters themselves are artificially propped up by monologues from third parties. The singer explains Randall, Randall explains Fischer, on and on. Finally, long after you don't care anymore, you will learn something about the script meetings. Three endings were no doubt proffered and no one could make a decision. The end result? All three were used, one, after another, after another. If you can hang in past the 100th yawn, you'll be able to pick them out. Despite the transparent attempt to gain points with a dedication to the Coast Guard, this one should have washed out the very first day.";

    private TextPolarityDetector textPolarityDetector;

    @Before
    public void init() {
        textPolarityDetector = new SvmPolarityDetector();
    }

    @Test
    public void isPositive() {
        Assert.assertTrue(textPolarityDetector.isPositive(POSITIVE_TEXT));
        Assert.assertFalse(textPolarityDetector.isPositive(NEGATIVE_TEXT));
    }

}
