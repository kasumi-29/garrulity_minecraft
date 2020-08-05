package kun.garrulity.garrulity_jinro;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class chat implements Listener {
    private final Main m;
    public chat(Main a){m=a;}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.getPlayer().sendMessage("[@GM]本日のキーワードは「"+m.getKeyword(event.getPlayer())+"」です");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String try_word=event.getMessage();
        if(try_word.contains(m.getKeyword(event.getPlayer()))) {
            event.getPlayer().sendTitle("お題CLEAR！","",10,70,20);
            m.doClear(event.getPlayer());
        }
    }


}
