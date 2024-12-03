package org.prison.model.data.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.prison.model.data.staffs.Duty;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResp {
    private List<Duty> invalidList;
    private boolean isValid;
}
