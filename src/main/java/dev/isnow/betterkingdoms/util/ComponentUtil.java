package dev.isnow.betterkingdoms.util;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

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

    public Component deserialize(String input, final Player player) {
        if(player != null && BetterKingdoms.getInstance().isUsePlaceholderAPI()) {
            input = PlaceholderAPI.setPlaceholders(player, input);
        }

        return MINI_MESSAGE.deserialize(input).decoration(TextDecoration.ITALIC, false);
    }

    public String serialize(final Component input) {
        return MINI_MESSAGE.serialize(input.decoration(TextDecoration.ITALIC, false));
    }
}
