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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    private Position origin;

    @BeforeEach
    void setUp() {
        origin = new Position(0, 0, 0);
    }

    @Test
    void samePosition_shouldNotBeAdjacent() {
        Position samePosition = new Position(0, 0, 0);
        assertFalse(origin.isAdjacent(samePosition));
    }

    @Test
    void directlyAdjacentPosition_shouldBeAdjacent() {
        Position adjacentPosition = new Position(1, 0, 0);
        assertTrue(origin.isAdjacent(adjacentPosition));
    }

    @Test
    void diagonallyAdjacentPosition_shouldBeAdjacent() {
        Position diagonalPosition = new Position(1, 1, 0);
        assertTrue(origin.isAdjacent(diagonalPosition));
    }

    @Test
    void notAdjacentPosition_shouldNotBeAdjacent() {
        Position farPosition = new Position(5, 5, 5);
        assertFalse(origin.isAdjacent(farPosition));
    }

    @Test
    void adjacentPositionAlongXAxis_shouldBeAdjacent() {
        Position adjacentPosition = new Position(1, 0, 0);
        assertTrue(origin.isAdjacent(adjacentPosition, Position.Axis.X));
    }

    @Test
    void adjacentPositionAlongYAxis_shouldBeAdjacent() {
        Position adjacentPosition = new Position(0, 1, 0);
        assertTrue(origin.isAdjacent(adjacentPosition, Position.Axis.Y));
    }

    @Test
    void adjacentPositionAlongZAxis_shouldBeAdjacent() {
        Position adjacentPosition = new Position(0, 0, 1);
        assertTrue(origin.isAdjacent(adjacentPosition, Position.Axis.Z));
    }

    @Test
    void nonAdjacentPosition_shouldNotBeAdjacent() {
        Position nonAdjacentPosition = new Position(2, 2, 2);
        assertFalse(origin.isAdjacent(nonAdjacentPosition, Position.Axis.X));
        assertFalse(origin.isAdjacent(nonAdjacentPosition, Position.Axis.Y));
        assertFalse(origin.isAdjacent(nonAdjacentPosition, Position.Axis.Z));
    }

    @Test
    void adjacentPositionInDirectionTop_shouldBeAdjacent() {
        Position currentPosition = new Position(0, 1, 0);
        Position adjacentPosition = new Position(0, 0, 0); // Top direction from currentPosition
        assertTrue(currentPosition.isAdjacent(adjacentPosition, Position.Direction.TOP));
    }

    @Test
    void adjacentPositionInDirectionBottom_shouldBeAdjacent() {
        Position adjacentPosition = new Position(0, 1, 0); // Bottom direction from currentPosition
        assertTrue(origin.isAdjacent(adjacentPosition, Position.Direction.BOTTOM));
    }

    @Test
    void adjacentPositionInDirectionLeft_shouldBeAdjacent() {
        Position currentPosition = new Position(1, 0, 0);
        Position adjacentPosition = new Position(0, 0, 0); // Left direction from currentPosition
        assertTrue(currentPosition.isAdjacent(adjacentPosition, Position.Direction.LEFT));
    }

    @Test
    void adjacentPositionInDirectionRight_shouldBeAdjacent() {
        Position adjacentPosition = new Position(1, 0, 0); // Right direction from currentPosition
        assertTrue(origin.isAdjacent(adjacentPosition, Position.Direction.RIGHT));
    }

    @Test
    void adjacentPositionInDirectionFront_shouldBeAdjacent() {
        Position currentPosition = new Position(0, 0, 1);
        Position adjacentPosition = new Position(0, 0, 0); // Front direction from currentPosition
        assertTrue(currentPosition.isAdjacent(adjacentPosition, Position.Direction.FRONT));
    }

    @Test
    void adjacentPositionInDirectionBack_shouldBeAdjacent() {
        Position adjacentPosition = new Position(0, 0, 1); // Back direction from currentPosition
        assertTrue(origin.isAdjacent(adjacentPosition, Position.Direction.BACK));
    }

    @Test
    void emptyListOfPositions_shouldNotBeConnected() {
        List<Position> positions = Collections.emptyList();
        assertFalse(Position.areConnected(positions, Position.Axis.X));
    }

    @Test
    void singleElementList_shouldNotBeConnected() {
        List<Position> positions = Collections.singletonList(new Position(0, 0, 0));
        assertFalse(Position.areConnected(positions, Position.Axis.X));
    }

    @Test
    void adjacentPositionAlongXAxis_shouldBeConnected() {
        List<Position> positions = Arrays.asList(
                new Position(0, 0, 0),
                new Position(1, 0, 0),
                new Position(2, 0, 0)
        );
        assertTrue(Position.areConnected(positions, Position.Axis.X));
    }

    @Test
    void adjacentPositionAlongYAxis_shouldBeConnected() {
        List<Position> positions = Arrays.asList(
                new Position(0, 0, 0),
                new Position(0, 1, 0),
                new Position(0, 2, 0)
        );
        assertTrue(Position.areConnected(positions, Position.Axis.Y));
    }

    @Test
    void disconnectedPositions_shouldNotBeConnected() {
        List<Position> positions = Arrays.asList(
                new Position(0, 0, 0),
                new Position(1, 0, 0),
                new Position(3, 0, 0) // This position is not directly connected
        );
        assertFalse(Position.areConnected(positions, Position.Axis.X));
    }

    @Test
    void connectedDiagonallyPositions_shouldNotBeConnected() {
        List<Position> positions = Arrays.asList(
                new Position(0, 0, 0),
                new Position(1, 1, 0),
                new Position(2, 2, 0)
        );
        assertFalse(Position.areConnected(positions, Position.Axis.X));
        assertFalse(Position.areConnected(positions, Position.Axis.Y));
    }
}