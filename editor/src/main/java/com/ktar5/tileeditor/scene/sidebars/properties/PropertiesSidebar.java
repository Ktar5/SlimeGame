package com.ktar5.tileeditor.scene.sidebars.properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTree;
import com.ktar5.tileeditor.Main;
import com.ktar5.tileeditor.properties.ParentProperty;
import com.ktar5.tileeditor.properties.Property;
import com.ktar5.tileeditor.properties.RootProperty;
import com.ktar5.tileeditor.properties.StringProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PropertiesSidebar extends VisTree {
    private RootProperty root;
    public Map<String, PropertyNode> nodes;

    public PropertiesSidebar(RootProperty root) {
        super();
        this.nodes = new HashMap<>();
        this.root = root;
        PropertyNode rootNode = new PropertyNode(root, this);
        for (Property property : root.getChildren().values()) {
            addNode(rootNode, property);
        }
        nodes.put("properties", rootNode);
        rootNode.setExpanded(true);
        this.add(rootNode);
    }

    public void removeNode(String key) {
        Node node = nodes.remove(key);
        if (node != null) {
            this.remove(node);
        }
    }

    public Node addNode(Node parent, Property property) {
        PropertyNode childNode = new PropertyNode(property, this);
        parent.add(childNode);
        nodes.put(property.getPath(), childNode);
        if (property instanceof ParentProperty) {
            for (Property descendant : ((ParentProperty) property).getChildren().values()) {
                addNode(childNode, descendant);
            }
        }
        return childNode;
    }

    @Getter
    public class PropertyNode extends Node {
        private Property property;
        private VisLabel nameLabel;
        private VisLabel valueLabel;

        public PropertyNode(Property property, PropertiesSidebar sidebar) {
            super(new VisTable());
            this.property = property;

            VisTable table = ((VisTable) this.getActor());

            this.nameLabel = new VisLabel(property.getName());
            if (property instanceof StringProperty) {
                this.valueLabel = new VisLabel(((StringProperty) property).getValue());
            } else {
                this.valueLabel = new VisLabel("");
            }

            ClickListener clickListener = new ClickListener(Input.Buttons.RIGHT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    com.ktar5.tileeditor.scene.sidebars.properties.PropertiesRClickMenu propertiesRClickMenu = new PropertiesRClickMenu(property, sidebar);
                    propertiesRClickMenu.showMenu(Main.getInstance().getRoot(), Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                }
            };

            table.add(nameLabel).space(10);
            table.addSeparator(true).space(10);
            table.add(valueLabel).expandX();
            table.addListener(clickListener);
        }

        public void update() {
            nameLabel.setText(property.getName());
            if (property instanceof StringProperty) {
                valueLabel.setText(((StringProperty) property).getValue());
            }
        }
    }

}
