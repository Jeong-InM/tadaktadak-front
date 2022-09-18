package ds.project.tadaktadakfront.contracts

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ContractRepository(private val contractDao: ContractDao) {

    val allContents: Flow<List<Contract>> = contractDao.getAlphabetizedWords()

    @Suppress
    @WorkerThread
    suspend fun insert(contract:Contract){
        contractDao.insert(contract)
    }

}