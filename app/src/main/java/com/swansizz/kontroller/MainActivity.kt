import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnShutdown = findViewById<Button>(R.id.btnShutdown)
        val btnRestart = findViewById<Button>(R.id.btnRestart)
        val volumeControl = findViewById<SeekBar>(R.id.volumeControl)
        btnShutdown.setOnClickListener {
            sendRequest("shutdown")
        }
        btnRestart.setOnClickListener {
            sendRequest("restart")
        }
        volumeControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sendRequest("volume/$progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun sendRequest(command: String) {
        val url = "http://88.230.255.95:5000/$command"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    println(response.body?.string())
                }
            }
        })
    }
}
