import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.censusapp.databinding.ActivityCensusBinding
import org.censusapp.models.CensusEntry
import org.censusapp.models.User
import org.censusapp.services.CensusService
import org.censusapp.ui.login.LoginActivity

class CensusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCensusBinding
    private lateinit var currentUser: User
    private val censusService = CensusService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCensusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getParcelableExtra(EXTRA_USER) ?: run {
            finish()
            return
        }

        setupViews()
        loadEntries()
    }

    private fun setupViews() {
        binding.etVotingCenter.setText(currentUser.votingCenter)
        binding.etVotingCenter.isEnabled = false

        binding.btnSave.setOnClickListener {
            saveEntry()
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun saveEntry() {
        val fullName = binding.etFullName.text.toString()
        val dni = binding.etDni.text.toString()
        val phone = binding.etPhone.text.toString()

        if (fullName.isBlank() || dni.isBlank() || phone.isBlank()) {
            showError("Please fill all fields")
            return
        }

        lifecycleScope.launch {
            val entry = CensusEntry(
                fullName = fullName,
                dni = dni,
                phoneNumbers = listOf(phone),
                votingCenter = currentUser.votingCenter,
                createdBy = currentUser.id
            )

            try {
                censusService.addEntry(entry)
                clearForm()
                loadEntries()
            } catch (e: Exception) {
                showError("Failed to save entry")
            }
        }
    }

    private fun loadEntries() {
        lifecycleScope.launch {
            try {
                val entries = censusService.getEntriesByVotingCenter(currentUser.votingCenter)
                // Update RecyclerView with entries
            } catch (e: Exception) {
                showError("Failed to load entries")
            }
        }
    }

    private fun clearForm() {
        binding.etFullName.text?.clear()
        binding.etDni.text?.clear()
        binding.etPhone.text?.clear()
    }

    private fun showError(message: String) {
        // Show error message using your preferred method (Toast, Snackbar, etc.)
    }

    companion object {
        private const val EXTRA_USER = "extra_user"

        fun createIntent(context: Context, user: User) =
            Intent(context, CensusActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
    }
}