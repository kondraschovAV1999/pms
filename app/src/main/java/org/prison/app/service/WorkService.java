package org.prison.app.service;


import org.prison.model.data.prisoners.Work;

import java.util.List;

public interface WorkService {
    void assignWork(int deptId, int id);

    void removeFromDept(int deptId, int workId);

    List<Work> findAll(int page, int size);

    Work findById(int id);

    void deleteById(int id);

    Work save(Work work);
}
