package edu.appstate.cs.cloud.restful.api;

import com.google.cloud.datastore.Key;
import edu.appstate.cs.cloud.restful.datastore.SubjectService;
import edu.appstate.cs.cloud.restful.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/subjects")
public class SubjectEndpoint {
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping(value = "{subjectId}")
    public Subject getSubject(@PathVariable long subjectId) {
        // TODO: Add code to get a specific subject here

        // TODO: Remove this once you can return a real object
        return null;
    }

    @DeleteMapping(value = "{subjectId}")
    public void deleteSubject(@PathVariable long subjectId) {
        // TODO: Add code to delete a specific subject here
    }

    @PostMapping
    public Subject addSubject(@RequestBody Subject subject) {
        // TODO: Add code to add a new subject here

        // TODO: Remove this once you can return a real object
        return null;
    }

    @PutMapping(value = "{subjectId}")
    public Subject updateSubject(@RequestBody Subject subject, @PathVariable long subjectId) {
        // TODO: Add code to update a specific subject here

        // TODO: Remove this once you can return a real object
        return null;
    }

    @GetMapping(value = "/init")
    public boolean initSubjects() {
        // Create some sample subjects
        List<Subject> subjects = new ArrayList<>();

        subjects.add(new Subject.Builder().withSubjectName("Computer Science").build());
        subjects.add(new Subject.Builder().withSubjectName("Mathematics").build());
        subjects.add(new Subject.Builder().withSubjectName("English").build());
        subjects.add(new Subject.Builder().withSubjectName("History").build());

        for (Subject s : subjects) {
            Key key = subjectService.createSubject(s);
            s.setId(key.getId());
        }

        return true;
    }
}
