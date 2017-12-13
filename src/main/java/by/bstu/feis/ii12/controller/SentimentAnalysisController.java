package by.bstu.feis.ii12.controller;

import by.bstu.feis.ii12.service.TextPolarityDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SentimentAnalysisController {

    @Autowired
    private TextPolarityDetectionService textPolarityDetectionService;

    @GetMapping("/is")
    public boolean getResult(@RequestParam("text") String text) {
        return textPolarityDetectionService.getTextPolarity(text);
    }
}
