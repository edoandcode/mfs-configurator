/*
 * MIT License
 *
 * Copyright (c) 2024 Edoardo Conti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.edoardoconti.mfs.model;

import java.util.*;

/**
 * Represents a position in a 3D space with x, y, and z coordinates.
 */
public final class Position {
    private final int x;
    private final int y;
    private final int z;

    /**
     * Constructs a Position object at the specified x, y, and z coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a Position object at the specified x and y coordinates.
     * The z coordinate is set to 0.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Returns the x-coordinate of this position.
     *
     * @return the x-coordinate
     */
    public int getX() { return x; }

    /**
     * Returns the y-coordinate of this position.
     *
     * @return the y-coordinate
     */
    public int getY() { return y; }

    /**
     * Returns the z-coordinate of this position.
     *
     * @return the z-coordinate
     */
    public int getZ() { return z;}

    /**
     * Checks if this position is adjacent to the specified position in the specified directions.
     *
     * @param other the other position
     * @param directions the directions to check adjacency in
     * @return true if the positions are adjacent, false otherwise
     */
    public boolean isAdjacent(Position other, Direction ...directions) {
        if(other.equals(this))
            return false;
        return other.equals(getRelativePosition(directions));
    }

    /**
     * Checks if this position is adjacent to the specified position along the specified axis.
     *
     * @param other the other position
     * @param axis the axis to check adjacency along
     * @return true if the positions are adjacent, false otherwise
     */
    public boolean isAdjacent(Position other, Axis axis) {
        return switch (axis) {
            case X -> isAdjacent(other, Direction.LEFT) || isAdjacent(other, Direction.RIGHT);
            case Y -> isAdjacent(other, Direction.TOP) || isAdjacent(other, Direction.BOTTOM);
            case Z -> isAdjacent(other, Direction.FRONT) || isAdjacent(other, Direction.BACK);
        };
    }

    /**
     * Checks if this position is adjacent to the specified position.
     *
     * @param other the other position
     * @return true if the positions are adjacent, false otherwise
     */
    public boolean isAdjacent(Position other) {
        int dx = Math.abs(x - other.x);
        int dy = Math.abs(y - other.y);
        int dz = Math.abs(z - other.z);
        return (
                (dx <= 1 && dy <= 1 && dz <= 1)
                        && (dx == 1 || dy == 1 || dz == 1)
        );
    }


    /**
     * Determines if the provided list of positions are connected to form a chain along a specified axis.
     * This method checks if there exists a sequence of adjacent positions (considering the specified axis)
     * that connects all given positions without revisiting any. The adjacency is determined based on the
     * {@code isAdjacent} method, which considers positions to be adjacent if they are next to each other
     * along the specified axis. The method employs a Depth-First Search (DFS) algorithm to explore possible
     * connections starting from each position until it finds a sequence that connects all positions or
     * determines that no such sequence exists.
     *
     * @param positions the list of positions to be checked for connectivity.
     * @param axis the axis along which adjacency is considered for forming the chain.
     * @return {@code true} if the positions can be connected to form a chain along the specified axis,
     *         {@code false} otherwise.
     * @throws NullPointerException if the {@code positions} list is {@code null}.
     */
    public static boolean areConnected(List<Position> positions, Axis axis) {
        Objects.requireNonNull(positions);
        if (positions.size() < 2)
            return false;
        // Use a set to keep track of visited positions
        Set<Position> visited = new HashSet<>();
        // Try to find a starting point and perform DFS to find a chain
        for (Position start : positions)
            if (dfs(start, positions, visited, positions.size() - 1, axis))
                return true;
        return false;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    // static

    /**
     * Represents the possible directions in a 3D space.
     */
    public static enum Direction {
        TOP, BOTTOM, LEFT, RIGHT, BACK, FRONT,
    }

    /**
     * Represents the possible axes in a 3D space.
     */
    public static enum Axis {
        X, Y, Z
    }

    // private

    /**
     * Returns a new position that is relative to this position in the specified directions.
     *
     * @param directions the directions to move in
     * @return a new position that is relative to this position
     */
    private Position getRelativePosition(Direction ... directions) {
        requiredUniqueAxisDirection(directions);
        int x = this.x;
        int y = this.y;
        int z = this.z;
        for(Direction direction : directions) {
            switch (direction) {
                case TOP    -> y--;
                case BOTTOM -> y++;
                case LEFT   -> x--;
                case RIGHT  -> x++;
                case BACK   -> z++;
                case FRONT  -> z--;
            }
        }
        return new Position(x, y, z);
    }

    /**
     * Checks if the specified directions are unique along each axis.
     * If not, throws an IllegalArgumentException.
     *
     * @param directions the directions to check
     * @throws IllegalArgumentException if the directions are not unique along each axis
     */
    private void requiredUniqueAxisDirection(Direction ...directions) {
        if(
                Arrays.stream(directions).filter(d -> (d==Direction.TOP || d==Direction.BOTTOM)).count() > 1
                        || Arrays.stream(directions).filter(d -> (d==Direction.LEFT || d==Direction.RIGHT)).count() > 1
                        || Arrays.stream(directions).filter(d -> (d==Direction.FRONT || d==Direction.BACK)).count() > 1
        )
            throw new IllegalArgumentException("The direction along the same axis must be unique.");
    }


    /**
     * Depth-First Search to check if we can form a chain starting from the current position.
     *
     * @param current the current position
     * @param positions the array of all positions
     * @param visited the set of visited positions
     * @param remaining the number of positions remaining to be visited
     * @return true if a chain can be formed, false otherwise
     */
    private static boolean dfs(Position current, List<Position> positions, Set<Position> visited, int remaining, Axis axis) {
        // Mark the current position as visited
        visited.add(current);
        // If no remaining positions, a chain is formed
        if (remaining == 0)
            return true;
        // Try to find the next position to form a chain
        for (Position next : positions)
            if (!visited.contains(next) && current.isAdjacent(next, axis))
                if (dfs(next, positions, visited, remaining - 1, axis))
                    return true;
        // Backtrack: unmark the current position as visited
        visited.remove(current);
        return false;
    }
}
