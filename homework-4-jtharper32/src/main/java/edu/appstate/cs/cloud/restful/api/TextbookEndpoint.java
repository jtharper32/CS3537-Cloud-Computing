package edu.appstate.cs.cloud.restful.api;

import com.google.cloud.datastore.Key;
import edu.appstate.cs.cloud.restful.datastore.TextbookService;
import edu.appstate.cs.cloud.restful.models.Textbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/textbooks")
public class TextbookEndpoint {
    @Autowired
    private TextbookService textbookService;

    @GetMapping
    public List<Textbook> getAllTextbooks(@RequestParam("subject") Optional<String> subject) {
        return subject
            .map(textbookService::getAllTextbooksForSubject)
            .orElseGet(textbookService::getAllTextbooks);
    }

    @GetMapping("/{textbookId}")
    public Textbook getTextbook(@PathVariable long textbookId) {
        return textbookService.getTextbook(textbookId);
    }

    @PostMapping
    public Textbook createTextbook(@RequestBody Textbook textbook) {
        Key key = textbookService.createTextbook(textbook);
        textbook.setId(key.getId());
        return textbook;
    }

    @PutMapping("/{textbookId}")
    public Textbook updateTextbook(@RequestBody Textbook textbook, @PathVariable long textbookId) {
        textbook.setId(textbookId);
        textbookService.updateTextbook(textbook);
        return textbook;
    }

    @DeleteMapping("/{textbookId}")
    public void deleteTextbook(@PathVariable long textbookId) {
        textbookService.deleteTextbook(textbookId);
    }

    @GetMapping("/init")
    public boolean initTextbooks() {
        // (sample‑data initializer unchanged)
        return textbookService.initSampleData();
    }
}
