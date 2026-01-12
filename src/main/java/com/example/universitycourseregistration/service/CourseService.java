package com.example.universitycourseregistration.service;

import com.example.universitycourseregistration.entity.Course;
import com.example.universitycourseregistration.entity.Student;
import com.example.universitycourseregistration.entity.Teacher;
import com.example.universitycourseregistration.repository.CourseRepository;
import com.example.universitycourseregistration.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Transactional
    public void assignTeacherToCourse(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Müəllim tapılmadı"));

        course.setTeacher(teacher);
        // teacher.getCourses().add(course); → mappedBy olduğundan buna ehtiyac yoxdur
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Student> getEnrolledStudents(Long courseId) {
        return courseRepository.findById(courseId)
                .map(course -> course.getStudents().stream().toList())
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı"));
    }
}
