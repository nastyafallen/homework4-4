package ru.hogwarts.school.homework43.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.homework43.model.Faculty;
import ru.hogwarts.school.homework43.model.Student;
import ru.hogwarts.school.homework43.service.StudentService;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RequestMapping("/student")
@RestController
@Validated
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(student));
    }

    @GetMapping("/find")
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        return  ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(studentService.updateStudent(student));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> getStudentsByAge(@RequestParam("age") int age) {
        if (age < 0) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping
    public ResponseEntity<List<Student>> findByAgeBetween(@RequestParam ("age1")int min, @RequestParam ("age2")int max) {
        if (min < 0 || max < 0) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFacultyById(@PathVariable Long id) {
        return studentService.getFacultyById(id);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countStudents() {
        return ResponseEntity.ok(studentService.countStudents());
    }

    @GetMapping("/averageAge")
    public ResponseEntity<Double> averageAge() {
        return ResponseEntity.ok(studentService.averageAge());
    }

    @GetMapping("/lastStudents")
    public ResponseEntity<List<Student>> getLastStudents(@RequestParam("number") @Min(1) @Max(10) int number) {
        return ResponseEntity.ok(studentService.getLastStudents(number));
    }
}
