package test.slatepowered.veru;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import slatepowered.veru.text.*;

public class VeruTestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // send message to console
        TextComponent component = TextComponent.text("hey guys its ")
                .append(TextComponent.text("quandale dingle ").color(TextColors.GOLD))
                .append(TextComponent.text("here").color(TextColors.AQUA).underline(true)
                        .add(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "showquandaledingle"))
                        .add(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.text("click to show quandale dingle").color(TextColors.RED))));
        Bukkit.getConsoleSender().sendMessage(BungeeTextSerializers.legacyText().serialize(component));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        final String cmd = "/gamemode 0 " + player.getName();
        Text.send(player, TextComponent.parse("&8[&a+&8] &[plr]&b" + player.getName() + "&3 joined the game")
                .edit("plr", c -> c.add(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        TextComponent.parse("" +
                        "&7&l█ &3Player &b" + player.getName() + "\n" +
                        "&8&l█ &6XYZ: &e" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + "\n" +
                        "&8&l█ &6Gamemode: &e" + player.getGameMode() + "\n" +
                        "&8&l█ &cClick to run &f" + cmd)
                )).add(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd)))
        );
    }

}
