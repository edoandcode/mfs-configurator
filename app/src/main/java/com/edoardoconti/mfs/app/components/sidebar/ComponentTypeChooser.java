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

package com.edoardoconti.mfs.app.components.sidebar;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import com.edoardoconti.mfs.model.ComponentFactory;
import com.edoardoconti.mfs.model.Position;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingClosedModule;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingClosedModuleWithDoor;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingOpenModule;
import com.edoardoconti.mfs.model.shelvingsystem.ShelvingComponentType;

import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.components.AppStrings;
import com.edoardoconti.mfs.app.components.GUIComponent;
import com.edoardoconti.mfs.app.components.LabeledBox;
import com.edoardoconti.mfs.app.components.shelvingworkspace.ShelvingModuleRenderer;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * A component that allows the user to choose the type of module to create.
 */
public class ComponentTypeChooser implements GUIComponent {
    private final LabeledBox<VBox> root; ;
    private final VBox wrapper;
    private final GUIAppController controller;
    private Button openModuleButton;
    private Button closedModuleButton;
    private Button doorModuleButton;


    public ComponentTypeChooser(GUIAppController controller) {
        this.controller = controller;
        wrapper = new VBox();
        root = new LabeledBox<>(AppStrings.CHOOSE_MODULE_TYPE_LABEL, wrapper);
        build();
        initialize();
    }

    private void build() {
        GUIUtils.addClasses(this, "module-type-chooser");
        wrapper.setSpacing(10);
        wrapper.setAlignment(Pos.CENTER);
        buildOpenModuleButton();
        buildClosedModuleButton();
        buildDoorModuleButton();
    }

    private void initialize() {
        openModuleButton.fire();
    }


    private void buildOpenModuleButton() {
        openModuleButton = new Button();
        GUIUtils.addClasses(openModuleButton, "module-button");
        openModuleButton.setOnAction(e -> handleButtonAction(e, ShelvingComponentType.OPEN));
        wrapper.getChildren().add(openModuleButton);
    }

    private void buildClosedModuleButton() {
        closedModuleButton = new Button();
        GUIUtils.addClasses(closedModuleButton, "module-button");
        closedModuleButton.setOnAction(e -> handleButtonAction(e, ShelvingComponentType.CLOSED));
        wrapper.getChildren().add(closedModuleButton);
    }

    private void buildDoorModuleButton() {
        doorModuleButton = new Button();
        GUIUtils.addClasses(doorModuleButton, "module-button");
        doorModuleButton.setOnAction(e -> handleButtonAction(e, ShelvingComponentType.CLOSED_WITH_DOOR));
        wrapper.getChildren().add(doorModuleButton);
    }

    private void handleButtonAction(Event e, ComponentFactory componentFactory) {
        openModuleButton.getStyleClass().remove("selected");
        closedModuleButton.getStyleClass().remove("selected");
        doorModuleButton.getStyleClass().remove("selected");
        ((Button) e.getSource()).getStyleClass().add("selected");
        controller.selectComponentFactory(componentFactory);
    }

    public void update() {
        var activeColor = controller.getActiveColor();

        ShelvingModuleRenderer openModule = new ShelvingModuleRenderer(controller, new ShelvingOpenModule(1, new Position(0 , 0), activeColor));
        openModuleButton.setGraphic(openModule.getRootNode());

        ShelvingModuleRenderer closedModule = new ShelvingModuleRenderer(controller, new ShelvingClosedModule(1, new Position(0 , 0), activeColor));
        closedModuleButton.setGraphic(closedModule.getRootNode());

        ShelvingModuleRenderer doorModule = new ShelvingModuleRenderer(controller, new ShelvingClosedModuleWithDoor(1, new Position(0 , 0), activeColor));
        doorModuleButton.setGraphic(doorModule.getRootNode());
    }



    @Override
    public VBox getRootNode() {
        return root.getRootNode();
    }
}
