package ds.project.tadaktadakfront

import android.app.Application
import ds.project.tadaktadakfront.contract_collection.model.ContractDatabase
import ds.project.tadaktadakfront.contract_collection.repository.ContractRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ContractApplication: Application() {
    val applicationScope= CoroutineScope(SupervisorJob())

    val database by lazy { ContractDatabase.getInstance(this, applicationScope) }
    val repository by lazy {ContractRepository(database!!.contractDao())}
}