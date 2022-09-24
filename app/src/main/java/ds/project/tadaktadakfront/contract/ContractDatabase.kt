package ds.project.tadaktadakfront.contract

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.chrono.HijrahChronology.INSTANCE

@Database(entities = arrayOf(Contract::class), version = 1, exportSchema = false)
abstract class ContractDatabase: RoomDatabase() {
    abstract fun contractDao(): ContractDAO


    // 데이터베이스 인스턴스를 싱글톤으로 사용하기 위해 companion object
    companion object {
        private var INSTANCE: ContractDatabase? = null

        // 여러 스레드가 접근하지 못하도록 synchronized로 설정
        fun getDatabase(context: Context,
                        scope: CoroutineScope)
        :ContractDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContractDatabase::class.java,
                    "contract"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ContractDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class ContractDatabaseCallback(
            private val scope: CoroutineScope
            ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch (Dispatchers.IO) {
                        populateDatabase(database.contractDao())
                    }
                }
            }
        }
        suspend fun populateDatabase(contractDao: ContractDAO) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            contractDao.deleteAll()
            var word = Contract("덕성CS", "02-901-8000", "서울 도봉구 삼양로 144길 33")
            contractDao.insert(word)
        }
    }
}