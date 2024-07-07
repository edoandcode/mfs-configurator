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

package com.edoardoconti.mfs.model.shelvingsystem;

import com.edoardoconti.mfs.model.Color;
import com.edoardoconti.mfs.model.Module;
import com.edoardoconti.mfs.model.Position;

/**
 * Represents a module of a shelving system.
 * A shelving module is a module that can be placed in a shelving system.
 * */
public abstract class ShelvingModule extends Module {
    private final boolean isBackClosed;
    private final boolean hasDoor;
    public ShelvingModule(int size, Position position, Color color, boolean isBackClosed, boolean hasDoor) {
        super(size, position, color);
        this.isBackClosed = isBackClosed;
        this.hasDoor = hasDoor;
    }

    public boolean isBackClosed() {
        return isBackClosed;
    }

    public boolean hasDoor() {
        return hasDoor;
    }
}
