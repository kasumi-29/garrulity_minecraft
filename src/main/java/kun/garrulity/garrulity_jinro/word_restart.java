package kun.garrulity.garrulity_jinro;

import org.bukkit.command.*;

public class word_restart implements CommandExecutor {
    private final Main m;
    public word_restart(Main a){
        m=a;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        System.out.println(m.keyword_map);
        return true;
    }
}
