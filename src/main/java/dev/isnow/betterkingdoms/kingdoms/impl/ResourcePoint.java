package dev.isnow.betterkingdoms.kingdoms.impl;

import de.exlll.configlib.Comment;
import org.bukkit.Material;

public record ResourcePoint(
        @Comment({"", " Material name to use"})
        Material material,
        @Comment({" ", " How many points its worth"})
        int points
) {
}