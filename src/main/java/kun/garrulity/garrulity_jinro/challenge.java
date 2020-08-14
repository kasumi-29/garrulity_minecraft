package kun.garrulity.garrulity_jinro;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public class challenge implements CommandExecutor,TabCompleter {
    private final Main m;
    public challenge(Main a){
        m=a;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length!=2){return false;}
        if(!(sender instanceof Player)){
            sender.sendMessage("[ERROR]プレイヤーのみが実行可能です。");
            return false;
        }
        Player p=Bukkit.getPlayer(args[0]);
        if(p==null){
            sender.sendMessage("[ERROR]プレイヤー名を正しく認識できませんでした。");
            return false;
        }
        if(m.isChallengeLog((Player) sender)){
            sender.sendMessage("[@GM]1日"+m.getMaxchallenge()+"回までしかチャレンジできません。");
            return true;
        }
        if(((Player) sender).getUniqueId().equals(p.getUniqueId())){
            sender.sendMessage("[ERROR]自分自身にチャレンジはできません。");
            return true;
        }
        getLogger().info("Challenge "+sender.getName()+ " to "+args[0]+":"+args[1]);
        if(m.challenge((Player)sender,p,args[1])){
            Bukkit.broadcastMessage("[@GM]"+sender+"さんが"+args[0]+"さんのキーワードチャレンジに成功しました。");
            m.putSavedList((Player)sender);
            ((LivingEntity) sender).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,10*60*20,2,false,false,true));
        }else{
            sender.sendMessage("[@GM]キーワードが違います。");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length<=1) {
            return null;
        }else{
            return new ArrayList<>();
        }
    }
}
