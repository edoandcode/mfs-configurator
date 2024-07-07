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

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.List;

import com.edoardoconti.mfs.model.shelvingsystem.ShelvingModule;
import com.edoardoconti.mfs.model.Module;
import com.edoardoconti.mfs.model.Position;

import com.edoardoconti.mfs.app.components.GUIComponent;
import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * A component that knows how to render a shelving module.
 */
public class ShelvingModuleRenderer implements GUIComponent {
    private GUIAppController controller;
    private final Pane root;
    private final ShelvingModule module;
    private final int PANEL_THICKNESS = 10;

    public ShelvingModuleRenderer(GUIAppController controller, ShelvingModule module) {
        this.controller = controller;
        root = new Pane();
        this.module = module;
        build();
    }

    public void build() {
        root.setPrefHeight(GUIAppController.MODULE_SIZE);
        root.setPrefWidth(GUIAppController.MODULE_SIZE);
        buildBackPanel();
        buildLeftPanel();
        buildRightPanel();
        buildTopPanel();
        buildBottomPanel();
        buildDoor();
    }

    private void buildBackPanel() {
        if(!module.isBackClosed())
            return;
        var backPanel = new Pane();
        GUIUtils.copySize(backPanel, root);
        backPanel.setStyle("-fx-background-color: " + module.getColor().getHex());
        var shadowPanel = new Pane();
        GUIUtils.copySize(shadowPanel, root);
        shadowPanel.setStyle("-fx-background-color: rgba(0,0,0,0.5)");
        backPanel.getChildren().add(shadowPanel);
        root.getChildren().add(backPanel);
    }

    private void buildLeftPanel() {
        if(hasJoinedNeighbour(module, Position.Direction.LEFT))
            return;
        var leftPanel = new Pane();
        GUIUtils.copyHeight(leftPanel, root);
        leftPanel.setPrefWidth(PANEL_THICKNESS);
        leftPanel.setStyle("-fx-background-color: " + module.getColor().getHex());
        leftPanel.setLayoutX(GUIAppController.MODULE_SIZE - PANEL_THICKNESS);
        leftPanel.setLayoutY(0);
        root.getChildren().add(leftPanel);
    }

    private void buildRightPanel() {
        if(hasJoinedNeighbour(module, Position.Direction.RIGHT))
            return;
        var rightPanel = new Pane();
        GUIUtils.copyHeight(rightPanel, root);
        rightPanel.setPrefWidth(PANEL_THICKNESS);
        rightPanel.setStyle("-fx-background-color: " + module.getColor().getHex());
        rightPanel.setLayoutX(0);
        rightPanel.setLayoutY(0);
        root.getChildren().add(rightPanel);
    }

    private void buildTopPanel() {
        if(hasJoinedNeighbour(module, Position.Direction.TOP))
            return;
        var topPanel = new Pane();
        GUIUtils.copyWidth(topPanel, root);
        topPanel.setPrefHeight(PANEL_THICKNESS);
        topPanel.setStyle("-fx-background-color: " + module.getColor().getHex());
        topPanel.setLayoutX(0);
        topPanel.setLayoutY(0);
        root.getChildren().add(topPanel);
    }

    private void buildBottomPanel() {
        if(hasJoinedNeighbour(module, Position.Direction.BOTTOM))
            return;
        var bottomPanel = new Pane();
        GUIUtils.copyWidth(bottomPanel, root);
        bottomPanel.setPrefHeight(PANEL_THICKNESS);
        bottomPanel.setStyle("-fx-background-color: " + module.getColor().getHex());
        bottomPanel.setLayoutX(0);
        bottomPanel.setLayoutY(GUIAppController.MODULE_SIZE - PANEL_THICKNESS);
        root.getChildren().add(bottomPanel);
    }

    private void buildDoor() {
        if(!module.hasDoor())
            return;
        var doorPanel = new Pane();
        GUIUtils.copySize(doorPanel, root);
        doorPanel.setStyle("-fx-background-color: " + module.getColor().getHex());
        var handle = new Circle(5);
        handle.setStyle("-fx-fill: rgba(0,0,0,0.5)");
        handle.setLayoutX(GUIAppController.MODULE_SIZE - 15);
        handle.setLayoutY((double) GUIAppController.MODULE_SIZE / 2);
        doorPanel.getChildren().add(handle);
        root.getChildren().add(doorPanel);
    }

    private boolean hasJoinedNeighbour(ShelvingModule module, Position.Direction direction) {
        if(isRealModule(module)) {
            List<Module> joinedModules = controller.getActiveFurniture().getComponent(module.getPosition()).getModules();
            for (Module m : joinedModules)
                if (m.getPosition().isAdjacent(module.getPosition(), direction))
                    return true;
        }
        return false;
    }

    private boolean isRealModule(ShelvingModule module) {
        return controller.getActiveFurniture() != null && controller.getActiveFurniture().getComponent(module.getPosition()) != null;
    }

    @Override
    public Pane getRootNode() {
        return root;
    }
}
