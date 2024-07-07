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

import java.util.List;

/**
 * Represents a component of a furniture system.
 * A component is a part of a furniture system that can be placed in a furniture system.
 */
public abstract class Component implements Joinable<Component, Component> {
    private final List<Module> modules;

    public Component(List<Module> modules) {
        this.modules = modules;
    }
    public Component(Module module) {
        this.modules = List.of(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModule(Position position){
        return modules.stream().filter(m -> m.getPosition().equals(position)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "Component{" +
                "modules=" + String.join(",", modules.stream().map(Module::toString).toArray(String[]::new)) +
                '}';
    }
}
