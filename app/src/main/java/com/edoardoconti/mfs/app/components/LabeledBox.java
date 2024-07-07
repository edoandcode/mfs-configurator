package com.edoardoconti.mfs.app.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * A labeled box that contains a node.
 * @param <T> the type of the node contained in the box.
 */
public class LabeledBox<T extends Node> implements GUIComponent {

    private final VBox root;
    private final Label label;
    private final T content;

    public LabeledBox(String text, T content) {
        this.label = new Label(text);
        this.root = new VBox();
        this.content = content;
        build();
    }

    private void build(){
        GUIUtils.addClasses(this, "label-element");
        root.setSpacing(10);
        this.root.getChildren().add(label);
        this.root.getChildren().add(content);
    }

    public T getContent() {
        return content;
    }

    @Override
    public VBox getRootNode() {
        return root;
    }
}
