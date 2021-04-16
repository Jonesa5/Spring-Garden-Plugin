package com.majesty373.springgarden;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("springgarden")
public interface SpringGardenConfig extends Config {
    @ConfigItem(
            keyName = "tilesGood",
            name = "Good Tile Color",
            description = "Color to highlight tiles when it is time to click them.",
            position = 1
    )
    default Color tilesGood() {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "tilesBad",
            name = "Bad Tile Color",
            description = "Color to highlight tiles when it is NOT time to click them.",
            position = 2
    )
    default Color tilesBad() {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "staminaWarning",
            name = "Low Stamina Threshold",
            description = "What stamina level to warn to use stamina potion (0 to disable).",
            position = 3
    )
    default int staminaThreshold() { return 25; }
}
