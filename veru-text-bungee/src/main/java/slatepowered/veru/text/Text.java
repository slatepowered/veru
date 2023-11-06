package slatepowered.veru.text;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.function.Consumer;

public class Text {

    private static final BungeeComponentTextSerializer SERIALIZER =
            BungeeTextSerializers.bungeeComponents();

    /** Send the given component to the given player as a chat message */
    public static void send(ProxiedPlayer player, TextComponent component) {
        player.sendMessage(SERIALIZER.serialize(component));
    }

    /** Send the given component to the given player as a chat message */
    public static void send(ProxiedPlayer player, TextComponent component, Consumer<BungeeTextSerializationContext> consumer) {
        player.sendMessage(SERIALIZER.serialize(component, consumer));
    }

    /** Send the given component to the given player */
    public static void send(ChatMessageType type, ProxiedPlayer player, TextComponent component) {
        player.sendMessage(type, SERIALIZER.serialize(component));
    }

    /** Send the given component to the given player */
    public static void send(ChatMessageType type, ProxiedPlayer player, TextComponent component, Consumer<BungeeTextSerializationContext> consumer) {
        player.sendMessage(type, SERIALIZER.serialize(component, consumer));
    }

    /** Log the given component to the console */
    public static void log(TextComponent component) {
        ProxyServer.getInstance().getConsole().sendMessage(SERIALIZER.serialize(component));
    }

    /** Log the given component to the console */
    public static void log(TextComponent component, Consumer<BungeeTextSerializationContext> consumer) {
        ProxyServer.getInstance().getConsole().sendMessage(SERIALIZER.serialize(component, consumer));
    }

    /** Convert the given Veru component to a BungeeCord BaseComponent */
    public static BaseComponent convert(TextComponent component) {
        return SERIALIZER.serialize(component);
    }

}
