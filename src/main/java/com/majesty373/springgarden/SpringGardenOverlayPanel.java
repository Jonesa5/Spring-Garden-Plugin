package com.majesty373.springgarden;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class SpringGardenOverlayPanel extends OverlayPanel {
    static final int TIMEOUT = 120;
    static final int DRINK_EXP = 1350;

    private final Client client;
    private final SpringGardenPlugin plugin;

    @Inject
    SpringGardenOverlayPanel(Client client, SpringGardenPlugin plugin) {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render (Graphics2D graphics) {
        SpringGardenSession session = plugin.getSession();
        if (session == null) {
            return null;
        }
        if (isDoingMaze() || Duration.between(session.getLastFruitGathered(), Instant.now()).getSeconds() < TIMEOUT) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Running")
                    .color(Color.GREEN)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("NOT running")
                    .color(Color.RED)
                    .build());
        }
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Run Time:")
                .right(getFormattedTimeFromStart())
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Runs/Fails:")
                .right(session.getTotalRuns() + "/" + session.getFailedRuns())
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Success Rate:")
                .right(getSuccessRate() + "%")
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("-----------")
                .right("----------")
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Fruit:")
                .right(Integer.toString(session.getFruitGathered()))
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Drinks:")
                .right(Integer.toString(session.getDrinksMade()))
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("-----------")
                .right("----------")
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Fruit/hr:")
                .right("" + getFruitPerHour())
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Drinks/hr:")
                .right("" + getDrinkPerHour())
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("XP/hr:")
                .right("" + getExperiencePerHour())
                .build());
        return super.render(graphics);
    }

    private boolean isDoingMaze () {
        return Objects.requireNonNull(client.getLocalPlayer()).getWorldLocation().distanceTo2D(new WorldPoint(2928, 5468, 0)) < 20;
    }

    public String getFormattedTimeFromStart() {
        Duration time = Duration.ofMillis(getTimeFromStart());
        long minutes = time.toMinutes() % 60;
        long seconds = time.getSeconds() % 60;
        long hours = time.toHours();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getExperiencePerHour() {
        int expGain = plugin.getSession().getDrinksMade() * DRINK_EXP;
        return (int) (3600000d / (long) getTimeFromStart() * (double) (expGain));
    }

    public int getFruitPerHour() {
        int fruit = plugin.getSession().getFruitGathered();
        return (int) (3600000d / (long) getTimeFromStart() * (double) (fruit));
    }

    public int getDrinkPerHour() {
        int drink = plugin.getSession().getDrinksMade();
        return (int) (3600000d / (long) getTimeFromStart() * (double) (drink));
    }

    public int getSuccessRate() {
        SpringGardenSession session = plugin.getSession();
        return (int)(((double)(session.getTotalRuns() - session.getFailedRuns()) / session.getTotalRuns()) * 100);
    }

    public int getTimeFromStart() {
        return (int)(System.currentTimeMillis() - SpringGardenPlugin.START_TIME);
    }
}