package com.github.sambsnyd.problems;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.MinMaxPriorityQueue;

import java.util.*;

/**
 * From Cracking the Coding Interview 6th edition, chapter 8, page 134
 * Example solutions begin on page 342
 */
public class RecursionAndMemoizationProblems {
    /**
     * A child is running up a staircase with n steps and can hop either 1 step, 2 steps or 3 steps at a time
     * Implement a method to count how many possible ways a child can run up the stairs
     */
    public int stairTraversalCombinationsBrute(int stepCount) {
        return stairTraversalCombinationsBrute(0, stepCount);
    }
    private int stairTraversalCombinationsBrute(int combosSoFar, int stepsRemaining) {
        switch(stepsRemaining) {
            case 0: return combosSoFar;
            case 1: return combosSoFar + 1;
            case 2: return combosSoFar + 2;
            case 3: return combosSoFar + 4;
        }
        // Each of n steps is roughly constant but kicks off 3 new invocations so roughly O(3^n)
        return combosSoFar +
                stairTraversalCombinationsBrute(combosSoFar, stepsRemaining-3) +
                stairTraversalCombinationsBrute(combosSoFar, stepsRemaining-2) +
                stairTraversalCombinationsBrute(combosSoFar, stepsRemaining-1);
    }
    public int stairTraversalCombinationsMemoized(int stepCount) {
        int[] memo = new int[Math.max(stepCount+1, 4)];
        memo[0] = 0;
        memo[1] = 1;
        memo[2] = 2;
        memo[3] = 4;
        return stairTraversalCombinationsMemoized(0, stepCount, memo);
    }
    private int stairTraversalCombinationsMemoized(int combosSoFar, int stepsRemaining, int[] memo) {
        if(stepsRemaining == 0) {
            return combosSoFar;
        }
        if(memo[stepsRemaining] == 0) {
            memo[stepsRemaining] = combosSoFar +
                    stairTraversalCombinationsMemoized(combosSoFar, stepsRemaining-3, memo) +
                    stairTraversalCombinationsMemoized(combosSoFar, stepsRemaining-2, memo) +
                    stairTraversalCombinationsMemoized(combosSoFar, stepsRemaining-1, memo);
        }
        return memo[stepsRemaining];
    }

    public static class GridPoint {
        public GridPoint(int row, int column) {
            this.row = row; this.column = column;
        }
        int row;
        int column;

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            GridPoint gridPoint = (GridPoint) other;
            return row == gridPoint.row &&
                    column == gridPoint.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }

        public GridPoint right() {
            return new GridPoint(row, column+1);
        }
        public GridPoint down() {
            return new GridPoint(row+1, column);
        }

        /**
         * The number of steps to get to another point on the grid
         * Considering only vertical and horizontal movements
         */
        public Integer distance(GridPoint other) {
            return Math.abs(row - other.row) + Math.abs(column - other.column);
        }

        @Override
        public String toString() {
            return "(" + row + "," + column + ")";
        }
    }

    /**
     * Robot in a Grid: Imagine a robot sitting on the upper left corner of a grid with r rows and c columns
     * The robot can only move right or down. Some cells are blocked as off-limits to the robot.
     * Find a path for the robot from the top left to the bottom right
     *
     * true in the grid will denote that the space is passable
     * false in the grid will denote that the space is impassible
     *
     * Implements A* even though that's overkill for a traversal that has so many constraints
     *
     * Returns an empty list if there is no valid path to the end
     */
    public List<GridPoint> robotTraverseGrid(ArrayTable<Integer, Integer, Boolean> grid) {
        Preconditions.checkNotNull(grid);
        Preconditions.checkArgument(grid.size() > 0, "Grid must be non-empty");

        List<GridPoint> result = new ArrayList<>();

        int gridWidth = grid.columnKeyList().size();
        int gridHeight = grid.rowKeyList().size();

        GridPoint startingPosition = new GridPoint(0, 0);
        GridPoint endingPosition = new GridPoint(gridHeight-1, gridWidth-1);
        // Points that have been discovered but not evaluated
        // Kept in a heap weakly ordered according to their distance from the endingPosition
        MinMaxPriorityQueue<GridPoint> toEvaluate =  MinMaxPriorityQueue
                .orderedBy(Comparator.comparing((GridPoint g) -> g.distance(endingPosition)))
                .create();
        toEvaluate.add(startingPosition);

        // Points that have been discovered and evaluated fully
        Set<GridPoint> evaluated = new HashSet<>();

        // Points that have been evaluated mapped to the smallest known number of steps from the starting position
        Map<GridPoint, Integer> stepsFromStart = new HashMap<>();
        stepsFromStart.put(startingPosition, 0);

        // Map of a GridPoint to the GridPoint it was discovered from.
        Map<GridPoint, GridPoint> pathToOrigin = new HashMap<>();

        while(toEvaluate.size() > 0) {
            // Take the point with the shortest heuristic distance to the end
            GridPoint point = toEvaluate.pollFirst();
            evaluated.add(point);
            if(point.equals(endingPosition)) {
                result = findPathToOrigin(pathToOrigin, point);
                break;
            }

            GridPoint rightCandiate = point.right();
            if(!evaluated.contains(rightCandiate) &&
                rightCandiate.column <= endingPosition.column &&
                grid.at(rightCandiate.row, rightCandiate.column)) {
                toEvaluate.add(rightCandiate);
                pathToOrigin.put(rightCandiate, point);
            }

            GridPoint downCandidate = point.down();
            if(!evaluated.contains(downCandidate) &&
                downCandidate.row <= endingPosition.row &&
                grid.at(downCandidate.row, downCandidate.column)) {
                toEvaluate.add(downCandidate);
                pathToOrigin.put(downCandidate, point);
            }
        }
        return result;
    }
    private List<GridPoint> findPathToOrigin(Map<GridPoint, GridPoint> stepsFromStart, GridPoint endPoint) {
        List<GridPoint> result = new ArrayList<>();
        result.add(endPoint);

        GridPoint next = stepsFromStart.get(endPoint);
        while(next != null) {
            result.add(0, next);
            next = stepsFromStart.get(next);
        }
        return result;
    }
}