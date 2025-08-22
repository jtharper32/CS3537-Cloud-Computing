package edu.appstate.cs.cloud.restful.api;

import com.google.cloud.datastore.Key;
import edu.appstate.cs.cloud.restful.datastore.SubjectService;
import edu.appstate.cs.cloud.restful.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectEndpoint {
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{subjectId}")
    public Subject getSubject(@PathVariable long subjectId) {
        return subjectService.getSubject(subjectId);
    }

    @PostMapping
    public Subject addSubject(@RequestBody Subject subject) {
        Key key = subjectService.createSubject(subject);
        subject.setId(key.getId());
        return subject;
    }

    @PutMapping("/{subjectId}")
    public Subject updateSubject(@RequestBody Subject subject, @PathVariable long subjectId) {
        subject.setId(subjectId);
        subjectService.updateSubject(subject);
        return subject;
    }

    @DeleteMapping("/{subjectId}")
    public void deleteSubject(@PathVariable long subjectId) {
        subjectService.deleteSubject(subjectId);
    }

    @GetMapping("/init")
    public boolean initSubjects() {
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
