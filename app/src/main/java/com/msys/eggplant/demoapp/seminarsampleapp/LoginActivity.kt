package com.msys.eggplant.demoapp.seminarsampleapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val button = findViewById<Button>(R.id.btn_login)
        val passcdEditText = findViewById<TextView>(R.id.input_passcd)
        passcdEditText.text = ""

        // ログインボタンクリック時
        button.setOnClickListener {
            var isValid = true

            val passcode = passcdEditText.text.toString()

            // パスコードが未入力の場合、エラーメッセージを表示
            if (passcode.isEmpty()) {
                passcdEditText.error = "パスコードを入力してください"
                isValid = false
            }

            // パスコードが合致していない場合、エラーメッセージを表示
            else if (passcode != "1234") {
                passcdEditText.error = "パスコードが違います"
                passcdEditText.text = ""
                isValid = false
            }

            // チェックに通ったら、日付選択画面に遷移する
            if (isValid) {
                val intent = Intent(this, AirconListActivity::class.java)
                //passcdEditText.text = ""
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val passcdEditText = findViewById<TextView>(R.id.input_passcd)
        passcdEditText.text = ""
    }
}