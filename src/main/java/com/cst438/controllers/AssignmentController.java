package com.cst438.controllers;

import java.sql.Date;

import com.cst438.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class AssignmentController {
    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    CourseRepository courseRepository;

    @PostMapping("/assignment/{name}/{date}")
    public void addAssignment(@PathVariable("name") String name, @PathVariable("date") Date date){
        Assignment assignment = new Assignment();
        Course course = courseRepository.findById(123456).orElse(null);

        if(course == null){
            return;
        }

        assignment.setCourse(course);
        assignment.setName(name);
        assignment.setDueDate(date);
        assignment.setNeedsGrading(0);

        assignmentRepository.save(assignment);
    }

    @PutMapping("/assignment/{name}/{id}")
    public void updateAssignment(@PathVariable("name") String name, @PathVariable("id") int id){
        Assignment assignment = assignmentRepository.findById(id).orElse(null);
        if(assignment == null){
            return;
        }

        assignment.setName(name);

        assignmentRepository.save(assignment);
    }

    @DeleteMapping("/assignment/{id}")
    public void deleteAssignment(@PathVariable("id") int id){
        Assignment assignment = assignmentRepository.findById(id).orElse(null);
        if(assignment == null){
            return;
        }

        if(assignment.getNeedsGrading() != 0){
            return;
        }

        assignmentRepository.delete(assignment);
    }

}
