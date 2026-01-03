package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ModuleService moduleService;

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessons(@PathVariable UUID moduleId) {
        List<LessonModel> allModules = moduleService.findAllByModule(moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(allModules);
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable UUID moduleId,
                                               @RequestParam UUID lessonId) {
        Optional<LessonModel> lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModel.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(lessonModel.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable UUID moduleId, @RequestBody @Valid LessonDto lessonDto) {
        Optional<ModuleModel> moduleModel = moduleService.findById(moduleId);
        if (moduleModel.isPresent()) {
            var lessonModel = new LessonModel();
            BeanUtils.copyProperties(lessonDto, lessonModel);
            lessonModel.setModule(moduleModel.get());
            lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
            return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable UUID moduleId,
                                               @PathVariable UUID lessonId) {
        Optional<LessonModel> lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModel.isPresent()) {
            lessonService.delete(lessonModel.get());
            return ResponseEntity.status(HttpStatus.OK).body("Lesson Deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateModule(@PathVariable UUID moduleId,
                                               @PathVariable UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto) {
        Optional<LessonModel> lessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModel.isPresent()) {
            BeanUtils.copyProperties(lessonDto, lessonModel.get());
            return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
    }
}
