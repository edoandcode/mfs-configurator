package com.edoardoconti.mfs.app.util;

import com.edoardoconti.mfs.app.components.GUIComponent;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public class GUIUtils {

    public static void addClasses(GUIComponent component, String ...classes) {
        for (String styleClass : classes)
            component.getRootNode().getStyleClass().add(styleClass);
    }

    public static void addClasses(Node node, String ...classes) {
        for (String styleClass : classes)
            node.getStyleClass().add(styleClass);
    }

    public static void copyWidth(Region target, Region source) {
        target.prefWidthProperty().bind(source.prefWidthProperty());
    }
    public static void copyHeight(Region target, Region source) {
        target.prefHeightProperty().bind(source.prefHeightProperty());
    }

    public static void copySize(Region target, Region source) {
        copyWidth(target, source);
        copyHeight(target, source);
    }

}
