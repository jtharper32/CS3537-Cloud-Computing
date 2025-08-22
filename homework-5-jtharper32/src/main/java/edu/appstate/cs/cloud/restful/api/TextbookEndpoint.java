package edu.appstate.cs.cloud.restful.api;

import com.google.cloud.datastore.Key;
import edu.appstate.cs.cloud.restful.datastore.TextbookService;
import edu.appstate.cs.cloud.restful.models.Textbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/textbooks")
public class TextbookEndpoint {
    @Autowired
    private TextbookService textbookService;

    @GetMapping
    public List<Textbook> getAllTextbooks(@RequestParam("subject") Optional<String> subject) {
        if (subject.isPresent()) {
            return textbookService.getAllTextbooksForSubject(subject.get());
        }
        return textbookService.getAllTextbooks();
    }

    @GetMapping(value = "{textbookId}")
    public Textbook getTextbook(@PathVariable long textbookId) {
        // TODO: Add code to get a specific textbook here

        // TODO: Remove this once you can return a real object
        return null;
    }

    @DeleteMapping(value = "{textbookId}")
    public void deleteTextbook(@PathVariable long textbookId) {
        // TODO: Add code to delete a specific textbook here
    }

    @PostMapping
    public Textbook createTextbook(@RequestBody Textbook textbook) {
        // TODO: Add code to add a new textbook here

        // TODO: Remove this once you can return a real object
        return null;
    }

    @PutMapping(value = "{textbookId}")
    public Textbook updateTextbook(@RequestBody Textbook textbook, @PathVariable long textbookId) {
        // TODO: Add code to update a specific textbook here

        // TODO: Remove this once you can return a real object
        return null;
    }

    @GetMapping(value = "/init")
    public boolean initTextbooks() {
        // Create some sample textbooks
        List<Textbook> textbooks = new ArrayList<>();

        textbooks.add(new Textbook.Builder()
                .withTitle("Operating System Concepts")
                .withAuthors("Abraham Silberschatz, Greg Gagne, Peter B. Galvin")
                .withSubject("Computer Science")
                .withPublisher("Wiley")
                .withYear(2018)
                .withImageLink("https://storage.googleapis.com/cloud-spring-2025-hills-hw3/book-cover-001.jpeg")
                .build());

        textbooks.add(new Textbook.Builder()
                .withTitle("Programming Language Pragmatics")
                .withAuthors("Michael Scott")
                .withSubject("Computer Science")
                .withPublisher("Elsevier")
                .withYear(2015)
                .withImageLink("https://storage.googleapis.com/cloud-spring-2025-hills-hw3/book-cover-002.jpeg")
                .build());

        textbooks.add(new Textbook.Builder()
                .withTitle("Concepts of Programming Languages")
                .withAuthors("Robert W. Sebesta")
                .withSubject("Computer Science")
                .withPublisher("Pearson")
                .withYear(2016)
                .withImageLink("https://storage.googleapis.com/cloud-spring-2025-hills-hw3/book-cover-003.jpeg")
                .build());

        textbooks.add(new Textbook.Builder()
                .withTitle("An Introduction to Statistical Learning: with Applications in R")
                .withAuthors("Gareth James, Daniela Witten, Trevor Hastie, and Robert Tibshirani")
                .withSubject("Mathematics")
                .withPublisher("Springer")
                .withYear(2017)
                .withImageLink("https://storage.googleapis.com/cloud-spring-2025-hills-hw3/book-cover-004.jpeg")
                .build());

        for (Textbook t : textbooks) {
            Key key = textbookService.createTextbook(t);
            t.setId(key.getId());
        }

        return true;
    }
}
