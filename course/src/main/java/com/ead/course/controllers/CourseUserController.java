package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    CourseService courseService;


    @GetMapping("/courses/{courseId}/users")
    ResponseEntity<Object> getAllUsersByCourse(@PageableDefault(sort = "userId", direction = Sort.Direction.ASC, size = 10)
                                                        Pageable pageable,
                                                      @PathVariable("courseId") UUID courseId) {

        return ResponseEntity.status(HttpStatus.OK).body("");
    }
    @Async
    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable("courseId") UUID courseId,
                                                               @RequestBody @Valid SubscriptionDto subscriptionDto){
        Optional<CourseModel> courseModel = courseService.findById(courseId);
        if (courseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        }
        //TO DO: Verificações state transfer
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
    }

}
