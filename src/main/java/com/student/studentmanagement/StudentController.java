package com.student.studentmanagement;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository repo;
    public StudentController(StudentRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Student> all() { return repo.findAll(); }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Student s) {
        if (repo.findByEmailIgnoreCase(s.getEmail()).isPresent())
            return ResponseEntity.status(409).body("This email already exists");
        return ResponseEntity.ok(repo.save(s));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Student s) {
        var existing = repo.findByEmailIgnoreCase(s.getEmail());
        if (existing.isPresent() && !existing.get().getId().equals(id))
            return ResponseEntity.status(409).body("This email already exists");
        s.setId(id);
        return ResponseEntity.ok(repo.save(s));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}