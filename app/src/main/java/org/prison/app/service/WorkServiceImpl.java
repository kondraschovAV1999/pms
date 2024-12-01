package org.prison.app.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prison.app.exceptions.NotFoundException;
import org.prison.model.prisoners.Work;
import org.prison.model.staffs.Department;
import org.prison.repositories.DepartmentRepository;
import org.prison.repositories.WorkRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
@Slf4j
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final DepartmentRepository departmentRepository;

    private static final String WORK_NOT_FOUND = "Work with id=%d Not Found";
    private static final String DEPT_NOT_FOUND = "Dept with id=%d Not Found";

    @Override
    public void assignWork(int deptId, Work work) {
        findDepartmentById(deptId).addWork(work);
    }

    @Override
    public void removeFromDept(int deptId, int workId) {
        findDepartmentById(deptId).removeWork(findById(workId));
    }

    @Override
    public List<Work> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Work> slice = workRepository.findWorkList(pageable);
        if (slice.hasContent()) return slice.getContent();
        else return new ArrayList<>();
    }

    @Override
    public Work findById(int id) {
        return workRepository.findById(id).orElseThrow(() -> {
                    String message = WORK_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                }
        );
    }

    private Department findDepartmentById(int id) {
        return departmentRepository.findById(id).orElseThrow(() -> {
                    String message = DEPT_NOT_FOUND.formatted(id);
                    log.error(message);
                    return new NotFoundException(message);
                }
        );
    }

    @Override
    public void deleteById(int id) {
        workRepository.deleteById(id);
    }

    @Override
    public Work save(Work work) {
        return workRepository.save(work);
    }
}
