package com.example.sesisilogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        // akun demo: periksa di langkah 11 (tabel uji coba)
        private const val EMAIL_DEMO = "rani@example.com"
        private const val SANDI_DEMO = "123456"
    }

    private lateinit var session: SessionManager
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbIngatSaya: CheckBox
    private lateinit var tvPesanMasuk: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1) siapkan pengelola sesi
        session = SessionManager(this)

        // 2) ambil semua view
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        cbIngatSaya = findViewById(R.id.cbIngatSaya)
        tvPesanMasuk = findViewById(R.id.tvPesanMasuk)

        // ===== CEK SESI =====
        if (session.sudahLogin()) {
            bukaDashboard()
            return
        }

        // tidak ada sesi -> tampilkan form login
        etEmail.setText(session.ambilEmail())

        // 3) pasang tombol masuk
        findViewById<Button>(R.id.btnMasuk).setOnClickListener { prosesLogin() }
    }

    private fun prosesLogin() {
        val email = etEmail.text.toString().trim()
        val sandi = etPassword.text.toString()

        // 1) bersihkan pesan kesalahan sebelumnya
        tvPesanMasuk.text = ""

        // 2) validasi isian
        if (email.isEmpty()) {
            etEmail.error = getString(R.string.pesan_email_kosong)
            return
        }
        if (sandi.length < 6) {
            etPassword.error = getString(R.string.pesan_sandi_pendek)
            return
        }

        // 3) cocokkan dengan akun demo
        if (email != EMAIL_DEMO || sandi != SANDI_DEMO) {
            tvPesanMasuk.text = getString(R.string.pesan_login_gagal)
            return
        }

        // 4) susun nama tampilan dari bagian depan email
        val nama = email.substringBefore("@").replaceFirstChar { it.uppercase() }

        // 5) simpan sesi
        session.simpanSesi(nama, email, cbIngatSaya.isChecked)

        // 6) masuk ke dashboard
        bukaDashboard()
    }

    private fun bukaDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}