import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.censusapp.databinding.ActivityLoginBinding
import org.censusapp.services.AuthService
import org.censusapp.ui.admin.AdminActivity
import org.censusapp.ui.census.CensusActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authService = AuthService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                showError("Please enter username and password")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = authService.login(username, password)
                if (user != null) {
                    startActivity(
                        when (user.role) {
                            "admin" -> AdminActivity.createIntent(this@LoginActivity, user)
                            else -> CensusActivity.createIntent(this@LoginActivity, user)
                        }
                    )
                    finish()
                } else {
                    showError("Invalid credentials")
                }
            }
        }
    }

    private fun showError(message: String) {
        // Show error message using your preferred method (Toast, Snackbar, etc.)
    }
}