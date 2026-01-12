package com.example.universitycourseregistration.controller;

import com.example.universitycourseregistration.entity.Course;
import com.example.universitycourseregistration.entity.Student;
import com.example.universitycourseregistration.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.save(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<String> addCourseToStudent(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        studentService.addCourseToStudent(studentId, courseId);
        return ResponseEntity.ok("Fənn uğurla əlavə olundu");
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getStudentCourses(@PathVariable Long id) {
        List<Course> courses = studentService.getCoursesOfStudent(id);
        return ResponseEntity.ok(courses);
    }
}
