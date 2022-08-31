package ds.project.tadaktadakfront.ContractCollection

import androidx.lifecycle.LiveData
import androidx.room.*
import org.jetbrains.annotations.Contract

@Dao
interface ContractDao {

    @Query("SELECT * FROM contract ORDER BY name ASC")
    fun getAll(): LiveData<List<ds.project.tadaktadakfront.ContractCollection.Contract>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contract: ds.project.tadaktadakfront.ContractCollection.Contract)

    @Delete
    fun delete(contract: ds.project.tadaktadakfront.ContractCollection.Contract)

}