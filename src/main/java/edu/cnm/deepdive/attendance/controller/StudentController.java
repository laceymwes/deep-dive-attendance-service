package edu.cnm.deepdive.attendance.controller;

import edu.cnm.deepdive.attendance.model.dao.StudentRepository;
import edu.cnm.deepdive.attendance.model.entity.Student;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@ExposesResourceFor(Student.class)
@RequestMapping("/students") // base URL for this controller
public class StudentController {

  private StudentRepository studentRepository;

  @Autowired
  public StudentController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @GetMapping
  public Iterable<Student> list() {
    return studentRepository.findAllByOrderByLastNameAscFirstNameAsc();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Student> post(@RequestBody Student student) {
    studentRepository.save(student);
    // generates response with status code 201 (resource created)
    return ResponseEntity.created(student.getHref()).body(student);
  }

  @GetMapping("{studentId}")
  public Student get(@PathVariable("studentId") long studentId) {
    // @Id specified in Student Entity class
    return studentRepository.findById(studentId).get(); // TODO EXPLAIN THIS!
  }

  @PatchMapping(value = "{studentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Student patch(@PathVariable("studentId") long studentId, @RequestBody Student update) {
    Student student = studentRepository.findById(studentId).get();
    student.patch(update);
    return studentRepository.save(student);
  }

  @DeleteMapping("{studentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("studentId") long studentId) {
    studentRepository.delete(studentRepository.findById(studentId).get());
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
  @ExceptionHandler(NoSuchElementException.class)
  public void notFound(){
  }
}

