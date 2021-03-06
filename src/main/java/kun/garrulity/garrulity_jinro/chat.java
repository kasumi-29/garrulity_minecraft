package kun.garrulity.garrulity_jinro;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class chat implements Listener {
    private final Main m;
    public chat(Main a){m=a;}

    /**
     * プレイヤーがワールドに入った際の処理を行う.
     * @param event プレイヤーが入った際のイベント
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        m.getServer().getScheduler().runTaskLater(m, () -> {
            Player p = event.getPlayer();
            if(!m.isAdmin(p)) {
                p.sendMessage("[@GM]ようこそ。");
                p.sendMessage("[@GM]本日のキーワードは「" + m.getKeyword(p) + "」です。");
                if(m.isClear(p)){
                    p.sendMessage("[@GM]すでに本日のキーワードはCLEAR済です。");
                }
            }else{
                p.sendMessage("[@GM]あなたは観戦者ロールのため、お題は出題されません。");
                p.sendMessage("[@GM]観戦者ロールを外れるには /word-deladmin を実行してください。");
            }
            p.sendMessage("[@GM]他のプレイヤーのお題が分かったら、 /word-challenge コマンドを実行してみてください。");
        },1L);
    }

    /**
     * チャットを送信した時点で、キーワード（お題）がクリアしたかどうか判定する.
     * @param event 非同期のチャットイベント
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String try_word=event.getMessage();
        if(try_word.contains(m.getKeyword(event.getPlayer()))) {
            event.getPlayer().sendTitle("お題CLEAR！","",10,70,20);
            m.doClear(event.getPlayer());
        }
    }


}
