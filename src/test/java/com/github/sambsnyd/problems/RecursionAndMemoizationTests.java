package com.github.sambsnyd.problems;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.github.sambsnyd.problems.RecursionAndMemoizationProblems.GridPoint;

import java.util.List;

public class RecursionAndMemoizationTests {

    private RecursionAndMemoizationProblems problems = new RecursionAndMemoizationProblems();

    @Test
    void stairTraversalCombinations() {
        Assertions.assertEquals(1, problems.stairTraversalCombinationsBrute(1),
                "A single step is the only way to climb a stairway a single step tall");
        Assertions.assertEquals(2, problems.stairTraversalCombinationsBrute(2),
                "two single steps or one double step climbs a stairway 2 stairs tall");
        Assertions.assertEquals(4, problems.stairTraversalCombinationsBrute(3),
                "3, 2+1, 1+2, 1+1+1");
        Assertions.assertEquals(7, problems.stairTraversalCombinationsBrute(4),
                "3+1, 1+3, 2+2, 2+1+1, 1+2+1, 1+1+2, 1+1+1+1");
    }
    @Test
    void stairTraversalCombinationsMemoized() {
        Assertions.assertEquals(1, problems.stairTraversalCombinationsMemoized(1),
                "A single step is the only way to climb a stairway a single step tall");
        Assertions.assertEquals(2, problems.stairTraversalCombinationsMemoized(2),
                "two single steps or one double step climbs a stairway 2 stairs tall");
        Assertions.assertEquals(4, problems.stairTraversalCombinationsMemoized(3),
                "3, 2+1, 1+2, 1+1+1");
        Assertions.assertEquals(7, problems.stairTraversalCombinationsMemoized(4),
                "3+1, 1+3, 2+2, 2+1+1, 1+2+1, 1+1+2, 1+1+1+1");
    }

    @Test
    void robotTraverseGridTest() {
        // A 2x2 grid where the upper-right corner is impassable
        // [ _ X
        //   _ _ ]
        // So the only valid traversal from top left to bottom right is through (0,0), (1,0), (1,1)
        ArrayTable<Integer, Integer, Boolean> simpleGrid =
                ArrayTable.create(Lists.newArrayList(0,1), Lists.newArrayList(0,1));
        simpleGrid.put(0,0, true);
        simpleGrid.put(0,1, false);
        simpleGrid.put(1,0, true);
        simpleGrid.put(1,1, true);

        var simpleGridTraversal = problems.robotTraverseGrid(simpleGrid);
        Assertions.assertIterableEquals(
                List.of(new GridPoint(0,0), new GridPoint(1,0), new GridPoint(1,1)),
                simpleGridTraversal);

        // A 2x2 grid where there is no valid path to the bottom right corner
        // [ _ X
        //   X _ ]
        // So the expected return value is an empty list
        ArrayTable<Integer, Integer, Boolean> impassableGrid =
                ArrayTable.create(Lists.newArrayList(0,1), Lists.newArrayList(0,1));
        impassableGrid.put(0,0, true);
        impassableGrid.put(0,1, false);
        impassableGrid.put(1,0, false);
        impassableGrid.put(1,1, true);

        var impassableGridTraversal = problems.robotTraverseGrid(impassableGrid);
        Assertions.assertIterableEquals(List.of(), impassableGridTraversal);

        // A larger grid where there are traps the algorithm must avoid
        // [ _ _ _ _ _
        //   _ X _ X _
        //   _ X _ X X
        //   _ X _ _ _ ]
        //TODO: Fill this grid and complete the test
//        ArrayTable<Integer,Integer,Boolean> trapGrid =
//                ArrayTable.create(Lists.newArrayList(0,1,2,3,4), Lists.newArrayList(0,1,2,3,4));
    }
}
