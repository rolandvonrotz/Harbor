package xyz.nkomarn.Harbor.task;

import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.nkomarn.Harbor.util.Config;
import xyz.nkomarn.Harbor.util.Message;

public class AccelerateNightTask extends BukkitRunnable {
    private final World world;

    public AccelerateNightTask(final World world) {
        this.world = world;
    }

    @Override
    public void run() {
        final long time = world.getTime();
        if (!(time >= 450 && time <= 1000)) {
            world.setTime(time + Config.getInteger("values.timeSkipInterval"));
        } else {
            // Announce night skip and clear queue
            Message.SendRandomChatMessage(world, "messages.chat.skipped");
            Checker.skippingWorlds.remove(world);

            // Reset sleep statistic if phantoms are disabled
            if (!Config.getBoolean("features.phantoms")) {
                world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
            }

            // Clear weather
            if (Config.getBoolean("features.weather")) {
                world.setStorm(false);
                world.setThundering(false);
            }

            this.cancel();
        }
    }
}
