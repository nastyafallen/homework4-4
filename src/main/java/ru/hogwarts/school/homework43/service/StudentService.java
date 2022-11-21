package ru.hogwarts.school.homework43.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.homework43.exception.StudentNotFoundException;
import ru.hogwarts.school.homework43.model.Faculty;
import ru.hogwarts.school.homework43.model.Student;
import ru.hogwarts.school.homework43.repository.FacultyRepository;
import ru.hogwarts.school.homework43.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student){
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findStudentById(id)
                .orElseThrow(StudentNotFoundException::new);
    }

    public Student updateStudent(Student student) {
        Student oldStudent = studentRepository.findStudentById(student.getId())
                .orElseThrow(StudentNotFoundException::new);
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        return studentRepository.save(oldStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAge(int age) {
        return studentRepository.getStudentsByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyById(Long id) {
        Student student = studentRepository.findStudentById(id)
                .orElseThrow(StudentNotFoundException::new);
        return student.getFaculty();
    }

    public Integer countStudents() {
        return studentRepository.countStudents();
    }

    public Double averageAge() {
        return studentRepository.averageAge();
    }

    public List<Student> getLastStudents(int number) {
        return studentRepository.getLastStudents(number);
    }
}
