package ru.oop.game.game.view;

import ru.oop.game.game.model.GameObject;
import ru.oop.game.game.model.Wall;
import java.awt.Color;
import java.awt.Graphics2D;

public class WallVisualizer extends GameObjectVisualizer {
    @Override
    public void draw(Graphics2D g, GameObject gameObject) {
        Wall wall = (Wall)gameObject;
        // Рисуем "тень" стенки для лучшего восприятия глубины
        g.setColor(new Color(100, 100, 100));
        g.fillRect((int)wall.getPositionX(), (int)wall.getPositionY(),
                (int)wall.getWidth(), (int)wall.getHeight());
        // Основной цвет стенки
        g.setColor(new Color(150, 150, 150));
        g.fillRect((int)wall.getPositionX()+1, (int)wall.getPositionY()+1,
                (int)wall.getWidth()-2, (int)wall.getHeight()-2);
        // Контур
        g.setColor(Color.BLACK);
        g.drawRect((int)wall.getPositionX(), (int)wall.getPositionY(),
                (int)wall.getWidth(), (int)wall.getHeight());
    }
}