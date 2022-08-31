package ds.project.tadaktadakfront.ContractCollection

import android.app.Application
import androidx.lifecycle.LiveData

class ContractRepository(application: Application) {
    private val contractDatabase = ContractDatabase.getInstance(application)!!
    private val contractDao: ContractDao = contractDatabase.ContractDao()
    private val contracts: LiveData<List<Contract>> = contractDao.getAll()

    fun getAll(): LiveData<List<Contract>> {
        return contracts
    }

    fun insert(contract: Contract) {
        try {
            val thread = Thread(Runnable {
                contractDao.insert(contract) })
            thread.start()
        } catch (e: Exception) { }
    }

    fun delete(contract: Contract) {
        try {
            val thread = Thread(Runnable {
                contractDao.delete(contract)
            })
            thread.start()
        } catch (e: Exception) { }
    }
}