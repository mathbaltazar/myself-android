package br.com.myself.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Entrada(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    @ColumnInfo val descricao: String,
    @ColumnInfo val data: Calendar,
    @ColumnInfo val valor: Double
) : Parcelable
