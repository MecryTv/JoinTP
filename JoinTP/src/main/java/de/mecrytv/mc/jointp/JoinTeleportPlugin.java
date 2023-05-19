package de.mecrytv.mc.jointp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinTeleportPlugin extends JavaPlugin implements Listener {

    private Location spawnLocation;

    @Override
    public void onEnable() {
        // Plugin-Initialisierung
        saveDefaultConfig();
        loadSpawnLocation();

        getServer().getPluginManager().registerEvents(this, this);
    }

    private void loadSpawnLocation() {
        // Lädt die Spawn-Position aus der Konfiguration
        if (getConfig().contains("spawn.world") && getConfig().contains("spawn.x")
                && getConfig().contains("spawn.y") && getConfig().contains("spawn.z")) {
            String worldName = getConfig().getString("spawn.world");
            double x = getConfig().getDouble("spawn.x");
            double y = getConfig().getDouble("spawn.y");
            double z = getConfig().getDouble("spawn.z");
            float yaw = (float) getConfig().getDouble("spawn.yaw");
            float pitch = (float) getConfig().getDouble("spawn.pitch");
            spawnLocation = new Location(getServer().getWorld(worldName), x, y, z, yaw, pitch);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(spawnLocation);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location currentLocation = player.getLocation();
                setSpawnLocation(currentLocation);
                player.sendMessage("Spawn erfolgreich gesetzt!");
            } else {
                sender.sendMessage("Dieser Befehl kann nur von einem Spieler verwendet werden.");
            }
            return true;
        }
        return false;
    }

    private void setSpawnLocation(Location location) {
        getConfig().set("spawn.world", location.getWorld().getName());
        getConfig().set("spawn.x", location.getX());
        getConfig().set("spawn.y", location.getY());
        getConfig().set("spawn.z", location.getZ());
        getConfig().set("spawn.yaw", location.getYaw());
        getConfig().set("spawn.pitch", location.getPitch());
        saveConfig();
        loadSpawnLocation();
    }
}
