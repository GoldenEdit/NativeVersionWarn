package dev.goldenedit.nativeversionwarn;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class NativeVersionWarn extends JavaPlugin implements Listener {

    private final ViaAPI<Player> viaAPI = Via.getAPI();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final FileConfiguration config = this.getConfig();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Player player = event.getPlayer();
            int playerProtocolVersion = viaAPI.getPlayerVersion(player);
            int serverProtocolVersion = config.getInt("protocol-version");
            String serverVersion = Bukkit.getBukkitVersion().split("-")[0];
            String messageTemplate = ChatColor.translateAlternateColorCodes('&', config.getString("warning-message", "Your Minecraft version is outdated! Please use version %s."));
            String message = String.format(messageTemplate, serverVersion);

            if (playerProtocolVersion < serverProtocolVersion) {
                player.sendMessage(message);
                getLogger().info("Player " + player.getName() + " connected with outdated client version " + playerProtocolVersion);
            }
        }, 20L);  // Delay of 20 ticks (1 second)
    }
}
