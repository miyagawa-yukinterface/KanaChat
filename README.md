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

このプラグインは、カスタム辞書を `plugins/KanaChat/dictionary.yml` に保存します。
`entries` の各要素に、日本語の単語 (`word`) とローマ字読みの配列 (`readings`) を記載します。

The plugin stores custom dictionary in `plugins/KanaChat/dictionary.yml`.
Each entry contains a Japanese word (`word`) and a list of romaji readings
(`readings`).

```yaml
entries:
  - word: 漢字
    readings:
      - kanji
      - かんじ
```

by Google Translate