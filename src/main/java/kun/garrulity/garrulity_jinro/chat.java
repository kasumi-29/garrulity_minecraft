package kun.garrulity.garrulity_jinro;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class chat implements Listener {
    private final Main m;
    public chat(Main a){m=a;}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player p=event.getPlayer();
        if(!m.isAdmin(event.getPlayer())) {
            p.sendMessage("[@GM]本日のキーワードは「" + m.getKeyword(p) + "」です。");
            if(m.isClear(p)){
                p.sendMessage("[@GM]すでに本日のキーワードはCLEAR済です。");
            }
        }else{
            p.sendMessage("[@GM]あなたは管理者ロールのため、お題は出題されません。");
            p.sendMessage("[@GM]管理者ロールを外れるには /word-deladmin を実行してください。");
        }
        p.sendMessage("[@GM]他のプレイヤーのお題が分かったら、 /word-challenge コマンドを実行してみてください。");
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
