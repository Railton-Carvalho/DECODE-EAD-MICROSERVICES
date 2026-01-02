package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable UUID courseId) {
        var allModules = moduleService.findAllByCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(allModules);
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable UUID courseId,
                                               @RequestParam UUID moduleId) {
        var moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (moduleModel.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(moduleModel.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable UUID courseId, @RequestBody @Valid ModuleDto moduleDto) {
        var courseModel = courseService.findById(courseId);
        if (courseModel.isPresent()) {
            var moduleModel = new ModuleModel();
            BeanUtils.copyProperties(moduleDto, moduleModel);
            moduleModel.setCourse(courseModel.get());
            moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable UUID courseId,
                                               @PathVariable UUID moduleId) {
        Optional<ModuleModel> moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (moduleModel.isPresent()) {
            moduleService.delete(moduleModel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Module Deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable UUID courseId,
                                               @PathVariable UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto) {
        Optional<ModuleModel> moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (moduleModel.isPresent()) {
            BeanUtils.copyProperties(moduleDto, moduleModel.get());
            return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
    }

}
