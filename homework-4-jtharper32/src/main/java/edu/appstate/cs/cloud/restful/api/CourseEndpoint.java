package edu.appstate.cs.cloud.restful.api;

import com.google.cloud.datastore.Key;
import edu.appstate.cs.cloud.restful.datastore.CourseService;
import edu.appstate.cs.cloud.restful.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseEndpoint {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    public Course getCourse(@PathVariable long courseId) {
        return courseService.getCourse(courseId);
    }

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        Key key = courseService.createCourse(course);
        course.setId(key.getId());
        return course;
    }

    @PutMapping("/{courseId}")
    public Course updateCourse(@RequestBody Course course, @PathVariable long courseId) {
        course.setId(courseId);
        courseService.updateCourse(course);
        return course;
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable long courseId) {
        courseService.deleteCourse(courseId);
    }

    @GetMapping("/init")
    public boolean initCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course.Builder().withCourseName("Operating Systems").withSubject("Computer Science").build());
        courses.add(new Course.Builder().withCourseName("Programming Languages").withSubject("Computer Science").build());
        courses.add(new Course.Builder().withCourseName("Statistics").withSubject("Mathematics").build());
        courses.add(new Course.Builder().withCourseName("English Composition").withSubject("English").build());
        courses.add(new Course.Builder().withCourseName("Poems and Sonnets").withSubject("English").build());
        for (Course c : courses) {
            Key key = courseService.createCourse(c);
            c.setId(key.getId());
        }
        return true;
    }
}
