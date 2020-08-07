package kun.garrulity.garrulity_jinro;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

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
            default:
                sender.sendMessage("該当コマンドが見つかりません");
                return false;
        }


        return true;
    }
}
