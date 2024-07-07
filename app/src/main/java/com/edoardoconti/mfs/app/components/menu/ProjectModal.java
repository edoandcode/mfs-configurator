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

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import com.edoardoconti.mfs.model.FurnitureType;

import com.edoardoconti.mfs.app.components.AppStrings;
import com.edoardoconti.mfs.app.components.LabeledBox;
import com.edoardoconti.mfs.app.components.Modal;
import com.edoardoconti.mfs.app.components.PrimaryButton;
import com.edoardoconti.mfs.app.GUIAppController;

/**
 * A modal that allows the user to create a new project.
 */
public class ProjectModal extends Modal {

    public ProjectModal(GUIAppController controller, String title) {
        super(controller, title);
    }

    @Override
    public void build() {

        getRootNode().getStylesheets().add("/styles/app.css");

        VBox verticalBox = new VBox();
        verticalBox.getStyleClass().add("project-modal");
        verticalBox.getStyleClass().add("padder");
        verticalBox.setSpacing(20);

        var title = new Label(AppStrings.PROJECT_MODAL_TITLE);

        // project name text field
        var nameField = new LabeledBox<>(AppStrings.PROJECT_MODAL_NAME_LABEL, new TextField());
        // project type choice box
        ChoiceBox<FurnitureType> projectTypeOptions = new ChoiceBox<>();
        projectTypeOptions.getItems().addAll(FurnitureType.values());
        projectTypeOptions.setValue(FurnitureType.SHELVING);
        var projectTypeField = new LabeledBox<>(AppStrings.PROJECT_MODAL_FURNITURE_TYPE_LABEL, projectTypeOptions);
        // project width spinner
        var widthField = new LabeledBox<>(AppStrings.PROJECT_MODAL_WIDTH_LABEL, new Spinner<Integer>(1, GUIAppController.PROJECT_BOARD_WIDTH/GUIAppController.MODULE_SIZE, 4));
        // project height spinner
        var heightField = new LabeledBox<>(AppStrings.PROJECT_MODAL_HEIGHT_LABEL, new Spinner<Integer>(1, GUIAppController.PROJECT_BOARD_HEIGHT/GUIAppController.MODULE_SIZE, 4));
        // module size spinner
        var moduleSize = new LabeledBox<>(AppStrings.PROJECT_MODAL_MODULE_SIZE_LABEL, new Spinner<Integer>(10, 100, 10));

        // create button
        var button = new PrimaryButton(AppStrings.PROJECT_MODAL_CREATE_BUTTON_LABEL);
        button.setOnAction(e -> {
            GUIAppController.exceptionHandler(
                    () -> {
                        getController().createProject(
                                nameField.getContent().getText(),
                                widthField.getContent().getValue(),
                                heightField.getContent().getValue(),
                                moduleSize.getContent().getValue(),
                                projectTypeField.getContent().getValue()
                        );
                    },
                    this::close
            );
            close();
        });

        // add elements
        verticalBox.getChildren().add(title);
        verticalBox.getChildren().add(nameField.getRootNode());
        verticalBox.getChildren().add(projectTypeField.getRootNode());
        verticalBox.getChildren().add(widthField.getRootNode());
        verticalBox.getChildren().add(heightField.getRootNode());
        verticalBox.getChildren().add(moduleSize.getRootNode());
        verticalBox.getChildren().add(button.getRootNode());
        getRootNode().getChildren().add(verticalBox);
    }
}
