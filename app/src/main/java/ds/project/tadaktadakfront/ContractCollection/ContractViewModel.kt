package ds.project.tadaktadakfront.ContractCollection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ContractViewModel(application: Application) : AndroidViewModel(application)  {
    private val repository = ContractRepository(application)
    private val contracts = repository.getAll()

    fun getAll(): LiveData<List<Contract>> {
        return this.contracts
    }
    fun insert(contract: Contract) {
        repository.insert(contract)
    }

    fun delete(contract: Contract) {
        repository.delete(contract)
    }

}