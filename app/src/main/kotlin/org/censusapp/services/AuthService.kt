import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthService private constructor() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    suspend fun login(username: String, password: String): User? {
        return try {
            val result = auth.signInWithEmailAndPassword(username, password).await()
            val snapshot = database.reference
                .child("users")
                .child(result.user?.uid ?: return null)
                .get()
                .await()
            
            snapshot.getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createUser(user: User): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.username, user.password).await()
            database.reference
                .child("users")
                .child(result.user?.uid ?: return false)
                .setValue(user.copy(id = result.user?.uid ?: ""))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        auth.signOut()
    }

    companion object {
        @Volatile
        private var instance: AuthService? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AuthService().also { instance = it }
        }
    }
}