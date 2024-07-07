package com.edoardoconti.mfs.app.components;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.edoardoconti.mfs.app.GUIAppController;

/**
 * A modal window that can be shown to the user.
 */
public abstract class Modal implements GUIComponent {

    private final Stage modalStage;
    private final GUIAppController controller;
    private final Pane root ;

    public Modal(GUIAppController controller, String title) {
        this.controller = controller;
        modalStage = new Stage();
        root = new AnchorPane();
        Scene modalScene = new Scene(root);
        modalStage.setTitle(title);
        modalStage.initOwner(controller.getStage());
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setScene(modalScene);
        build();
    }

    public void show() {
        modalStage.showAndWait();
    }

    public void close() {
        modalStage.hide();
    }

    public GUIAppController getController() {
        return controller;
    }


    @Override
    public Pane getRootNode() {
        return root;
    }

    public abstract void build();


}
