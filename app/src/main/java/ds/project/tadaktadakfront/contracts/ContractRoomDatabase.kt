package ds.project.tadaktadakfront.contracts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Contract::class), version = 1, exportSchema = false)
abstract class ContractRoomDatabase : RoomDatabase() {

    abstract fun contractDao(): ContractDao

    companion object {
        @Volatile
        private var INSTANCE: ContractRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ContractRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContractRoomDatabase::class.java,
                    "contract_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ContractDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class ContractDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.contractDao())
                    }
                }
            }
        }
        suspend fun populateDatabase(contractDao: ContractDao) {
            contractDao.deleteAll()

            var content = Contract("Hello")
            contractDao.insert(content)
            content = Contract("World!")
            contractDao.insert(content)
        }
    }
}