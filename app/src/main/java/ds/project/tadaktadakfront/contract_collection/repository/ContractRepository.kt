package ds.project.tadaktadakfront.contract_collection.repository

import androidx.annotation.WorkerThread
import ds.project.tadaktadakfront.contract_collection.model.entity.Contract
import ds.project.tadaktadakfront.contract_collection.model.dao.ContractDao
import kotlinx.coroutines.flow.Flow

//DAO에 대한 액세스만 필요하기 때문에 DAO만 프로퍼티로 선언
class ContractRepository(private val contractDao: ContractDao) {

    //Room에서 LiveData목록을 가져와서 초기화
    val allContracts: Flow<List<Contract>> = contractDao.getAll()

    //기본적으로 ROOM은 Main thread에서 suspend 쿼리를 실행함
    //따라서 DB 작업을 main thread에서 오래 실행하지 않도록 하는 다른 구현 필요X

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(contract: Contract) {
        contractDao.insert(contract)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(contract: Contract) {
        contractDao.delete(contract)
    }
}