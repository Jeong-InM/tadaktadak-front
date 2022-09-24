package ds.project.tadaktadakfront.contract

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ContractRepository(private val contractDao: ContractDAO) {

    val allContracts: Flow<List<Contract>> = contractDao.getAll()
    // 기본적으로 Room은 main thread에서 suspend 쿼리를 실행함
    // 따라서 DB 작업을 main thread에서 오래 실행하지 않도록 하는 다른 구현 필요 없음
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