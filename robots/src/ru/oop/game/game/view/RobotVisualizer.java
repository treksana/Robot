package ru.oop.game.game.view;

import ru.oop.game.game.model.GameObject;
import ru.oop.game.game.model.Robot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class RobotVisualizer extends GameObjectVisualizer {
    @Override
    public void draw(Graphics2D g, GameObject gameObject) {
        Robot robot = (Robot)gameObject;
        int robotCenterX = (int)robot.getPositionX();
        int robotCenterY = (int)robot.getPositionY();
        int robotDiameter = 30;

        AffineTransform t = AffineTransform.getRotateInstance(robot.getDirection(),
                robotCenterX, robotCenterY);
        g.setTransform(t);

        g.setColor(Color.BLUE);
        g.fillOval(robotCenterX - robotDiameter/2, robotCenterY - robotDiameter/2,
                robotDiameter, robotDiameter);
        g.setColor(Color.BLACK);
        g.drawOval(robotCenterX - robotDiameter/2, robotCenterY - robotDiameter/2,
                robotDiameter, robotDiameter);

        g.setTransform(new AffineTransform());

        if (robot.isTargetVisible()) {
            drawTarget(g, robot.getTargetPositionX(), robot.getTargetPositionY());
        }

        if (robot.isStuck()) {
            drawStuckWarning(g, robotCenterX, robotCenterY);
        }

        // Отладочная информация
        g.setColor(Color.RED);
        g.drawString(String.format("Pos: %.1f, %.1f", robot.getPositionX(), robot.getPositionY()),
                robotCenterX + 20, robotCenterY);

        // Визуализация зоны коллизий
        g.setColor(new Color(255, 0, 0, 50));
        g.fillOval((int)(robot.getPositionX() - robot.getRadius()),
                (int)(robot.getPositionY() - robot.getRadius()),
                (int)(robot.getRadius() * 2),
                (int)(robot.getRadius() * 2));

    }

    private void drawTarget(Graphics2D g, int x, int y) {
        g.setColor(Color.GREEN);
        g.fillOval(x - 3, y - 3, 6, 6);
        g.setColor(Color.BLACK);
        g.drawOval(x - 3, y - 3, 6, 6);
    }

    private void drawStuckWarning(Graphics2D g, int x, int y) {
        g.setColor(Color.RED);
        g.drawString("STUCK!", x + 20, y - 10);
    }
}