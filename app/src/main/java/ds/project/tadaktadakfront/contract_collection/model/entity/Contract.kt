package ds.project.tadaktadakfront.contract_collection.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contract")
data class Contract(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "number")
    var number: String,

    @ColumnInfo(name = "initial")
    var initial: Char
){

    constructor(): this(null, "", "", '\u0000')
}


