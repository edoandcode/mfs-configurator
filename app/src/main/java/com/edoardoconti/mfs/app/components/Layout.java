package com.edoardoconti.mfs.app.components;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * The layout of the application, containing the header, sidebar, and body.
 */
public final class Layout implements GUIComponent{

    public static final int HEADER_HEIGHT = 65;
    public static final int SIDEBAR_WIDTH = 200;
    public static final int BODY_WIDTH = GUIAppController.WIDTH - SIDEBAR_WIDTH;
    public static final int BODY_HEIGHT = GUIAppController.HEIGHT - HEADER_HEIGHT;
    private final AnchorPane root;
    private Pane header;
    private Pane body;
    private Pane sideBar;

    public Layout() {
        this.root = new AnchorPane();
        build();
    }

    private void build() {
        GUIUtils.addClasses(this, "layout");
        root.prefWidth(GUIAppController.WIDTH);
        root.prefHeight(GUIAppController.HEIGHT);
        root.getStylesheets().add("/styles/app.css");

        buildBody();
        buildSideBar();
        buildHeader();
    }

    private void buildHeader() {
        header = new Pane();
        GUIUtils.addClasses(header, "layout-header");
        header.setPrefWidth(GUIAppController.WIDTH);
        header.setPrefHeight(HEADER_HEIGHT);
        header.setLayoutX(0);
        header.setLayoutY(0);
        root.getChildren().add(header);
    }

    private void buildBody() {
        body = new Pane();
        GUIUtils.addClasses(body, "layout-body");
        body.setPrefWidth(GUIAppController.WIDTH - SIDEBAR_WIDTH);
        body.setPrefHeight(GUIAppController.HEIGHT - HEADER_HEIGHT);
        body.setLayoutX(0);
        body.setLayoutY(HEADER_HEIGHT);
        root.getChildren().add(body);
    }

    private void buildSideBar() {
        sideBar = new Pane();
        GUIUtils.addClasses(sideBar, "layout-sidebar");
        sideBar.setPrefWidth(SIDEBAR_WIDTH);
        sideBar.setPrefHeight(GUIAppController.HEIGHT - HEADER_HEIGHT);
        sideBar.setLayoutX(GUIAppController.WIDTH - SIDEBAR_WIDTH);
        sideBar.setLayoutY(HEADER_HEIGHT);
        root.getChildren().add(sideBar);
    }

    public void setHeader(GUIComponent component) {
        if (!header.getChildren().isEmpty())
            throw new IllegalStateException("Header already set");
        header.getChildren().add(component.getRootNode());
    }

    public void setSideBar(GUIComponent component) {
        if (!sideBar.getChildren().isEmpty())
            throw new IllegalStateException("SideBar already set");
        sideBar.getChildren().add(component.getRootNode());
    }

    public void setBody(GUIComponent component) {
        if (!body.getChildren().isEmpty())
            throw new IllegalStateException("Body already set");
        body.getChildren().add(component.getRootNode());
    }


    @Override
    public Parent getRootNode() {
        return root;
    }
}
