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

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.components.GUIComponent;
import com.edoardoconti.mfs.app.components.Layout;
import com.edoardoconti.mfs.app.util.GUIUtils;

/**
 * The sidebar component of the application, containing the color chooser, component type chooser, and buttons.
 */
public class SideBar implements GUIComponent {

    private final GUIAppController controller;
    private final VBox root;
    private final ComponentTypeChooser componentTypeChooser;
    private final ColorChooser colorChooser;
    private final Buttons buttons;


    public SideBar(GUIAppController controller) {
        this.root = new VBox();
        this.controller = controller;
        this.componentTypeChooser = new ComponentTypeChooser(controller);
        this.colorChooser = new ColorChooser(controller);
        this.buttons = new Buttons(controller);
        build();
        initialize();
    }

    private void build() {
        GUIUtils.addClasses(this, "side-bar", "padder");
        root.setSpacing(20);
        root.setPrefWidth(Layout.SIDEBAR_WIDTH);
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().add(colorChooser.getRootNode());
        root.getChildren().add(componentTypeChooser.getRootNode());
        root.getChildren().add(buttons.getRootNode());
    }

    public void update() {
        componentTypeChooser.update();
    }

    @Override
    public VBox getRootNode() {
        return root;
    }

    private void initialize() {
        update();
        hide();
    }

    public void hide() {
        FadeTransition fade = new FadeTransition();
        fade.setNode(root);
        fade.setDuration(Duration.millis(500));
        fade.setToValue(0);
        fade.play();
    }

    public void show() {
        FadeTransition fade = new FadeTransition();
        fade.setNode(root);
        fade.setDuration(Duration.millis(500));
        fade.setToValue(1);
        fade.play();
    }
}
