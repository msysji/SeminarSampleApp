package com.msys.eggplant.demoapp.seminarsampleapp

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var isDriving = false
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 戻るボタンをタップした時などへのキャンセル対応
        setResult(Activity.RESULT_CANCELED)

        val location = intent.getStringExtra("location")
        title = location

        position = intent.getIntExtra("position", 0)
        val status = intent.getIntExtra("status", 0)
        val mode = intent.getStringExtra("mode")
        val temp = intent.getIntExtra("temp", 27)

        // 運転モード
        findViewById<TextView>(R.id.txt_driveMode).text = mode
        // 温度
        findViewById<TextView>(R.id.txt_temperature).text = temp.toString()
        // 運転状態
        if(status == 1) {
            isDriving = true
            findViewById<TextView>(R.id.txt_message).text = "** 運転中 **"
            findViewById<Button>(R.id.btn_submit).text = "停　止"
        }

        // 運転モード選択
        val driveMode = findViewById<LinearLayout>(R.id.driveMode)
        driveMode.setOnClickListener { view ->
            //view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light))
            val dialog = DriveModeFragment()
            dialog.arguments = Bundle().apply {
                putBoolean("isDriving", isDriving)
            }
            val ret = dialog.show(supportFragmentManager, "driveMode")
        }

        // 風量選択
        val airFlow = findViewById<LinearLayout>(R.id.airFlow)
        airFlow.setOnClickListener {
            val dialog = AirFlowFragment()
            dialog.show(supportFragmentManager, "airFlow")
        }

        // 温度UP
        val tempUp = findViewById<Button>(R.id.temperature_up)
        tempUp.setOnClickListener {
            val currentTemp = findViewById<TextView>(R.id.txt_temperature).text.toString().toInt()
            if(currentTemp < 32) {
                findViewById<TextView>(R.id.txt_temperature).text = (currentTemp + 1).toString()
            }
        }
        // 温度DOWN
        val tempDown = findViewById<Button>(R.id.temperature_down)
        tempDown.setOnClickListener {
            val currentTemp = findViewById<TextView>(R.id.txt_temperature).text.toString().toInt()
            if(currentTemp > 18) {
                findViewById<TextView>(R.id.txt_temperature).text = (currentTemp - 1).toString()
            }
        }

        // 電源ボタン
        val submit = findViewById<Button>(R.id.btn_submit)
        submit.setOnClickListener {
            if(!isDriving) {
                findViewById<TextView>(R.id.txt_message).text = "** 運転中 **"
                findViewById<Button>(R.id.btn_submit).text = "停　止"
                isDriving = true
            } else {
                findViewById<TextView>(R.id.txt_message).text = ""
                findViewById<Button>(R.id.btn_submit).text = "運　転"
                isDriving = false
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        // 遷移元の画面に返す結果
//        val result = Intent()
//
        val status = if (isDriving) 1 else 0
        val mode = findViewById<TextView>(R.id.txt_driveMode).text.toString()
        val temp = findViewById<TextView>(R.id.txt_temperature).text.toString().toInt()
//
//        // エアコンの設定情報を返す
//        result.putExtra("position", position)
//        result.putExtra("status", status)
//        result.putExtra("mode", mode)
//        result.putExtra("temp", temp)
//        // 「OK」の結果を返す
//        setResult(Activity.RESULT_OK, result)

        val airconInfo = AirconInfo(position, status, "", mode, temp)
        // DBの更新
        AirconDatabase(this).updateAirconInfo(this,airconInfo)

        setResult(Activity.RESULT_OK)

        // この画面を閉じる
        //finish()
        finishAndRemoveTask()
    }
}

class DriveModeFragment : DialogFragment() {

    private var isDriving = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isDriving = arguments?.getBoolean("isDriving") ?: false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = arrayOf<CharSequence>("自動", "冷房", "除湿", "送風", "暖房")
        val builder = AlertDialog.Builder(activity)
        builder.apply {
            setTitle("運転モード")
            //setView(R.layout.dialog_list)
            setItems(items, { dialog, which ->
                activity?.findViewById<TextView>(R.id.txt_driveMode)?.text = items[which]
                if (isDriving)
                    Toast.makeText(activity, "運転モードを変更しました", Toast.LENGTH_LONG).show()
            })
        }
        return builder.create()
    }
}

class AirFlowFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = arrayOf<CharSequence>("自動", "強", "中", "弱")
        val builder = AlertDialog.Builder(activity)
        builder.apply {
            setTitle("風量")
            //setView(R.layout.dialog_list)
            setItems(items, { dialog, which ->
                activity?.findViewById<TextView>(R.id.txt_airFlow)?.text = items[which]
            })
        }
        return builder.create()
    }
}


