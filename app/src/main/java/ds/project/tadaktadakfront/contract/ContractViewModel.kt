package ds.project.tadaktadakfront.contract

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ContractViewModel(private val repository: ContractRepository): ViewModel() {

    internal val contracts: LiveData<List<Contract>> = repository.allContracts.asLiveData()

    fun insert(contract: Contract) = viewModelScope.launch {
        repository.insert(contract)
    }
    fun delete(contract: Contract) = viewModelScope.launch {
        repository.delete(contract)
    }
}

class ContractViewModelFactory(private val repository: ContractRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContractViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}