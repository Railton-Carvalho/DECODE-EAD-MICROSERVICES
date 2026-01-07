package com.ead.authuser.controllers;

import com.ead.authuser.clients.UserClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.models.UserModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserCourseController {

    @Autowired
    UserClient userClient;


    @GetMapping("/users/{userId}/courses")
    ResponseEntity<Page<CourseDto>> getAllCoursesByUser(@PageableDefault(sort = "courseId", direction = Sort.Direction.ASC, size = 10)
                                        Pageable pageable,
                                                        @PathVariable("userId") UUID userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userClient.getAllCoursesByUsers(userId, pageable));


    }
}
