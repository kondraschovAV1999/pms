package org.prison.app.service;


import org.prison.model.prisoners.Work;

import java.util.List;

public interface WorkService {
    void assignWork(int deptId, Work work);

    void removeFromDept(int deptId, int workId);

    List<Work> findAll(int page, int size);

    Work findById(int id);

    void deleteById(int id);

    Work save(Work work);
}
