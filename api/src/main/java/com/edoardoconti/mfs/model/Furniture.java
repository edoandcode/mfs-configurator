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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a furniture system that can hold components at specific positions.
 */
public abstract class Furniture implements Nameable {
    private final String name;
    private final int width;
    private final int height;
    private final int moduleSize;
    private final Map<Position, Component> components;

    public Furniture(String name, int width, int height, int moduleSize) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.moduleSize = moduleSize;
        components = new HashMap<>();
    }

    public void addComponent(Component component) {
        component.getModules().forEach(m -> components.put(m.getPosition(), component));
    };

    public void removeComponent(Component component) {
        component.getModules().forEach(m -> components.remove(m.getPosition()));
    };

    public List<Component> getComponents(){
        return List.copyOf(components.values());
    };

    public Component getComponent(Position position){
        return components.get(position);
    };

    public int getWidth() {
        return width;
    };

    public int getHeight() {
        return height;
    };

    public int getModuleSize() {
        return moduleSize;
    };

    @Override
    public String getName() {
        return name;
    };

}
