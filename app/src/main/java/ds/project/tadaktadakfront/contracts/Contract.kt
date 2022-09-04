package ds.project.tadaktadakfront.contracts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contract")
data class Contract(
    @PrimaryKey @ColumnInfo(name="content") val content:String)
