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

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import com.edoardoconti.mfs.app.components.AppStrings;
import com.edoardoconti.mfs.app.components.GUIComponent;
import com.edoardoconti.mfs.app.components.PrimaryButton;
import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * A wrapper component for the buttons in the sidebar.
 */
public class Buttons implements GUIComponent {
    private final GUIAppController controller;
    private final VBox root;
    private final PrimaryButton createModuleButton;
    //private final PrimaryButton joinModulesButton;

    public Buttons(GUIAppController controller) {
        this.controller = controller;
        root = new VBox();
        createModuleButton = new PrimaryButton(AppStrings.CREATE_MODULE_LABEL);
        //joinModulesButton = new PrimaryButton(AppStrings.JOIN_MODULES_LABEL);
        build();
        addListners();
    }

    public void build() {
        GUIUtils.addClasses(this, "context-buttons", "padder");
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(createModuleButton.getRootNode());
        // root.getChildren().add(joinModulesButton.getRootNode());
    }

    private void addListners() {
        createModuleButton.setOnAction(e -> controller.createComponent());
        //joinModulesButton.setOnAction(e -> controller.joinModules());
    }

    @Override
    public Node getRootNode() {
        return root;
    }
}
