package com.edoardoconti.mfs.app.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/**
 * A primary button that can be used in the application.
 */
public class PrimaryButton implements GUIComponent {

    private final Button button;

    public PrimaryButton(String text) {
        this.button = new Button(text);
       // this.button.setPrefHeight(100);
        this.button.getStyleClass().add("primary-button");
    }

    public void setOnAction(EventHandler<ActionEvent> handler){
        button.setOnAction(handler);
    }


    @Override
    public Parent getRootNode() {
        return button;
    }
}
