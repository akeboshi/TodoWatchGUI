/*
 * Copyright(C) 2014-2015 mikan All rights reserved.
 */
package jp.co.gui.aruga.watch;

import com.sun.istack.internal.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author mikan
 */
class ClockUpdater implements Runnable {

    private static final Logger LOG = Logger.getLogger(ClockUpdater.class);
    private static final int REFRESH_INTERVAL = 100;
    private static final DateTimeFormatter FORMAT
            = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    private final Label date;
    private final Label hour;
    private final Label dot1;
    private final Label dot2;
    private final Label minute;

    ClockUpdater(Label date, Label hour, Label dot1, Label dot2, Label minute) {
        this.date = date;
        this.hour = hour;
        this.dot1 = dot1;
        this.dot2 = dot2;
        this.minute = minute;
    }

    @Override
    public void run() {
        while (true) {
            Platform.runLater(() -> {
                LocalDateTime dateTime = LocalDateTime.now();
                date.setText(dateTime.toLocalDate().format(FORMAT));
                hour.setText(String.format("%02d", dateTime.getHour()));
                minute.setText(String.format("%02d", dateTime.getMinute()));
                dot1.setVisible(dateTime.getNano() < 500000000);
                dot2.setVisible(dateTime.getNano() < 500000000);
            });
            try {
                Thread.sleep(REFRESH_INTERVAL);
            } catch (InterruptedException ex) {
                LOG.warning("Updater interrupted!");
                break;
            }
        }
    }
}
