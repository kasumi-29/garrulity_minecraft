package kun.garrulity.garrulity_jinro;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

public class challenge implements CommandExecutor {
    private final Main m;
    public challenge(Main a){
        m=a;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length!=2){return false;}
        Player p=Bukkit.getPlayer(args[0]);
        if(p==null){
            sender.sendMessage("[ERROR]プレイヤー名を正しく認識できませんでした。");
            return false;
        }
        if(m.isChallengeLog((Player) sender)){
            sender.sendMessage("[@GM]本日分のチャレンジ上限に達しました。");
            return false;
        }
        if(((Player) sender).getUniqueId().equals(p.getUniqueId())){
            sender.sendMessage("[ERROR]自分自身にチャレンジはできません。");
            return false;
        }
        if(m.challenge((Player)sender,p,args[1])){
            Bukkit.broadcastMessage("[@GM]"+sender+"さんが"+args[0]+"さんのキーワードチャレンジに成功しました。");
            m.putSavedList((Player)sender);
            ((LivingEntity) sender).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,10*60*20,2,false,false,true));
        }else{
            sender.sendMessage("[@GM]キーワードが違います。");
        }
        return true;
    }
}
