package com.ktar5.analytics.displays;

import com.ktar5.analytics.statistics.AverageDeathsPerLevel;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.VerticalBarPlot;

public class AverageDeathsPerLevelDisplay {

    public AverageDeathsPerLevelDisplay(int minLevel, int maxLevel) {
        IntColumn level = IntColumn.create("level");
        DoubleColumn avg = DoubleColumn.create("average");

        for (int i = minLevel; i <= maxLevel; i++) {
            double value = new AverageDeathsPerLevel(i).value;
            if (value >= 0) {
                level.append(i);
                avg.append(value);
                System.out.println("added level: " + i + " with value: " + value);
            }
        }

        Table table = Table.create("Test").addColumns(level, avg);
        System.out.println(table.print());

        Plot.show(
                VerticalBarPlot.create(
                        "Average Deaths Per Level", // plot title
                        table,           // table
                        "level",               // grouping column name
                        "average"));  // numeric column name
    }

}
