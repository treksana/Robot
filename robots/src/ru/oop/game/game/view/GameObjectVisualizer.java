package ru.oop.game.game.view;

import ru.oop.game.game.model.GameObject;
import java.awt.Graphics2D;

public abstract class GameObjectVisualizer {
    public abstract void draw(Graphics2D g, GameObject gameObject);
}