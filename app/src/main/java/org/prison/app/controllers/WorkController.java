package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.WorkService;
import org.prison.model.prisoners.Work;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/pms/api")
public class WorkController {

    private final WorkService workService;

    @PostMapping("/departments/{deptId}")
    public void assignWork(@PathVariable int deptId, @RequestBody Work work) {
        workService.assignWork(deptId, work);
    }

    @DeleteMapping("/departments/{deptId}/works/{workId}")
    public void removeFromDept(@PathVariable int deptId, @PathVariable int workId) {
        workService.removeFromDept(deptId, workId);
    }

    @GetMapping
    public List<Work> findAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return workService.findAll(page, size);
    }

    @GetMapping("/duties/{id}")
    public Work findById(@PathVariable int id) {
        return workService.findById(id);
    }

    @DeleteMapping("/duties/{id}")
    public void deleteById(@PathVariable int id) {
        workService.deleteById(id);
    }

    @PostMapping("/duties")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Work save(@RequestBody Work work) {
        return workService.save(work);
    }
}
