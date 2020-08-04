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
    private static List<String> word;
    private static int max;
    static Map<UUID, String> keyword_map;
    static Map<UUID, String> old_keyword_map;
    static ArrayList<UUID> word_clear;

    public Main(){keyword_map = new HashMap<>();}

    @Override
    public void onEnable(){
        word = getConfig().getStringList("word");
        max=word.size();

        getServer().getPluginManager().registerEvents(new chat(this), this);
        getLogger().info("Success - Garrulity_Jinro");

        getCommand("key").setExecutor(new debug_key(this));
        getCommand("word-restart").setExecutor(new word_restart(this));
    }


    public String getKeyword(Player p){
        String p_key=keyword_map.get(p.getUniqueId());
        if(p_key==null){
            Random rand = new Random();
            int k=rand.nextInt(max);
            p_key=word.get(k);
            keyword_map.put(p.getUniqueId(),p_key);
        }
        return p_key;
    }
    public boolean isKeyword(Player p,String keyword_challenge){
        return keyword_map.get(p.getUniqueId())==keyword_challenge||old_keyword_map.get(p.getUniqueId())==keyword_challenge;
    }
    public void resetKeyword(){
        keyword_map.clear();
        old_keyword_map.clear();
        word_clear.clear();
    }
    public void doClear(Player p){
        word_clear.add(p.getUniqueId());
    }
    public void nextRound(){
        for(UUID id:keyword_map.keySet()){
            if(!word_clear.contains(id)){
                Bukkit.getBanList(BanList.Type.NAME).addBan("kasumigaura",null,null,"キーワードを入力できなかったため");
                Bukkit.getPlayer(id).kickPlayer("キーワードを入力できなかったため");
            }
        }
    }
}
