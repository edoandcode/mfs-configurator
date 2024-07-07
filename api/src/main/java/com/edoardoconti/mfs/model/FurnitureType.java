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


import com.edoardoconti.mfs.model.shelvingsystem.ShelvingSystem;

/**
 * An enumeration of the different types of furniture that can be created.
 * Currently, the only type of furniture that can be created is shelving.
 */
public enum FurnitureType implements FurnitureFactory {
    SHELVING;

    /**
     * Creates a new Furniture object with the specified width, height, name, and module size.
     * The type of furniture created depends on the enum value.
     *
     * @param name the name of the furniture
     * @param width the width of the furniture
     * @param height the height of the furniture
     * @param moduleSize the size of the modules that can be placed in the furniture
     * @return a new Furniture object of the specified type
     */
    @Override
    public Furniture create( String name, int width, int height, int moduleSize) {
        return switch (this) {
            case SHELVING ->  new ShelvingSystem( name, width, height, moduleSize);
        };
    }
}