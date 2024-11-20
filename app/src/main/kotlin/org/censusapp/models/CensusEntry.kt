data class CensusEntry(
    val id: String = "",
    val fullName: String = "",
    val dni: String = "",
    val phoneNumbers: List<String> = listOf(),
    val votingCenter: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)