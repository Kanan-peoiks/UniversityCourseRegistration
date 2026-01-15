package com.example.universitycourseregistration.service;

import com.example.universitycourseregistration.entity.Teacher;
import com.example.universitycourseregistration.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public void delete(Long id) {
        Teacher teacher = findById(id)
                .orElseThrow(() -> new RuntimeException("Müəllim tapılmadı"));
        teacher.getCourses().forEach(course -> course.setTeacher(null));
        teacherRepository.delete(teacher);
    }
}
