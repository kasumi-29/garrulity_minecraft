package kun.garrulity.garrulity_jinro;

import org.bukkit.*;
import org.bukkit.command.*;

import java.util.*;


public class word_restart implements CommandExecutor,TabCompleter {
    private final Main m;
    public word_restart(Main a){
        m=a;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        m.skipDay();
        for(World w:Bukkit.getWorlds()){
            w.setTime(1L);
        }
        m.nextRound();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return new ArrayList<>();
    }
}
