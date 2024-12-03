package org.prison.model.data.utils;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
//@Builder
@NoArgsConstructor
public class StatisticsResp {
    Map<Integer, Integer> ageStat;
    Map<String, Integer> crimeTypeStat;
    Map<String, Integer> marriageStat;
    Map<String, Integer> edLevelStat;
    Map<String, Integer> districtStat;
    Map<String, Integer> dLevelStat;
}
