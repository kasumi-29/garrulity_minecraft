package kun.garrulity.garrulity_jinro;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.world.*;

public class handle implements Listener {
    private final Main m;
    public handle(Main a){
        m=a;
    }
    public static boolean timeset=true;

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event){
        //
        //if(timeset) {
        //    m.getServer().getScheduler().runTask(m, () -> {
        //        m.getLogger().info(String.valueOf(event.getWorld().getTime()));
        //        timeset=!timeset;
        //    });
        //    timeset=!timeset;
        //}
        //
    }
    //今後の拡張用にclass自体は残しておきますが、現在は使用していません。

}
