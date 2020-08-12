package kun.garrulity.garrulity_jinro;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class challenge implements CommandExecutor {
    private final Main m;
    public challenge(Main a){
        m=a;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length!=2){return false;}
        Player p=Bukkit.getPlayer(args[0]);
        if(p==null){
            sender.sendMessage("[ERROR]プレイヤー名を正しく認識できませんでした");
            return false;
        }
        if(m.challenge(p,args[1])){
            Bukkit.broadcastMessage("[@GM]"+sender+"さんが"+args[0]+"さんのキーワードチャレンジに成功しました。");
            m.putSavedList((Player)sender);
        }
        return true;
    }
}