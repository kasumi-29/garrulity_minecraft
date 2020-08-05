package kun.garrulity.garrulity_jinro;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.world.*;

public class handle implements Listener {
    private final Main m;
    public handle(Main a){
        m=a;
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event){
        m.skipDay();
        Bukkit.broadcastMessage("[@GM]時間がスキップしたため、BAN者はいませんでした。");
        m.nextRound();
    }

}
