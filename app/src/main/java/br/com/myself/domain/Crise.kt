package br.com.myself.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Crise(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),

    @ColumnInfo var data: Calendar,
    @ColumnInfo var observacoes: String,
    @ColumnInfo var horario1: String,
    @ColumnInfo var horario2: String,
    //var arquivos ???
) : Parcelable