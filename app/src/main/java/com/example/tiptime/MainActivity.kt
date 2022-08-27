

package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Activity yang menampilkan kalkulator tip.
 */
class MainActivity : AppCompatActivity() {

    // Binding objek instance dengan akses ke tampilan di layout activity_main.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengembang file XML tata letak dan mengembalikan instance objek yang mengikat
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Setel tampilan konten Aktivitas menjadi tampilan akar tata letak
        setContentView(binding.root)

        // Kode klik pada tombol Calculate untuk menghitung tip
        binding.calculateButton.setOnClickListener { calculateTip() }

        // Kode untuk membaca hasil input dari nilai yang dimasukan
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }
    }

    /**
     * Menghitung tip berdasarkan input pengguna.
     */
    private fun calculateTip() {
        // Dapatkan nilai desimal yang dimasukan dari EditText
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        // Jika biayanya nol atau 0, maka tampilkan tip 0 dan keluar dari fungsi ini lebih awal.
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        // Dapatkan persentase tip berdasarkan tombol mana yang dipilih
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        // Menghitung tipsnya
        var tip = tipPercentage * cost

        // Jika sakelar untuk pembulatan ke atas diaktifkan (dicentang adalah benar), maka pembulatan ke atas tip.
        //Jika tidak, jangan ubah nilai tip.
        val roundUp = binding.roundUpSwitch.isChecked
        if (roundUp) {
            // Ambil nilai dari tip saat ini, yang membulatkan ke bilangan bulat berikutnya, dan simpan
            // nilai baru dalam variabel tip.
            tip = kotlin.math.ceil(tip)
        }

        // Kode untuk menampilkan berapa tip di aplikasi
        displayTip(tip)
    }

    /**
     * Format jumlah tip sesuai dengan mata uang lokal dan tampilkan di layar.
     * Contoh adalah "Jumlah Tip: Rp10.00".
     */
    private fun displayTip(tip: Double): String {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        return "IDR "+formattedTip.format(tip).replace(",".toRegex(), ".")
    }

    /**
     * Perintah kunci untuk menyembunyikan keyboard saat tombol "Enter" ditekan.
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Sembunyikan keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}