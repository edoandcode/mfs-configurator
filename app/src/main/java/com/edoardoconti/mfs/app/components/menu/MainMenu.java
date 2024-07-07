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

package com.edoardoconti.mfs.app.components.menu;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;

import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.components.*;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * The main menu of the application, containing the list of projects, create project button, and export button.
 */
public class MainMenu implements GUIComponent {
    private final GUIAppController controller;
    private final HBox root;
    private final ProjectsList projectsList;
    private final ProjectModal projectModal;
    private final PrimaryButton createButton;
    private final PrimaryButton exportButton;

    public MainMenu(GUIAppController controller) {
        this.controller = controller;
        root = new HBox();
        projectsList = new ProjectsList(controller);
        projectModal = new ProjectModal(controller, AppStrings.PROJECT_MODAL_TITLE);
        createButton = new PrimaryButton(AppStrings.CREATE_PROJECT_LABEL);
        exportButton = new PrimaryButton(AppStrings.EXPORT_LABEL);

        build();
        addListeners();
    }

    private void build() {
        GUIUtils.addClasses(this, "main-menu", "padder");
        root.setSpacing(20);
        root.toFront();
        root.getChildren().add(projectsList.getRootNode());
        root.getChildren().add(createButton.getRootNode());
        root.getChildren().add(exportButton.getRootNode());
    }

    private void addListeners() {
        createButton.setOnAction(e -> {
            projectModal.show();
        });
        exportButton.setOnAction(controller::exportData);
    }

    public void update() {
        projectsList.update();
    }


    @Override
    public Parent getRootNode() {
        return this.root;
    }


}
