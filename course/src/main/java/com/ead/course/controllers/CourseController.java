package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(("/courses"))
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(direction = Sort.Direction.ASC,sort = "courseId") Pageable pageable,
                                                           @RequestParam(required = false) UUID userId) {
        Page<CourseModel> allCourses;

        if(userId != null) {
                allCourses = courseService.findAll(SpecificationTemplate.userCourseId(userId).and(spec), pageable);
        }else{
            allCourses = courseService.findAll(spec, pageable);
        }

        return ResponseEntity.status(HttpStatus.OK).body(allCourses);
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@RequestParam UUID courseId) {
        var course = courseService.findById(courseId);
        if (course.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(course.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID courseId) {
        Optional<CourseModel> courseModel = courseService.findById(courseId);
        if (courseModel.isPresent()) {
            courseService.delete(courseModel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Course Deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");

    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseDto courseDto) {
        Optional<CourseModel> courseModel = courseService.findById(courseId);
        if (courseModel.isPresent()) {
            BeanUtils.copyProperties(courseDto, courseModel.get());
            courseModel.get().setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
    }

}
