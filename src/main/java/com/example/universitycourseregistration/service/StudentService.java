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
        return studentRepository.save(student);
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional
    public void addCourseToStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Tələbə tapılmadı"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı"));

        if (student.getCourses().contains(course)) {
            throw new RuntimeException("Bu fənn artıq seçilib");
        }

        student.getCourses().add(course);
        // course.getStudents().add(student); → mappedBy olduğundan buna ehtiyac yoxdur
    }

    public List<Course> getCoursesOfStudent(Long studentId) {
        return studentRepository.findCoursesByStudentId(studentId);
    }
}