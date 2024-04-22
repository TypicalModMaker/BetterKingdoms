package dev.isnow.betterkingdoms.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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
        return MINI_MESSAGE.deserialize(input).decoration(TextDecoration.ITALIC, false);
    }

    public String serialize(final Component input) {
        return MINI_MESSAGE.serialize(input.decoration(TextDecoration.ITALIC, false));
    }
}
