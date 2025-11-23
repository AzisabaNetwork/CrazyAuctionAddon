# CrazyAuctionAddon
**Version:** 1.1.0<br>
**Native Minecraft Version:** 1.16.5<br>
**Author:** pino223<br>
**LICENSE:** [GPL-3.0](LICENSE)<br>
## 概要
[AuctionHouse](https://github.com/Shock95/AuctionHouse)に出品制限をつけるアドオン<br>
## 設定
### config.yml
`blocked-names:`<br>
指定した文字列が名前に含まれている場合出品を不可にします。<br>
`blocked-lores:`<br>
指定した文字列が説明文に含まれている場合出品を不可にします。<br>

どちらも複数指定可能です。
## コマンド
`/caa set`<br>
取引制限設定用のGUIを開きます。<br>
ガラス板にアイテムを置くことでアイテムの出品制限を編集できます。<br>
エメラルドブロックをクリックすることで出品不可に設定できます。<br>
もう一度ブロックをクリックすると再び出品可能にできます。<br>
設定したアイテムは blocked-items.yml に保存されます。<br>
スタックできるアイテムを設定するときは１つだけを設定するようにしてください。<br>
一度GUIに入れたアイテムは帰ってこないので注意。<br>
<img width="421" height="92" alt="Image" src="https://github.com/user-attachments/assets/82649c92-164f-455c-9ec5-43375c857fce" /><br>
`/caa noselllist <number>`<br>
今までに取引禁止設定をしたアイテム一覧を表示します。<br>
任意でページを指定できます。<br>
<img width="329" height="252" alt="Image" src="https://github.com/user-attachments/assets/2c91e734-019d-4ac7-90dd-486842bf0249" /><br>
`/caa reload`<br>
設定をリロードします。
## 権限
crazyauctionaddon.use
