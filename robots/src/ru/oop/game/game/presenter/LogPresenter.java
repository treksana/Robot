package ru.oop.game.game.presenter;

import ru.oop.game.log.LogChangeListener;
import ru.oop.game.log.LogEntry;
import ru.oop.game.log.LogWindowSource;
import ru.oop.game.log.LogWindow;

public class LogPresenter implements LogChangeListener {
    private final LogWindowSource logSource;
    private final LogWindow logWindow;

    public LogPresenter(LogWindowSource logSource, LogWindow logWindow) {
        this.logSource = logSource;
        this.logWindow = logWindow;

        // Регистрируем этот презентер как слушатель изменений
        logSource.registerListener(this);

        // Первоначальное обновление представления
        updateLogContent();
    }

    @Override
    public void onLogChanged() {
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logWindow.setLogContent(content.toString());
    }

    public void dispose() {
        logSource.unregisterListener(this);
    }
}