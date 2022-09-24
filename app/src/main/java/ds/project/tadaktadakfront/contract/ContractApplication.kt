package ds.project.tadaktadakfront.contract

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ContractApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ContractDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ContractRepository(database.contractDao()) }
}