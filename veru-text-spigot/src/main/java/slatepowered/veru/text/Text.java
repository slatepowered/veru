package slatepowered.veru.text;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class Text {

    private static final BungeeComponentTextSerializer SERIALIZER =
            BungeeTextSerializers.bungeeComponents();

    private static final MinecraftLegacyTextSerializer LEGACY_TEXT_SERIALIZER =
            BungeeTextSerializers.legacyText();

    /** Send the given component to the given player as a chat message */
    public static void send(Player player, TextComponent component) {
        player.spigot().sendMessage(SERIALIZER.serialize(component));
    }

    /** Send the given component to the given player as a chat message */
    public static void send(Player player, TextComponent component, Consumer<BungeeTextSerializationContext> consumer) {
        player.spigot().sendMessage(SERIALIZER.serialize(component, consumer));
    }

    /** Broadcast the given component to the whole server */
    public static void broadcast(TextComponent component) {
        Bukkit.spigot().broadcast(SERIALIZER.serialize(component));
    }

    /** Broadcast the given component to the whole server */
    public static void broadcast(TextComponent component, Consumer<BungeeTextSerializationContext> consumer) {
        Bukkit.spigot().broadcast(SERIALIZER.serialize(component, consumer));
    }

    /** Log the given component to the console */
    public static void log(TextComponent component) {
        Bukkit.getConsoleSender().sendMessage(LEGACY_TEXT_SERIALIZER.serialize(component));
    }

    /** Log the given component to the console */
    public static void log(TextComponent component, Consumer<TextSerializationContext<StringBuilder>> consumer) {
        Bukkit.getConsoleSender().sendMessage(LEGACY_TEXT_SERIALIZER.serialize(component, consumer));
    }

    /** Convert the given Veru component to a BungeeCord BaseComponent */
    public static BaseComponent convert(TextComponent component) {
        return SERIALIZER.serialize(component);
    }

}
