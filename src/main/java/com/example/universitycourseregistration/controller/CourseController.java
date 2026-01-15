package com.example.universitycourseregistration.controller;

import com.example.universitycourseregistration.entity.Course;
import com.example.universitycourseregistration.entity.Student;
import com.example.universitycourseregistration.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.save(course));
    }

    // GET: Kursu tap
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET: Bütün kurslar (duplicate-i qaldırdıq)
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    // POST: Müəllim təyin et
    @PostMapping("/{courseId}/teacher/{teacherId}")
    public ResponseEntity<String> assignTeacher(
            @PathVariable Long courseId,
            @PathVariable Long teacherId) {
        courseService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok("Müəllim fənnə təyin olundu");
    }

    // GET: Kursda qeydiyyat olan tələbələr
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getEnrolledStudents(@PathVariable Long id) {
        List<Student> students = courseService.getEnrolledStudents(id);
        return ResponseEntity.ok(students);
    }

    // GET: Müəllimin kursları
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    // PUT: Kursu yenilə
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updated) {
        return ResponseEntity.ok(courseService.update(id, updated));
    }

    // DELETE: Kursu sil
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok("Fənn silindi");
    }

    // POST: Kurs xalı ver
    @PostMapping("/{id}/rate")
    public ResponseEntity<Course> rateCourse(
            @PathVariable Long id,
            @RequestParam double rating) {

        return ResponseEntity.ok(courseService.rate(id, rating));
    }


    // GET: Populyar kurslar
    @GetMapping("/popular")
    public ResponseEntity<List<Course>> getPopularCourses() {
        return ResponseEntity.ok(courseService.getPopularCourses());
    }

}