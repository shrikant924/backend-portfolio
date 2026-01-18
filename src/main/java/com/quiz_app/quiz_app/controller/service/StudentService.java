package com.quiz_app.quiz_app.controller.service;

import com.quiz_app.quiz_app.controller.repo.StudentRepo;
import com.quiz_app.quiz_app.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    public ResponseEntity<List<Student>> findAll() {
        return new ResponseEntity<>(studentRepo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Student> findById(int id) {
        return new ResponseEntity<>(studentRepo.findById(id).get(),HttpStatus.OK);
    }

    public ResponseEntity<Student> findByName(String name) {
        return new ResponseEntity<>(studentRepo.findByName(name),HttpStatus.OK);
    }

    public ResponseEntity<String> save(Student student) {
        studentRepo.save(student);
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }
}
