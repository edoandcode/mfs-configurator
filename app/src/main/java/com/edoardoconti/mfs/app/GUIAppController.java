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

package com.edoardoconti.mfs.app;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.edoardoconti.mfs.model.*;

import com.edoardoconti.mfs.Controller;
import com.edoardoconti.mfs.app.components.Layout;
import com.edoardoconti.mfs.app.components.menu.MainMenu;
import com.edoardoconti.mfs.app.components.shelvingworkspace.ShelvingWorkSpace;
import com.edoardoconti.mfs.app.components.sidebar.SideBar;
import com.edoardoconti.mfs.service.OntologyService;


/**
 * The main controller for the GUI application.
 */
public class GUIAppController {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int PROJECT_BOARD_WIDTH = 700;
    public static final int PROJECT_BOARD_HEIGHT = 700;
    public static final int MODULE_SIZE = 80;

    private final Stage mainStage;
    private final Controller controller;
    private final Scene scene;
    private final Layout layout;
    private final MainMenu mainMenu;
    private final ShelvingWorkSpace workSpace;
    private final SideBar sideBar;

    public GUIAppController(Stage mainStage) {
        AtomicReference<OntologyService> ontologyServiceRef = new AtomicReference<>();
        GUIAppController.exceptionHandler(() -> {
            ontologyServiceRef.set(new OntologyService());
        });
        OntologyService ontologyService = ontologyServiceRef.get();

        this.mainStage = mainStage;
        controller = new Controller(ontologyService);
        layout = new Layout();
        scene = new Scene(layout.getRootNode(), WIDTH, HEIGHT);
        mainMenu = new MainMenu(this);
        workSpace = new ShelvingWorkSpace(this, Layout.BODY_WIDTH, Layout.BODY_HEIGHT);
        sideBar = new SideBar(this);
        build();
        addListeners();
    }

    public void setActiveProject(String projectName) {
        controller.setActiveFurniture(projectName);
        updateStageTitle();
        workSpace.updateProject();
    }

    public void setActiveColor(Color color) {
        controller.setActiveColor(color);
        if(sideBar != null)
            sideBar.update();
    }

    public void setActivePosition(Position position) {
        controller.setActivePosition(position);
    }

    public void selectComponentFactory(ComponentFactory componentFactory) {
        controller.setComponentFactory(componentFactory);
    }


    public void createProject(String projectName, int width, int height, int moduleSize, FurnitureType furnitureFactory) {
        this.controller.setFurnitureFactory(furnitureFactory);
        this.controller.createFurniture(projectName, width, height, moduleSize);
        this.setActiveProject(projectName);
        mainMenu.update();
        sideBar.show();
    }

    public void createComponent() {
        exceptionHandler(() -> {
            Component component = controller.createComponent();
            component.getModules().stream().forEach(m -> workSpace.updateLocation(m.getPosition()));
        });
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return mainStage;
    }

    public List<Color> getColors() { return controller.getColors(); }

    public Furniture getActiveFurniture() {
        return controller.getActiveFurniture();
    }

    public Color getActiveColor() {
        return controller.getActiveColor();
    }

    public List<? extends Nameable> getProjectsNames() {
        return controller.getFurnitures();
    }

    public void exportData(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save MSC File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MSC File", "*.rdf"));
        File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            exceptionHandler(() -> {
                try {
                    controller.exportData(selectedFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    ///////


    private void build() {
        layout.setHeader(mainMenu);
        layout.setBody(workSpace);
        layout.setSideBar(sideBar);
    }

    private void addListeners() {
        /*
        TODO: implement selection mood
        getScene().setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("SHIFT")) {
                System.out.println("Selection mood on");
                event.consume();
            }
        });

        getScene().setOnKeyReleased(event -> {
            if (event.getCode().toString().equals("SHIFT")) {
                System.out.println("Selection mood off");
                event.consume();
            }
        });

        scene.setOnMouseClicked(event -> scene.getRoot().requestFocus());
         */
    }


    private void updateStageTitle() {
        mainStage.setTitle(mainStage.getTitle().split(" - ")[0] + " - " + controller.getActiveFurniture().getName());
    }


    public static void exceptionHandler(Runnable tryBlock) {
        try {
            tryBlock.run();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void exceptionHandler(Runnable tryBlock, Runnable catchBlock) {
        try {
            tryBlock.run();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            catchBlock.run();
        }
    }
}
