package ds.project.tadaktadakfront.contract.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import ds.project.tadaktadakfront.contract.model.ContractDatabase
import ds.project.tadaktadakfront.contract.model.dao.ContractDAO
import ds.project.tadaktadakfront.contract.model.entity.Contract
import kotlinx.coroutines.flow.Flow

class ContractRepository(private var contractDao: ContractDAO) {

    val allContracts: Flow<List<Contract>> = contractDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(contract: Contract){
        contractDao.insert(contract)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(contract: Contract){
        contractDao.delete(contract)
    }

}