package ds.project.tadaktadakfront.contracts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractDao {

    @Query("SELECT * FROM contract ORDER BY content ASC")
    fun getAlphabetizedWords(): Flow<List<Contract>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(content: Contract)

    @Query("DELETE FROM contract")
    suspend fun deleteAll()
}