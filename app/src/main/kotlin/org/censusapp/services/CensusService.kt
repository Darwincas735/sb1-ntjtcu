import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class CensusService private constructor() {
    private val database = FirebaseDatabase.getInstance()

    suspend fun addEntry(entry: CensusEntry): String {
        val ref = database.reference.child("census").push()
        val id = ref.key ?: throw IllegalStateException("Failed to generate key")
        
        ref.setValue(entry.copy(id = id)).await()
        return id
    }

    suspend fun getEntriesByVotingCenter(votingCenter: String): List<CensusEntry> {
        val snapshot = database.reference
            .child("census")
            .orderByChild("votingCenter")
            .equalTo(votingCenter)
            .get()
            .await()

        return snapshot.children.mapNotNull { it.getValue(CensusEntry::class.java) }
    }

    companion object {
        @Volatile
        private var instance: CensusService? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: CensusService().also { instance = it }
        }
    }
}