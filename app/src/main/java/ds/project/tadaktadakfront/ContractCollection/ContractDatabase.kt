package ds.project.tadaktadakfront.ContractCollection

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contract::class], version = 1)
abstract class ContractDatabase: RoomDatabase() {

    abstract fun ContractDao():ContractDao

    companion object{
        private var INSTANCE: ContractDatabase? = null

        fun getInstance(context: Context): ContractDatabase? {
            if (INSTANCE == null) {
                synchronized(ContractDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ContractDatabase::class.java, "contract")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}


