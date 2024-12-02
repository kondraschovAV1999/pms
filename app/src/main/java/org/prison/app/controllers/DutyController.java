package org.prison.app.controllers;

import lombok.AllArgsConstructor;
import org.prison.app.service.DutyService;
import org.prison.model.staffs.Duty;
import org.prison.model.utils.ValidationResp;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/pms/api/duties")
public class DutyController {

    private final DutyService dutyService;

    @GetMapping
    public List<Duty> getDutyList(@RequestParam LocalDate date,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return dutyService.getDutyList(date, page, size);
    }

    @GetMapping("/validate")
    public ValidationResp validateDutyList(@RequestParam LocalDate date) {
        return dutyService.validateDutyList(date);
    }

    @PostMapping
    public Duty saveDuty(@RequestBody Duty duty) {
        return dutyService.saveDuty(duty);
    }

    @DeleteMapping("/{id}")
    public void deleteDuty(@PathVariable int id) {
        dutyService.deleteDuty(id);
    }

    @GetMapping("/{id}")
    public Duty findById(@PathVariable int id) {
        return dutyService.findById(id);
    }
}
