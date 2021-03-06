package kun.garrulity.garrulity_jinro;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class word_admin implements CommandExecutor,TabCompleter {
    private final Main m;
    public word_admin(Main a){m=a;}

    /**
     * /word-setadmin /word-deladmin をキャッチする.
     * <p>管理者ロールの付与および剥奪を行う</p>
     */
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length!=1){return false;}
        Player p=m.getServer().getPlayer(args[0]);
        if(p==null){
            sender.sendMessage("[@GM]"+args[0]+"さんは接続中のプレイヤーとして認識できませんでした。");
            sender.sendMessage("[@GM]オフラインプレイヤーを対象に観戦者ロールの操作する場合は、configファイルを編集してください。");
            return false;
        }else if (cmd.getName().equals("word-setadmin")){
            m.setAdmin(p);
            sender.sendMessage("[@GM]"+args[0]+"さんを観戦者ロールに追加しました。");
            p.sendMessage("[@GM]あなたは観戦者ロールに追加されました。");
            m.getLogger().info("観戦者ロールに追加："+args[0]);
        }else if(cmd.getName().equals("word-deladmin")){
            m.delAdmin(p);
            sender.sendMessage("[@GM]"+args[0]+"さんを観戦者ロールから削除しました。");
            p.sendMessage("[@GM]あなたの観戦者ロールは剥奪されました。次の朝よりお題が出題されます。");
            m.getLogger().info("観戦者ロールから削除："+args[0]);
            m.getKeyword(p);
            m.doClear(p);
        }else{
            sender.sendMessage("[@GM]コマンドが正しくありません（word_admin）");
            return false;
        }
        return true;
    }

    /**
     * /word-setadmin /word-deladmin のTabキーによる補完を行う.
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length<=1) {
            return null;
        }
        return new ArrayList<>();
    }
}
