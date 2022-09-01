package ds.project.tadaktadakfront.contract_collection.model.dao

import androidx.room.*
import ds.project.tadaktadakfront.contract_collection.model.entity.Contract
import kotlinx.coroutines.flow.Flow


@Dao
interface ContractDao {

    @Query("SELECT * FROM contract ORDER BY name ASC")
    fun getAll(): Flow<List<Contract>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contract: Contract)

    @Delete
    fun delete(contract: Contract)

    @Query("DELETE FROM contract")
    fun deleteAll()


}