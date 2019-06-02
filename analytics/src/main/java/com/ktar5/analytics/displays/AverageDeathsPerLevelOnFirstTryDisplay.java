package com.ktar5.analytics.displays;

import com.ktar5.analytics.statistics.AverageDeathsPerLevelOnFirstComplete;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;

public class AverageDeathsPerLevelOnFirstTryDisplay {

    public AverageDeathsPerLevelOnFirstTryDisplay(int minLevel, int maxLevel) {
        IntColumn level = IntColumn.create("level");
        DoubleColumn avg = DoubleColumn.create("average");

        for (int i = minLevel; i <= maxLevel; i++) {
            double value = new AverageDeathsPerLevelOnFirstComplete(i).value;
            if (value >= 0) {
                level.append(i);
                avg.append(value);
                System.out.println("added level: " + i + " with value: " + value);
            }
        }

        Table table = Table.create("Test").addColumns(level, avg);
        System.out.println(table.print());

//        Plot.show(
//                VerticalBarPlot.create(
//                        "test", // plot title
//                        table,           // table
//                        "level",               // grouping column name
//                        "average"));  // numeric column name
    }

}
