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
    /**
     * プレイヤーのUUIDと現在のチャレンジ回数を保持するMap.
     * <p>
     * 日が明けてから一度もチャレンジしていない場合、そもそも登録されていない<br>
     * Integerが取る値は、1,2,3...,macchallengeのいずれか<br>
     * </p>
     */
    private HashMap<UUID, Integer> challengelog;
    private World def;

    /**
     * プラグインの起動処理.
     * <p>変数の初期化、TabCompleterの登録、registerEventsの登録、Executorの登録など</p>
     */
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
        getLogger().info("Success - Garrulity_Jinro");
    }

    /**
     * 管理者ロールかどうか確かめる.
     * @param p プレイヤーオブジェクト
     * @return 管理者ロールかどうかの真偽値
     */
    public boolean isAdmin(Player p){
        return admin.contains(p.getName());
    }

    /**
     * 管理者ロールに追加する.
     * @param p プレイヤーオブジェクト
     */
    public void setAdmin(Player p){
        if(admin.add(p.getName())) {
            getConfig().set("admin", new ArrayList<>(admin));
            saveConfig();
            keyword_map.remove(p.getUniqueId());
            old_keyword_map.remove(p.getUniqueId());
        }
    }

    /**
     * 管理者ロールから削除する.
     * @param p プレイヤーオブジェクト
     */
    public void delAdmin(Player p){
        if(admin.remove(p.getName())) {
            getConfig().set("admin", new ArrayList<>(admin));
            saveConfig();
        }
    }

    /**
     * チャレンジが成功した人をListに登録する.
     * @param p プレイヤーオブジェクト
     */
    public void putSavedList(Player p){
        saved.add(p.getUniqueId());
    }

    /**
     * キーワード（お題）を取得する.
     * @param p プレイヤーオブジェクト
     * @return 割り当てられた（または割り当て済の）キーワード
     */
    public String getKeyword(Player p){
        String p_key=keyword_map.get(p.getUniqueId());
        if(p_key==null){
            p_key=putKeyword();
            keyword_map.put(p.getUniqueId(),p_key);
        }
        return p_key;
    }

    /**
     * ランダムにキーワードを取得する.
     * @return キーワード
     */
    public String putKeyword(){
        Random rand = new Random();
        int k=rand.nextInt(max);
        return word.get(k);
    }

    /**
     * キーワードが一致しているかを判定する.
     * @param p チャレンジされるプレイヤーオブジェクト
     * @param keyword_challenge チャレンジするキーワード
     * @return チャレンジが成功したかどうか
     */
    public boolean isKeyword(Player p,String keyword_challenge){
        return keyword_map.get(p.getUniqueId()).equals(keyword_challenge) || old_keyword_map.get(p.getUniqueId()).equals(keyword_challenge);
    }

    /**
     * チャレンジ可能か判定する.
     * @param p 判定されるプレイヤー
     * @return チャレンジ可能ならfalseを返す
     */
    public boolean isnotChallengeLog(Player p){
        Integer player_challenge=challengelog.get(p.getUniqueId());
        if(player_challenge==null){return false;}
        return player_challenge >= maxchallenge;
    }

    /**
     * チャレンジ可能な最大回数を取得する.
     * @return チャレンジ可能な最大回数
     */
    public int getMaxchallenge(){
        return maxchallenge;
    }

    /**
     * チャレンジ回数をカウントアップする.
     * <p>ただし最大回数を超えているかの判定は行っていないため、呼び出し側で判定すること。</p>
     * @param p カウントアップするプレイヤー
     */
    private void CountupChallenge(Player p){
        Integer player_challenge = challengelog.get(p.getUniqueId());
        if(player_challenge==null){player_challenge=1;}
        challengelog.put(p.getUniqueId(),player_challenge+1);
    }

    /**
     * チャレンジを行う.
     * @param fromP チャレンジを行うプレイヤーオブジェクト
     * @param toP チャレンジを行われるプレイヤー
     * @param keyword_challenge 予想したキーワード
     * @return チャレンジ最大回数より多い場合、またはチャレンジが失敗した場合、false。チャレンジが成功した場合trueを返す。
     */
    public boolean challenge(Player fromP,Player toP,String keyword_challenge){
        if(isnotChallengeLog(fromP)){return false;}
        CountupChallenge(fromP);
        if(isKeyword(toP,keyword_challenge)){
            ban(toP,"キーワードを知られてしまったため");
            return true;
        }
        return false;
    }

    /**
     * キーワードマップを削除する.
     * <p>デバッグ用に存在しているだけのため、外部からの呼び出しは非推奨</p>
     * @deprecated
     */
    public void AllResetKeyword(){
        keyword_map.clear();
        old_keyword_map.clear();
        word_clear.clear();
    }

    /**
     * BANを行う.
     * <p>
     * ただし、管理者ロールを指定した場合、処理は行われない<br>
     * また、カウントアップと全員へのBAN結果通知も同時に行う<br>
     * さらにkeyword_mapなどのメモリ上に展開されている変数から、プレイヤーの情報を完全に削除する
     * </p>
     * @param p BANするプレイヤーオブジェクト
     * @param reason BAN理由となる文字列を入力する
     */
    private void ban(Player p,String reason){
        if(isAdmin(p)){return;}
        Bukkit.getBanList(BanList.Type.NAME).addBan(p.getUniqueId().toString(), ChatColor.WHITE+reason, null, null);
        Objects.requireNonNull(p).kickPlayer(reason);
        count++;
        keyword_map.remove(p.getUniqueId());
        old_keyword_map.remove(p.getUniqueId());
        word_clear.remove(p.getUniqueId());
        saved.remove(p.getUniqueId());
        challengelog.remove(p.getUniqueId());
        Bukkit.broadcastMessage("[@GM]"+p.getPlayerListName()+"さんがBANされました。");
    }

    /**
     * 日付を更新するために、全員がキーワード（お題）をクリアしたことにする.
     */
    public void skipDay(){word_clear.addAll(keyword_map.keySet());}

    /**
     * プレイヤーをクリアさせる.
     * @param p クリアさせるプレイヤーオブジェクト
     */
    public void doClear(Player p){word_clear.add(p.getUniqueId());}

    /**
     * クリアしているかどうか判定する.
     * <p>プレイヤーオブジェクトを用いるオーバーロード関数</p>
     * @param p プレイヤーオブジェクト
     * @return クリアしているかどうかの真偽値
     */
    public boolean isClear(Player p){return isClear(p.getUniqueId());}

    /**
     * クリアしているかどうか判定する.
     * @param id プレイヤーのユニークID
     * @return クリアしているかどうかの真偽値
     */
    public boolean isClear(UUID id){return word_clear.contains(id);}

    /**
     * 毎Tick実行し、夜明けを判定する.
     */
    public void observe(){
        if(def.getTime()==1L){
            nextRound();
        }
    }

    /**
     * 朝が来たら実行する関数.
     * <p>BAN処理、新たなキーワード（お題）の割り当て、チャレンジ報酬</p>
     */
    public void nextRound(){
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
        Bukkit.broadcastMessage("[@GM]朝になりました。本日のBAN者は"+count+"人です。");
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
