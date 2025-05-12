/*package ru.oop.game.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();
    
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100; 
    private volatile double m_robotDirection = 0; 

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    
    private static final double maxVelocity = 0.1; 
    private static final double maxAngularVelocity = 0.001; 
    
    public GameVisualizer() 
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
    
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    
    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY, 
            m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }
        
        moveRobot(velocity, angularVelocity, 10);
    }
    
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
    
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity * 
            (Math.sin(m_robotDirection  + angularVelocity * duration) -
                Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity * 
            (Math.cos(m_robotDirection  + angularVelocity * duration) -
                Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration); 
        m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }
    
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(m_robotPositionX); 
        int robotCenterY = round(m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }
    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
*/


/*package ru.oop.game.gui.view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;
import javax.swing.*;

import static javax.swing.SwingUtilities.convertPointFromScreen;

public class GameVisualizer extends JPanel {
    private int robotX;
    private int robotY;
    private double robotDirection;
    private int targetX;
    private int targetY;
    private Consumer<Point> targetChangeListener;

    public GameVisualizer() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Получаем координаты относительно GameVisualizer
                Point panelPoint = SwingUtilities.convertPoint(
                        (Component)e.getSource(),
                        e.getPoint(),
                        GameVisualizer.this
                );
                handleTargetChange(panelPoint);
                System.out.println("Raw click: " + e.getPoint());
                Point clickedPoint = e.getPoint();
                System.out.println("Panel coords: " + clickedPoint);
                handleTargetChange(clickedPoint);
            }
        });
        setDoubleBuffered(true);
    }

    public GameVisualizer() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point realPoint = convertPointFromScreen(e.getPoint());
                System.out.println("Converted point: " + realPoint);
                if (targetChangeListener != null) {
                    targetChangeListener.accept(realPoint);
                }
            }
        });
    }

    private Point convertPointFromScreen(Point p) {
        // Учитываем смещение и границы всех родительских компонентов
        Point converted = SwingUtilities.convertPoint(
                this,
                p,
                getTopLevelAncestor()
        );
        return SwingUtilities.convertPoint(
                getTopLevelAncestor(),
                converted,
                this
        );
    }

    public void updateState(int robotX, int robotY, double direction, int targetX, int targetY) {
        this.robotX = robotX;
        this.robotY = robotY;
        this.robotDirection = direction;
        this.targetX = targetX;
        this.targetY = targetY;
        repaint();
    }

    public void setTargetChangeListener(Consumer<Point> listener) {
        this.targetChangeListener = listener;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, robotX, robotY, robotDirection);
        drawTarget(g2d, targetX, targetY);
    }

    private void handleTargetChange(Point point) {
        if (targetChangeListener != null) {
            targetChangeListener.accept(point);
        }
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = x;
        int robotCenterY = y;

        AffineTransform rotate = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(rotate);

        // Тело робота (эллипс)
        g.setColor(Color.BLUE);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);

        // "Глаз" робота
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);

        // Сбрасываем трансформацию
        g.setTransform(new AffineTransform());
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        // Убираем трансформацию для точного позиционирования
        g.setTransform(new AffineTransform());
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }


    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

}*/







