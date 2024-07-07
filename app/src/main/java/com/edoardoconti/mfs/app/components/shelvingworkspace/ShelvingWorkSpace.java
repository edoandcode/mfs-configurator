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


import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.edoardoconti.mfs.model.Component;
import com.edoardoconti.mfs.model.Position;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingModule;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingSystem;

import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.components.GUIComponent;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * The workspace of the application for configuring shelving system .
 */
public class ShelvingWorkSpace implements GUIComponent {

    private final GUIAppController controller;
    private final StackPane root;
    private final GridPane gridPane;
    private final Map<Position, ShelvingModuleLocation> modulesGrid;
    private final int width;
    private final int height;

    public ShelvingWorkSpace(GUIAppController controller, int width, int height) {
        this.controller = controller;
        this.width = width;
        this.height = height;
        modulesGrid = new HashMap<>();
        root = new StackPane();
        gridPane = new GridPane();
        build();
    }

    public void build() {
        GUIUtils.addClasses(this, "work-space", "padder");
        root.setAlignment(Pos.CENTER);
        gridPane.setAlignment(Pos.CENTER);
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        root.getChildren().add(gridPane);
        root.toBack();
    }

    public void updateProject() {
        gridPane.getChildren().clear();
        modulesGrid.clear();
        var activeFurniture = controller.getActiveFurniture();
        if(!(activeFurniture instanceof ShelvingSystem)){
            throw new IllegalArgumentException("ShelvingWorkSpace can only be used with ShelvingSystem!");
        }
        for (int i = 0; i < activeFurniture.getHeight(); i++) {
            for (int j = 0; j < activeFurniture.getWidth(); j++) {
                var position = new Position(j, i);
                ShelvingModuleLocation moduleLocation = new ShelvingModuleLocation(controller, position);
                modulesGrid.put(position, moduleLocation);
                moduleLocation.onAction(this::handleModuleLocationAction);
                gridPane.add(moduleLocation.getRootNode(), j, i);
            }
        }
        initializeProject();
    }


    public void updateLocation(Position position) {
        var moduleLocation = modulesGrid.get(position);
        Component component = controller.getActiveFurniture().getComponent(position);
        moduleLocation.setModule((ShelvingModule) component.getModule(position));
    }


    @Override
    public Parent getRootNode() {
        return root;
    }


    //// private

    private void handleModuleLocationAction(Position position) {
        // if is selection mood do something else ...
        var moduleLocation = modulesGrid.get(position);
        highlightModuleLocation(moduleLocation);
        controller.setActivePosition(moduleLocation.getPosition());
    }

    private void highlightModuleLocation(ShelvingModuleLocation...moduleLocation) {
        modulesGrid.values().forEach(ml -> ml.getRootNode().getStyleClass().remove("selected"));
        Arrays.stream(moduleLocation).forEach(ml -> ml.getRootNode().getStyleClass().add("selected"));
    }

    private void initializeProject() {

        controller.getActiveFurniture().getComponents().forEach(c -> {
            c.getModules().forEach(m -> {
                updateLocation(m.getPosition());
            });
        });
        modulesGrid.get(new Position(0, 0)).getRootNode().fire();
    }

}
