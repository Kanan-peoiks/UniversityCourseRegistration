package com.example.universitycourseregistration.service;

import com.example.universitycourseregistration.entity.Course;
import com.example.universitycourseregistration.entity.Student;
import com.example.universitycourseregistration.repository.CourseRepository;
import com.example.universitycourseregistration.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Student save(Student student) {
        // Email unique yoxlaması (istədiyin kimi)
        if (student.getId() == null && studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Bu adda email artıq mövcuddur");
        }
        return studentRepository.save(student);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public void addCourseToStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Tələbə tapılmadı (ID: " + studentId + ")"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı (ID: " + courseId + ")"));

        if (student.getCourses().contains(course)) {
            throw new RuntimeException("Bu fənn artıq seçilib");
        }

        student.getCourses().add(course);
        studentRepository.save(student);
    }

    @Transactional
    public void removeCourseFromStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Tələbə tapılmadı (ID: " + studentId + ")"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı (ID: " + courseId + ")"));

        if (!student.getCourses().contains(course)) {
            throw new RuntimeException("Bu fənn tələbədə yoxdur");
        }

        student.getCourses().remove(course);
        course.getStudents().remove(student);
        studentRepository.save(student);
    }

    public List<Course> getCoursesOfStudent(Long studentId) {
        return studentRepository.findCoursesByStudentId(studentId);
    }

    // DELETE tələbə (yeni əlavə - deleteById)
    public void deleteById(Long id) {
        Student student = findById(id)
                .orElseThrow(() -> new RuntimeException("Tələbə tapılmadı"));

        // Kurslardan ayır (ManyToMany əlaqəsini qır)
        student.getCourses().forEach(course -> course.getStudents().remove(student));

        studentRepository.deleteById(id);
    }


}