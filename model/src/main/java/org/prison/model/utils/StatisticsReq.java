package org.prison.model.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StatisticsReq {
    Integer ageStart;
    Integer ageEnd;
    String crimeType;
    String marriage;
    String edLevel;
    String district;
    String dLevel;
}