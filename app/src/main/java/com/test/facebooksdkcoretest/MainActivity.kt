package com.test.facebooksdkcoretest

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        UnableToStartAsyncTask(this).execute()
    }

    override fun onResume() {
        super.onResume()
        /*
         Uncomment this line to force ANR and
         pull collected data by `adb pull /data/anr/traces.txt`
         */
        //Thread.sleep(60000)
    }
}

@SuppressLint("StaticFieldLeak")
class UnableToStartAsyncTask(
    private val context: Context
) : AsyncTask<Void, Void, Unit>() {

    override fun doInBackground(vararg p0: Void?) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "You're breathtaking!", Toast.LENGTH_LONG).show()
        }
    }

}
