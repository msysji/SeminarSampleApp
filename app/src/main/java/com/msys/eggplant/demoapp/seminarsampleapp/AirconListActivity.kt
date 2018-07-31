package com.msys.eggplant.demoapp.seminarsampleapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView

class AirconListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aircon_list)

        title = "エアコン一覧"

        // アプリが初回の起動か
        val pref = getSharedPreferences("datacheck", Context.MODE_PRIVATE)
        val hasData = pref.getBoolean("hasData", false)

        // アプリが初回起動の場合、エアコン情報データをDBに登録する
        if(!hasData) {
            registerAirconInfos()
            pref.edit().putBoolean("hasData", true).apply()
        }

        // エアコンリスト表示
        showAirconList()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 設定した内容的でリストを更新する
        if (requestCode == 1 // リクエストコードが一致している
                && resultCode == Activity.RESULT_OK // 結果がRESULT_OKである
        ){
            showAirconList()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack (true)
        finish()
    }

    private fun showAirconList() {
        val list = findViewById<ListView>(R.id.list_aircons)
        val aiconInfos = AirconDatabase(this).queryAirconInfos(this)

        list.adapter = AirconInfoAdapter(this, aiconInfos ) { airconInfo ->
            showOperation(airconInfo)
        }
    }

    // エアコン操作画面へ遷移
    private fun showOperation(airconInfo: AirconInfo) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("position", airconInfo.position)
        intent.putExtra("location", airconInfo.location)
        intent.putExtra("status", airconInfo.status)
        intent.putExtra("mode", airconInfo.mode)
        intent.putExtra("temp", airconInfo.temp)
        startActivityForResult(intent, 1)
    }

    // エアコン情報の初期登録
    private fun registerAirconInfos() {
        val airconInfos = mutableListOf<AirconInfo>()
        val airconInfo1 = AirconInfo(1 ,0, "リビング", "自動", 27)
        val airconInfo2 = AirconInfo(2, 0, "キッチン", "自動", 27)
        val airconInfo3 = AirconInfo(3, 0, "寝室", "自動", 27)
        airconInfos.add(airconInfo1)
        airconInfos.add(airconInfo2)
        airconInfos.add(airconInfo3)
        AirconDatabase(this).insertAirconInfos(this, airconInfos)

    }
}