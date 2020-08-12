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

    }
    //今後の拡張用にclass自体は残しておきますが、現在は使用していません。

}
