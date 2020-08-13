package kun.garrulity.garrulity_jinro;

import org.bukkit.command.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class debug_key implements CommandExecutor {
    private final Main m;
    public debug_key(Main a){
        m=a;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length<1){
            sender.sendMessage("引数が足りません");
            return false;
        }
        switch (args[0]){
            case "nextRound":
                m.nextRound();
                break;
            case "skipDay":
                m.skipDay();
                break;
            case "setAdmin":
                m.setAdmin((Player) sender);
                break;
            case "break":
                m.AllResetKeyword();
                break;
            case "effect":
                ((LivingEntity) sender).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,10*60*20,2,false,false,true));
                break;
            default:
                sender.sendMessage("該当コマンドが見つかりません");
                return false;
        }


        return true;
    }
}
