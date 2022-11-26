package ds.project.tadaktadakfront.contract.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ds.project.tadaktadakfront.contract.model.entity.Contract
import ds.project.tadaktadakfront.contract.model.dao.ContractDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Contract::class], version = 1)
abstract class ContractDatabase: RoomDatabase() {
    abstract fun contractDao(): ContractDAO

    private class ContractDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                }
            }
        }
    }

    companion object {
        private var INSTANCE: ContractDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): ContractDatabase? {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContractDatabase::class.java,
                    "contract"
                ).allowMainThreadQueries()
                    .addCallback(ContractDatabaseCallback(scope)) // build 전 콜백 추가
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}