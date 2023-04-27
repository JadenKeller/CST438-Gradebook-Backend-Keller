package com.cst438.controllers;

import java.sql.Date;

import com.cst438.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class AssignmentController {
    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    CourseRepository courseRepository;


    @PostMapping("/assignment")
    @Transactional
    public AssignmentListDTO.AssignmentDTO newAssignment(@RequestBody AssignmentListDTO.AssignmentDTO dto) {
        String userEmail = "dwisneski@csumb.edu";
        // validate course and that the course instructor is the user
        Course c = courseRepository.findById(dto.courseId).orElse(null);
        if (c != null && c.getInstructor().equals(userEmail)) {
            // create and save new assignment
            // update and return dto with new assignment primary key
            Assignment a = new Assignment();
            a.setCourse(c);
            a.setName(dto.assignmentName);
            a.setDueDate(Date.valueOf(dto.dueDate));
            a.setNeedsGrading(1);
            a = assignmentRepository.save(a);
            dto.assignmentId=a.getId();
            return dto;

        } else {
            // invalid course
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid course id.");
        }
    }

//    @PostMapping("/assignment/{name}/{date}")
//    public void addAssignment(@PathVariable("name") String name, @PathVariable("date") Date date){
//        Assignment assignment = new Assignment();
//        Course course = courseRepository.findById(123456).orElse(null);
//
//        if(course == null){
//            return;
//        }
//
//        assignment.setCourse(course);
//        assignment.setName(name);
//        assignment.setDueDate(date);
//        assignment.setNeedsGrading(0);
//
//        assignmentRepository.save(assignment);
//    }

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
