package ds.project.tadaktadakfront.contract

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contract")
data class Contract (
    @PrimaryKey @ColumnInfo(name="name") var name: String,
    @ColumnInfo(name = "number") var number: String,
    @ColumnInfo(name = "address") var address: String ) {
    constructor(): this("", "", "")
}
