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

    // POST: Yeni tələbə yarat
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.save(student));
    }

    // GET: Bütün tələbələr
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    // GET: ID ilə tələbə tap
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Tələbəyə fənn əlavə et
    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<String> addCourseToStudent(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        studentService.addCourseToStudent(studentId, courseId);
        return ResponseEntity.ok("Fənn uğurla əlavə olundu");
    }

    // GET: Tələbənin kursları (JOIN)
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getStudentCourses(@PathVariable Long id) {
        List<Course> courses = studentService.getCoursesOfStudent(id);
        return ResponseEntity.ok(courses);
    }

    // PUT: Tələbə yenilə
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentService.findById(id)
                .map(existingStudent -> {
                    existingStudent.setFirstName(updatedStudent.getFirstName());
                    existingStudent.setLastName(updatedStudent.getLastName());
                    existingStudent.setEmail(updatedStudent.getEmail());
                    return ResponseEntity.ok(studentService.save(existingStudent));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Tələbə sil
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        return studentService.findById(id)
                .map(student -> {
                    studentService.deleteById(id);
                    return ResponseEntity.ok("Tələbə silindi");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: Tələbədən fənn sil
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<String> removeCourseFromStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.removeCourseFromStudent(studentId, courseId);
        return ResponseEntity.ok("Fənn silindi");
    }
}