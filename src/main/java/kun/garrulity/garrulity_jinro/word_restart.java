package kun.garrulity.garrulity_jinro;

import org.bukkit.*;
import org.bukkit.command.*;

import java.util.*;


public class word_restart implements CommandExecutor,TabCompleter {
    private final Main m;
    public word_restart(Main a){
        m=a;
    }

    /**
     * /word-restart をキャッチする.
     * <p>なお、呼び出された場合は時刻が2にセットされる</p>
     */
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        m.skipDay();
        for(World w:Bukkit.getWorlds()){
            w.setTime(2L);
        }
        m.nextRound();
        return true;
    }

    /**
     * /word-restart のTabキーによる補完を行う.
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return new ArrayList<>();
    }
}
