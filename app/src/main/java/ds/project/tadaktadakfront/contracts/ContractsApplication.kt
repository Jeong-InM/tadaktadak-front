package ds.project.tadaktadakfront.contracts

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ContractsApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ContractRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ContractRepository(database.contractDao()) }
}
