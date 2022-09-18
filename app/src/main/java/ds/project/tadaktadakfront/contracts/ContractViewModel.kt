@file:Suppress("unused")

package ds.project.tadaktadakfront.contracts

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContractViewModel(private  val repository: ContractRepository) : ViewModel(){

    val allContracts: LiveData<List<Contract>> =repository.allContents.asLiveData()

    fun insert(contract: Contract) = viewModelScope.launch {
        repository.insert(contract)
    }

}

class ContractViewModelFactory(private val repository: ContractRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContractViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
