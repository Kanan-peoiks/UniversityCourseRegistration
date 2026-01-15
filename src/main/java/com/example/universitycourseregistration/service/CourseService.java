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

    public CourseService(CourseRepository courseRepository,
                         TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    // YALNIZ YENİ COURSE ÜÇÜN
    public Course save(Course course) {
        if (course.getId() == null && courseRepository.existsByName(course.getName())) {
            throw new RuntimeException("Bu adda fənn artıq mövcuddur");
        }
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
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Student> getEnrolledStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı"));
        return course.getStudents().stream().toList();
    }

    public Course update(Long id, Course updatedCourse) {
        Course course = findById(id)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı"));

        if (!course.getName().equals(updatedCourse.getName())
                && courseRepository.existsByName(updatedCourse.getName())) {
            throw new RuntimeException("Bu adda fənn artıq mövcuddur");
        }

        course.setName(updatedCourse.getName());
        course.setCredits(updatedCourse.getCredits());
        course.setRating(updatedCourse.getRating());

        return courseRepository.save(course); // ✅ DÜZGÜN
    }

    @Transactional
    public void delete(Long id) {
        Course course = findById(id)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı"));

        course.getStudents().forEach(s -> s.getCourses().remove(course));
        course.setTeacher(null);

        courseRepository.delete(course);
    }

    @Transactional
    public Course rate(Long id, double newRating) {
        if (newRating < 0 || newRating > 5) {
            throw new RuntimeException("Xal 0-5 arası olmalıdır");
        }

        Course course = findById(id)
                .orElseThrow(() -> new RuntimeException("Fənn tapılmadı"));

        course.setRating(newRating);
        return courseRepository.save(course);
    }

    public List<Course> getPopularCourses() {
        return courseRepository.findAllByPopularity();
    }
}
