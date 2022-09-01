package ds.project.tadaktadakfront.contract_collection.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ds.project.tadaktadakfront.contract_collection.model.entity.Contract
import ds.project.tadaktadakfront.contract_collection.model.dao.ContractDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Contract::class], version = 1, exportSchema = false)
abstract class ContractDatabase: RoomDatabase() {
    abstract fun contractDao(): ContractDao

    private class ContractDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var contractDao = database.contractDao()
                    contractDao.deleteAll()
                }
            }
        }
    }

    //데이터베이스 인스턴스를 싱글톤으로 사용
    companion object{
        private var INSTANCE: ContractDatabase? = null

        //여러 스레드가 접근하지 못하도록 synchronized설정
        fun getInstance(context: Context, scope: CoroutineScope): ContractDatabase? {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContractDatabase::class.java,
                    "contract"
                ).build()
                INSTANCE = instance
                instance
            }

        }

    }

}


