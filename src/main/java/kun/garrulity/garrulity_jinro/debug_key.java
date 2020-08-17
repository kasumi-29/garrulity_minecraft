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

    /**
     * デバッグ用のサブコマンドを解析し、実行する.
     * <p>
     * デバッグで必要だったMain.java内の関数を直接実行できる<br>
     * また、プレイヤーのみしか実行できない（プレイヤー以外は弾く）<br><br>
     * subCommand一覧<br>
     *     nextRound:Main.javaのnextRound関数を実行<br>
     *     skipDay:Main.javaのskipDay関数を実行<br>
     *     setAdmin:Main.javaのsetAdmin関数に実行者のプレイヤーオブジェクトを渡す<br>
     *     break:Main.javaのAllResetKeyword関数を実行<br>
     *     effect:チャレンジ成功報酬のREGENERATIONエフェクトを実行者に付与する
     * </p>
     * @see Main#nextRound()
     * @see Main#skipDay()
     * @see Main#setAdmin(Player)
     * @see Main#AllResetKeyword()
     */
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length<1){
            sender.sendMessage("引数が足りません");
            return false;
        }
        if(!(sender instanceof Player)){
            sender.sendMessage("[ERROR]プレイヤーのみが実行可能です。");
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

    /**
     * デバッグ用のサブコマンドをTabキーで補完する.
     */
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
