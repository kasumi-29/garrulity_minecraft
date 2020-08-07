package kun.garrulity.garrulity_jinro;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Objects;

public class word_admin implements CommandExecutor {
    private final Main m;
    public word_admin(Main a){m=a;}

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length!=1){return false;}
        Player p=m.getServer().getPlayer(args[0]);
        if(p==null){
            sender.sendMessage("[@GM]"+args[0]+"さんは接続中のプレイヤーとして認識できませんでした。");
            return false;
        }else if (cmd.getName().equals("word-setadmin")){
            m.setAdmin(p);
            sender.sendMessage("[@GM]"+args[0]+"さんを管理者ロールに追加しました。");
            p.sendMessage("[@GM]あなたは管理者ロールに追加されました。");
        }else if(cmd.getName().equals("word-deladmin")){
            m.delAdmin(p);
            sender.sendMessage("[@GM]"+args[0]+"さんを管理者ロールから削除しました。");
            p.sendMessage("[@GM]あなたの管理者ロールは剥奪されました。次の朝よりお題が出題されます。");
            m.getKeyword(p);
            m.doClear(p);
        }else{
            sender.sendMessage("[@GM]コマンドが正しくありません（word_admin）");
            return false;
        }
        return true;
    }
}
