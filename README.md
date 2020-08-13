# garrulity_minecraft
## 概要
minecraftの bukkit/spigot サーバーで利用できるサーバープラグインです。
[人狼ジャッジメント](https://www.sorairo.jp/jrvs.html) に登場する「饒舌な人狼」をモチーフとし、プレイヤーにキーワード（お題）を割り振ります。
夜明けまでにminecraft内のチャットにて、与えられたキーワード（お題）を含んだ発言を行ってください。
行わない場合、BANされます。
ただし、あまり露骨にキーワード（お題）を言うのは得策ではないでしょう。
他のプレイヤーにキーワードが知られてしまうと、「チャレンジ」をされて成功した場合、自分がBANされます。

## チャレンジ
`/word-challenge` コマンドを用いると「チャレンジ」をすることが出来ます。他のプレイヤーのキーワード（お題）が分かった場合、チャレンジを行ってください。
推測したキーワード（お題）が実際のキーワード（お題）と合致していれば以下の効果が付与されます。
ただし、チャレンジは前日のお題および当日のお題に対して有効なため、前日のログを見直し、前日のキーワード（お題）を後から推測することも出来ます。
- 当てられたプレイヤーのBAN
- 次の日のキーワード（お題）が自動的にクリアとなる
- 再生能力2が10分間付与

※ただし、次の日のお題が自動的にクリアとなるだけなので `/word-challenge` によって偶然、お題を当てられた場合、BANされます。

## 管理者ロール
プラグイン独自に「管理者ロール」が設けられています。管理者ロールではキーワードが割り振られず、BANされることもありません。
また、管理者ロールはプラグイン独自に設けているものですので、OP権限とは何の関係もなく、チートコマンドが使えるようになるわけでもありません。
ただし管理者ロールでも`/word-challenge` コマンドは使用可能です。

## 追加されるコマンド
このプラグインでは5つのコマンドが追加されます。

- word-challenge  
実行方法：`/word-challenge [Player] [Keyword]`  
実行可能：誰でも  
実行結果：チャレンジを行います。  
実行例：例えばプレイヤー「famas1219」の昨日または今日のお題が「建築」だと推測した場合、次のようにコマンドを入力します。  
`/word-challenge famas1219 建築`

- word-restart  
実行方法：`/word-restart`  
実行可能：オペレーター権限  
実行結果：次の日の朝にした上で、全員にキーワードを振り直します。

- word-setadmin  
実行方法：`/word-setadmin [Player]`  
実行可能：オペレーター権限  
実行結果：プラグイン独自の「管理者ロール」を付与します。

- word-deladmin  
実行方法：`/word-deladmin [Player]`  
実行可能：オペレーター権限  
実行結果：プラグイン独自の「管理者ロール」を剥奪します。  

- key  
実行方法：`/key [subCommand]`  
注意：このコマンドはデバッグ用のコマンドであり、デフォルトで使用不可となっています。基本的には使用しないでください。

## その他
- 初回起動時、Pluginフォルダ内に `config.yml` が生成されます。この中の `admin:` の項目では「管理者ロール」の手動追加が出来ます。
- `config.yml` の `word:` の項目ではキーワード（お題）一覧があります。この項目をカスタマイズすることでキーワード（お題）を追加または削除できます。
- `config.yml` の `maxchallenge:` の項目ではプレイヤーがチャレンジできる（`/word-challenge`を実行できる）上限回数が設定されています。
デフォルトでは3回です。
- `config.yml` を変更した後には必ず `/reload` コマンドでプラグインの再読み込みを行ってください。
- "garrulity"とは、「饒舌な」という意味です。

## 動作確認済環境
- Java 13.0.1  
CraftBukkit 1.15.2

## 遊び方の例
相性として、Discord等で会話しながら行うminecraftとは相性が悪いと思います。
一方でお互いが、チャットでしか会話できない、サバイバルワールドに導入すれば最大限、このプラグインで遊べるでしょう。

例）
