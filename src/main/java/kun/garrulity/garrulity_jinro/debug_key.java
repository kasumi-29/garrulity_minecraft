package kun.garrulity.garrulity_jinro;

import org.bukkit.Bukkit;
import org.bukkit.command.*;

public class debug_key implements CommandExecutor {
    private final Main m;
    public debug_key(Main a){
        m=a;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

        m.nextRound();

        return true;
    }
}
