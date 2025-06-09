package vcmsa.projects.sindiswasinazo.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.sindiswasinazo.databinding.ActivityRegisterBinding
import vcmsa.projects.sindiswasinazo.data.FirebaseRepository
import vcmsa.projects.sindiswasinazo.ui.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val repository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            val username = binding.usernameEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Snackbar.make(binding.root, "Please fill all fields", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Snackbar.make(binding.root, "Passwords don't match", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.Main).launch {
                if (repository.register(email, password, username)) {
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(binding.root, "Registration failed", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginTextView.setOnClickListener {
            finish()
        }
    }
}