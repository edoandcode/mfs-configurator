package com.edoardoconti.mfs.app.components;

import javafx.scene.Node;

/**
 * This interface is used to create a component that can be added to the application.
 * A GUIComponent is a wrapper for a JavaFX tree of nodes with some inner logic.
 */
@FunctionalInterface
public interface GUIComponent {
    Node getRootNode();
}
