package kun.garrulity.garrulity_jinro;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.*;
import java.util.*;

public class Main extends JavaPlugin {
    private static List<String> word;//乱数で取得するため、Listのままで放置
    private static int max;
    private static HashSet<String> admin;
    private HashMap<UUID, String> keyword_map;
    private HashMap<UUID, String> old_keyword_map;
    private ArrayList<UUID> word_clear;
    private ArrayList<UUID> saved;
    private int count=0;

    @Override
    public void onEnable(){
        keyword_map=new HashMap<>();
        old_keyword_map=new HashMap<>();
        word_clear=new ArrayList<>();
        saved=new ArrayList<>();

        saveDefaultConfig();
        word=getConfig().getStringList("word");
        max=word.size();
        admin=new HashSet<>(getConfig().getStringList("admin"));

        getServer().getPluginManager().registerEvents(new chat(this), this);
        getLogger().info("Success - Garrulity_Jinro");

        Objects.requireNonNull(getCommand("key")).setExecutor(new debug_key(this));
        Objects.requireNonNull(getCommand("word-restart")).setExecutor(new word_restart(this));
        Objects.requireNonNull(getCommand("word-setadmin")).setExecutor(new word_admin(this));
        Objects.requireNonNull(getCommand("word-deladmin")).setExecutor(new word_admin(this));
        Objects.requireNonNull(getCommand("word-challenge")).setExecutor(new challenge(this));
        getServer().getPluginManager().registerEvents(new handle(this), this);
    }

    public boolean isAdmin(Player p){
        return admin.contains(p.getName());
    }
    public void setAdmin(Player p){
        if(admin.add(p.getName())) {
            getConfig().set("admin", new ArrayList<>(admin));
            saveConfig();
            keyword_map.remove(p.getUniqueId());
            old_keyword_map.remove(p.getUniqueId());
        }
    }
    public void delAdmin(Player p){
        if(admin.remove(p.getName())) {
            getConfig().set("admin", new ArrayList<>(admin));
            saveConfig();
        }
    }
    public void putSavedList(Player p){
        saved.add(p.getUniqueId());
    }
    public String getKeyword(Player p){
        String p_key=keyword_map.get(p.getUniqueId());
        if(p_key==null){
            p_key=putKeyword();
            keyword_map.put(p.getUniqueId(),p_key);
        }
        return p_key;
    }
    public String putKeyword(){
        Random rand = new Random();
        int k=rand.nextInt(max);
        return word.get(k);
    }
    public boolean isKeyword(Player p,String keyword_challenge){
        return keyword_map.get(p.getUniqueId()).equals(keyword_challenge) || old_keyword_map.get(p.getUniqueId()).equals(keyword_challenge);
    }
    public boolean challenge(Player p,String keyword_challenge){
        if(isKeyword(p,keyword_challenge)){
            ban(p,"キーワードを知られてしまったため");
            return true;
        }
        return false;
    }
    public void AllResetKeyword(){
        keyword_map.clear();
        old_keyword_map.clear();
        word_clear.clear();
    }
    private void ban(Player p,String reason){
        if(isAdmin(p)){return;}
        Bukkit.getBanList(BanList.Type.NAME).addBan(p.getUniqueId().toString(), reason, null, null);
        Objects.requireNonNull(p).kickPlayer(reason);
        count++;
        keyword_map.remove(p.getUniqueId());
        old_keyword_map.remove(p.getUniqueId());
        Bukkit.broadcastMessage("[@GM]"+p.getPlayerListName()+"さんがBANされました。");
    }
    public void skipDay(){word_clear.addAll(keyword_map.keySet());}
    public void doClear(Player p){word_clear.add(p.getUniqueId());}
    public boolean isClear(Player p){return isClear(p.getUniqueId());}
    public boolean isClear(UUID id){return word_clear.contains(id);}
    public void nextRound(){//朝が来ると実行
        old_keyword_map.clear();
        old_keyword_map.putAll(keyword_map);
        keyword_map.clear();
        for (UUID id : old_keyword_map.keySet()) {
            Player p=Bukkit.getPlayer(id);
            if(p==null){continue;}
            if(isAdmin(p)){
                p.sendMessage("[@GM]すでにあなたは管理者ロールです。");
                continue;
            }
            if (!isClear(id)) {//キーワードを入力できなかった人
                old_keyword_map.remove(id);
                ban(p,"キーワードを入力できなかったため");
            }else{
                String new_keyword=putKeyword();
                keyword_map.put(id,new_keyword);
            }
        }
        Bukkit.broadcastMessage("[@GM]本日のBAN者は"+count+"人です。");
        Bukkit.broadcastMessage("");
        //BAN通知の後にまとめてキーワードを送信する
        Bukkit.broadcastMessage("[@GM]おめでとうございます。何とか疑われずに生き残ったようですね！");
        for (UUID id:keyword_map.keySet()){
            Player p=Bukkit.getPlayer(id);
            if(p==null){continue;}
            p.sendMessage("[@GM]本日のあなたのキーワードは「"+keyword_map.get(id)+"」です。");
        }
        for(UUID id:saved){
            Player p=Bukkit.getPlayer(id);
            if(p==null){continue;}
            p.sendMessage("[@GM]キーワードチャレンジの報酬としてCLEARといたします。");
            p.sendTitle("お題CLEAR！","",10,70,20);
            doClear(p);
        }
        word_clear.clear();
        saved.clear();
        count=0;

    }
}
