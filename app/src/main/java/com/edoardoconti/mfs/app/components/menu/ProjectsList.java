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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.edoardoconti.mfs.model.Nameable;

import com.edoardoconti.mfs.app.GUIAppController;
import com.edoardoconti.mfs.app.components.AppStrings;
import com.edoardoconti.mfs.app.components.GUIComponent;

/**
 * A list of projects that the user can choose from.
 */
public class ProjectsList implements GUIComponent {

    private final TreeView<String> root;
    private final GUIAppController controller;
    private final String ROOT_LABEL = AppStrings.PROJECTS_LABEL;

    public ProjectsList(GUIAppController controller, String ...classes) {
        this.root = new TreeView<>(new TreeItem<>(ROOT_LABEL));
        this.controller = controller;
        build();
        addListeners();
    }

    public void update() {
        this.root.getRoot().getChildren().clear();
        List<TreeItem<String>> treeItems = getProjectsNames().stream()
                .map(TreeItem::new)
                .toList();
        this.root.getRoot().getChildren().addAll(treeItems);
        adjustTreeViewHeight();
    }

    // private

    private void build() {
        adjustTreeViewHeight();
    }

    private void addListeners() {
        root.getRoot().addEventHandler(TreeItem.branchExpandedEvent(), event -> adjustTreeViewHeight());
        root.getRoot().addEventHandler(TreeItem.branchCollapsedEvent(), event -> adjustTreeViewHeight());

        root.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            var item = root.getSelectionModel().getSelectedItem();
            if (item == null || Objects.equals(item.getValue(), ROOT_LABEL))
                return;
            controller.setActiveProject(item.getValue());
        });

    }

    private void adjustTreeViewHeight() {
        int visibleRowCount = countVisibleRows(root.getRoot());
        double rowHeight = 24;
        root.setPrefHeight(visibleRowCount * rowHeight);
    }

    private int countVisibleRows(TreeItem<?> item) {
        int count = 1;
        if (item.isExpanded()) {
            for (TreeItem<?> child : item.getChildren())
                count += countVisibleRows(child);
        }
        return count;
    }

    private List<String> getProjectsNames() {
        return controller.getProjectsNames().stream()
                .map(Nameable::getName)
                .collect(Collectors.toList());
    }


    @Override
    public Parent getRootNode() {
        return root;
    }
}
