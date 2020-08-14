package kun.garrulity.garrulity_jinro;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.util.StringUtil;

import java.util.*;

public class debug_key implements CommandExecutor,TabCompleter {
    private final Main m;
    public debug_key(Main a){
        m=a;
    }

    private static final String[] subCommand={"nextRound","skipDay","setAdmin","break","effect"};

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

        sender.sendMessage(args[0]+"を実行しました。");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length<=1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(subCommand), completions);
            Collections.sort(completions);
        }
        return completions;
    }
}
