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

package com.edoardoconti.mfs.app.components.shelvingworkspace;


import javafx.scene.control.Button;

import java.util.function.Consumer;

import com.edoardoconti.mfs.model.Positionable;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingModule;
import com.edoardoconti.mfs.model.Position;

import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.components.GUIComponent;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * A component that represents a location in the shelving workspace where a module can be placed.
 */
public class ShelvingModuleLocation implements GUIComponent, Positionable {

    private final GUIAppController controller;
    private final Position position;
    private final Button root;
    private Consumer<Position> handleLocationSelect;

    public ShelvingModuleLocation(GUIAppController controller, Position position) {
        this.controller = controller;
        this.position = position;
        root = new Button();
        build();
        addListeners();
    }


    private void build() {
        GUIUtils.addClasses(this, "module-location");
        root.setPrefHeight(GUIAppController.MODULE_SIZE);
        root.setPrefWidth(GUIAppController.MODULE_SIZE);
    }

    public void onAction(Consumer<Position> callback) {
        handleLocationSelect = callback;
    }

    public void setModule(ShelvingModule module) {
        if(module == null)
            return;
        var moduleRenderer = new ShelvingModuleRenderer(controller, module);
        moduleRenderer.getRootNode().setLayoutX(0);
        moduleRenderer.getRootNode().setLayoutY(0);
        root.setGraphic(moduleRenderer.getRootNode());
    }

    @Override
    public Button getRootNode() {
        return root;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    // private

    private void addListeners() {
        root.setOnMouseClicked(e -> {
            if(handleLocationSelect != null)
                handleLocationSelect.accept(position);
        });
        root.setOnAction(e -> {
            if(handleLocationSelect != null)
                handleLocationSelect.accept(position);
        });
    }
}