/*package ru.oop.game.game.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;
import javax.swing.JPanel;


 //Класс для визуализации игрового поля (View в MVP)
 // Отвечает только за отрисовку состояния и обработку пользовательского ввода

public class GameVisualizer extends JPanel {
    private int robotX;
    private int robotY;
    private double robotDirection;
    private int targetX;
    private int targetY;

    private Consumer<Point> targetChangeListener;

    public GameVisualizer() {
        // Установка обработчика кликов мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTargetChange(e.getPoint());
            }
        });
        setDoubleBuffered(true);
    }

    //слишком частная реализация сейчас, не только две конкретные сущности в модели gameObject, частный случай кот это робот. Модель может сообщать о обжект кот у нее есть, передавать список.
    // надо дополнительные сущности отрисовщиков, которые отрисовывают конкретный тип объекта (робота). на этом уровне хран сопостовляется между типами сущности модели и отдельного объект отрисовщик сущности такого типа
    // надо ввести класс gameObjectVisualizer - общий классс для всех отрисовщиков наслед частные виды отрисовщиков которые отрисовывают конкретный тип объектов

    // надо на уровне модели класс GameObject - общий класс для всех сущностей модели. Модель должна уметь возвращать коллекцию всех сущностей в ней (gameOblectов)
    // в GameVisualizer должно быть сопостовление типам - как Map<Class<? extends GameObject>, GameObjectVisualizer> - при старте gameVisualizer заполняется, получает список gameObject в методе updateState(должен получать не координаты, а объект)
    // Robot.class - объект типа класс


    public void updateState(int robotX, int robotY, double direction, int targetX, int targetY) {
        this.robotX = robotX;
        this.robotY = robotY;
        this.robotDirection = direction;
        this.targetX = targetX;
        this.targetY = targetY;
        repaint();
    }


    public void setTargetChangeListener(Consumer<Point> listener) {
        this.targetChangeListener = listener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Отрисовка робота
        drawRobot(g2d, robotX, robotY, robotDirection);

        // Отрисовка цели
        drawTarget(g2d, targetX, targetY);
    }

    private void handleTargetChange(Point point) {
        if (targetChangeListener != null) {
            targetChangeListener.accept(point);
        }
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = x;
        int robotCenterY = y;

        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);

        // Тело робота (эллипс)
        g.setColor(Color.BLUE);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);

        // "Глаз" робота
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);

        // Сбрасываем трансформацию
        g.setTransform(new AffineTransform());
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}*/





/*package ru.oop.game.game.view;

import ru.oop.game.game.model.GameObject;
import ru.oop.game.game.model.Robot;
import ru.oop.game.game.model.Wall;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private final Map<Class<? extends GameObject>, GameObjectVisualizer> visualizers = new HashMap<>();
    private List<GameObject> gameObjects;
    private Point targetPosition = new Point(150, 100);
    private Consumer<Point> targetChangeListener;

    public GameVisualizer() {
        // Регистрируем визуализаторы для каждого типа объектов
        visualizers.put(Robot.class, new RobotVisualizer());
        visualizers.put(Wall.class, new WallVisualizer());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTargetChange(e.getPoint());
            }
        });
        setDoubleBuffered(true);
    }

    public void updateState(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
        repaint();
    }

    public void setTargetChangeListener(Consumer<Point> listener) {
        this.targetChangeListener = listener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Отрисовка всех игровых объектов
        if (gameObjects != null) {
            for (GameObject obj : gameObjects) {
                GameObjectVisualizer visualizer = visualizers.get(obj.getClass());
                if (visualizer != null) {
                    visualizer.draw(g2d, obj.getPositionX(), obj.getPositionY(), 0);
                }
            }
        }

        // Отрисовка цели
        drawTarget(g2d, targetPosition.x, targetPosition.y);
    }

    private void handleTargetChange(Point point) {
        targetPosition = point;
        if (targetChangeListener != null) {
            targetChangeListener.accept(point);
        }
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}*/


package ru.oop.game.game.view;

import ru.oop.game.game.model.GameObject;
import ru.oop.game.game.model.Robot;
import ru.oop.game.game.model.Wall;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private final Map<Class<? extends GameObject>, GameObjectVisualizer> visualizers = new HashMap<>();
    private List<GameObject> gameObjects;
    private Consumer<Point> mouseClickListener;

    public GameVisualizer() {
        visualizers.put(Robot.class, new RobotVisualizer());
        visualizers.put(Wall.class, new WallVisualizer());


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mouseClickListener != null) {
                    mouseClickListener.accept(e.getPoint());
                }
            }
        });
        setDoubleBuffered(true);
    }

    public void updateState(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
        repaint();
    }

    public void setMouseClickListener(Consumer<Point> listener) {
        this.mouseClickListener = listener;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (gameObjects != null) {
            for (GameObject obj : gameObjects) {
                GameObjectVisualizer visualizer = visualizers.get(obj.getClass());
                if (visualizer != null) {
                    visualizer.draw(g2d, obj);
                }
            }
        }
    }

}