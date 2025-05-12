package ru.oop.game.game.presenter;

import ru.oop.game.game.model.GameModel;
import ru.oop.game.game.view.GameVisualizer;
import java.util.Timer;
import java.util.TimerTask;

public class GamePresenter {
    private final GameModel model;
    private final GameVisualizer view;
    private final Timer timer;

    public GamePresenter(GameModel model, GameVisualizer view) {
        this.model = model;
        this.view = view;
        this.timer = new Timer("events generator", true);

        initMouseHandling();
        startTimer();
    }

    private void initMouseHandling() {
        view.setMouseClickListener(point -> {
            model.handleMouseClick(point.x, point.y);
            updateView();
        });
        updateView();
    }

    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.update(10);
                updateView();
            }
        }, 0, 10);
    }

    private void updateView() {
        view.updateState(model.getGameObjects());
    }
}