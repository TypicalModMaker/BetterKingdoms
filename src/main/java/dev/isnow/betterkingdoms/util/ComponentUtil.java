package dev.isnow.betterkingdoms.util;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.kingdom.KingdomConfig;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@UtilityClass
public class ComponentUtil {
    private final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER =
            LegacyComponentSerializer.legacyAmpersand().toBuilder()
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();
    private final MiniMessage MINI_MESSAGE = MiniMessage.builder().postProcessor(it ->
        it.replaceText(replacementBuilder -> {
            replacementBuilder.match(
                    Pattern.compile(
                            ".*"
                    )
            ).replacement((matchResult, builder) -> LEGACY_COMPONENT_SERIALIZER.deserialize(matchResult.group()));
        }).replaceText(replacementBuilder -> {
            replacementBuilder.match(Pattern.compile(">>")).replacement("»");
        }).replaceText(replacementBuilder -> {
            replacementBuilder.match(Pattern.compile("<<")).replacement("«");
        })).build();

    public Component deserialize(final String input) {
        return deserialize(input, null);
    }

    public Component deserialize(String input, final Player player, final Object... placeholders) {
        if(player != null && BetterKingdoms.getInstance().getHookManager().isPlaceholerAPIHook()) {
            input = PlaceholderAPI.setPlaceholders(player, input);
        }

        return MINI_MESSAGE.deserialize(placeholders != null ? formatPlaceholders(input, placeholders) : input).decoration(TextDecoration.ITALIC, false);
    }

    public Component deserialize(String input, final Player player) {
        return deserialize(input, player, null);
    }

    public String serialize(final Component input) {
        return MINI_MESSAGE.serialize(input.decoration(TextDecoration.ITALIC, false));
    }

    public String formatPlaceholders(final String input, final Object... placeholders) {
        if (placeholders.length % 2 != 0) {
            return input;
        }

        final Map<String, Object> placeholderMap = new HashMap<>();
        for (int i = 0; i < placeholders.length; i += 2) {
            if (placeholders[i] instanceof String) {
                placeholderMap.put((String) placeholders[i], placeholders[i + 1]);
            }
        }

        return replacePlaceholders(input, placeholderMap);
    }

    private String replacePlaceholders(final String input, final Map<String, Object> placeholderMap) {
        final StringBuilder result = new StringBuilder(input);

        for (final Map.Entry<String, Object> entry : placeholderMap.entrySet()) {
            final String placeholder = entry.getKey();
            final Object value = entry.getValue();

            int index = result.indexOf(placeholder);
            while (index != -1) {
                result.replace(index, index + placeholder.length(), value.toString());
                index = result.indexOf(placeholder, index + value.toString().length());
            }
        }

        return result.toString();
    }

    public String formatLocation(final Location input, final boolean longVersion) {
        return "X: " + input.getBlockX() + (longVersion ? " Y: " + input.getBlockY() : "") + " Z: " + input.getBlockZ();
    }

    public String getTranslationForRank(final KingdomRank rank) {
        final KingdomConfig config = BetterKingdoms.getInstance().getConfigManager().getKingdomConfig();

        String name = config.getMemberName();

        switch (rank) {
            case OWNER -> name = config.getOwnerName();
            case COOWNER -> name = config.getCoownerName();
            case OFFICER -> name = config.getOfficerName();
            case KNIGHT -> name = config.getKnightName();
        }

        return name;
    }
}
