package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.DutyService;
import org.prison.model.data.staffs.Duty;
import org.prison.model.data.utils.ValidationResp;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping
public class DutyController {

    private final DutyService dutyService;

    @GetMapping("/duties")
    public List<Duty> getDutyList(@RequestParam LocalDate date,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return dutyService.getDutyList(date, page, size);
    }

    @GetMapping("/duties/validate")
    public ValidationResp validateDutyList(@RequestParam LocalDate date) {
        return dutyService.validateDutyList(date);
    }

    @PostMapping("/departments/{deptId}/duties")
    public Duty saveDuty(@PathVariable int deptId,
                         @RequestBody Duty duty) {
        return dutyService.saveDuty(deptId, duty);
    }

    @DeleteMapping("/duties/{id}")
    public void deleteDuty(@PathVariable int id) {
        dutyService.deleteDuty(id);
    }

    @GetMapping("/duties/{id}")
    public Duty findById(@PathVariable int id) {
        return dutyService.findById(id);
    }
}
