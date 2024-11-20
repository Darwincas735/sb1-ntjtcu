import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.censusapp.databinding.ActivityAdminBinding
import org.censusapp.models.User
import org.censusapp.services.AuthService
import org.censusapp.ui.login.LoginActivity

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val authService = AuthService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.btnCreateUser.setOnClickListener {
            createUser()
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun createUser() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val votingCenter = binding.etVotingCenter.text.toString()
        val isAdmin = binding.switchAdmin.isChecked

        if (username.isBlank() || password.isBlank() || votingCenter.isBlank()) {
            showError("Please fill all fields")
            return
        }

        lifecycleScope.launch {
            val user = User(
                username = username,
                password = password,
                votingCenter = votingCenter,
                role = if (isAdmin) "admin" else "user"
            )

            if (authService.createUser(user)) {
                showSuccess("User created successfully")
                clearForm()
            } else {
                showError("Failed to create user")
            }
        }
    }

    private fun clearForm() {
        binding.etUsername.text?.clear()
        binding.etPassword.text?.clear()
        binding.etVotingCenter.text?.clear()
        binding.switchAdmin.isChecked = false
    }

    private fun showError(message: String) {
        // Show error message using your preferred method (Toast, Snackbar, etc.)
    }

    private fun showSuccess(message: String) {
        // Show success message using your preferred method (Toast, Snackbar, etc.)
    }

    companion object {
        fun createIntent(context: Context, user: User) =
            Intent(context, AdminActivity::class.java).apply {
                putExtra("user", user)
            }
    }
}