KanaChat
========

[![Circle CI](https://circleci.com/gh/fubira/KanaChat.svg?style=svg&circle-token=3392171f053e7a8452a9e5139fec383c5bdb11c0)](https://circleci.com/gh/fubira/KanaChat)

KanaChatはローマ字で入力されたチャット文章を自動的にかな・漢字に変換するBukkitプラグインです。  
KanaChat is plugin for bukkit that automatic conversion chat text to Kana and Kanji.  

![ScreenShot](https://i.imgur.com/z4vOY4H.png)

Compilation
-----------

* Install [Maven 3](http://maven.apache.org/download.html)
* Check out this repo and: `mvn clean package`

Dictionary
----------

このプラグインは、カスタム辞書を `plugins/KanaChat/dictionary.xlsx` に保存します。
各行には、`word` 列に日本語の単語、`reading` 列にそのローマ字読みが記載されています。

The plugin stores custom dictionary in `plugins/KanaChat/dictionary.xlsx`.
Each row contains a Japanese word in the `word` column and its romaji reading in
the `reading` column. 

by Google Translate