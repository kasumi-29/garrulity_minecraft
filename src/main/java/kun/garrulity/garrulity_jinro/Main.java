package kun.garrulity.garrulity_jinro;

import org.bukkit.*;
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
    private int count;
    private int maxchallenge;
    private HashMap<UUID, Integer> challengelog;
    private World def;

    @Override
    public void onEnable(){
        keyword_map=new HashMap<>();
        old_keyword_map=new HashMap<>();
        word_clear=new ArrayList<>();
        saved=new ArrayList<>();
        count=0;
        challengelog=new HashMap<>();
        def=Bukkit.getWorlds().get(0);

        saveDefaultConfig();
        word=getConfig().getStringList("word");
        max=word.size();
        admin=new HashSet<>(getConfig().getStringList("admin"));
        maxchallenge=getConfig().getInt("maxchallenge");

        getServer().getPluginManager().registerEvents(new chat(this), this);
        getLogger().info("Success - Garrulity_Jinro");

        Objects.requireNonNull(getCommand("key")).setExecutor(new debug_key(this));
        Objects.requireNonNull(getCommand("key")).setTabCompleter(new debug_key(this));
        Objects.requireNonNull(getCommand("word-restart")).setExecutor(new word_restart(this));
        Objects.requireNonNull(getCommand("word-restart")).setTabCompleter(new word_restart(this));
        Objects.requireNonNull(getCommand("word-setadmin")).setExecutor(new word_admin(this));
        Objects.requireNonNull(getCommand("word-setadmin")).setTabCompleter(new word_admin(this));
        Objects.requireNonNull(getCommand("word-deladmin")).setExecutor(new word_admin(this));
        Objects.requireNonNull(getCommand("word-deladmin")).setTabCompleter(new word_admin(this));
        Objects.requireNonNull(getCommand("word-challenge")).setExecutor(new challenge(this));
        Objects.requireNonNull(getCommand("word-challenge")).setTabCompleter(new challenge(this));
        getServer().getPluginManager().registerEvents(new handle(this), this);

        getServer().getScheduler().runTaskTimer(this, this::observe, 1, 1);
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
    public boolean isChallengeLog(Player p){
        Integer player_challenge=challengelog.get(p.getUniqueId());
        if(player_challenge==null){return false;}
        return player_challenge >= maxchallenge;
    }
    public int getMaxchallenge(){
        return maxchallenge;
    }
    private void CountupChallenge(Player p){
        Integer player_challenge = challengelog.get(p.getUniqueId());
        if(player_challenge==null){player_challenge=0;}
        challengelog.put(p.getUniqueId(),player_challenge+1);
    }
    public boolean challenge(Player fromP,Player toP,String keyword_challenge){
        if(isChallengeLog(fromP)){return false;}
        CountupChallenge(fromP);
        if(isKeyword(toP,keyword_challenge)){
            ban(toP,"キーワードを知られてしまったため");
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
        word_clear.remove(p.getUniqueId());
        saved.remove(p.getUniqueId());
        challengelog.remove(p.getUniqueId());
        Bukkit.broadcastMessage("[@GM]"+p.getPlayerListName()+"さんがBANされました。");
    }
    public void skipDay(){word_clear.addAll(keyword_map.keySet());}
    public void doClear(Player p){word_clear.add(p.getUniqueId());}
    public boolean isClear(Player p){return isClear(p.getUniqueId());}
    public boolean isClear(UUID id){return word_clear.contains(id);}
    public void observe(){
        if(def.getTime()==1L){
            nextRound();
        }
    }
    public void nextRound(){//朝が来ると実行
        //Todo 朝が来ると実行するように
        old_keyword_map.clear();
        old_keyword_map.putAll(keyword_map);
        keyword_map.clear();
        challengelog.clear();
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
