package br.com.myself.infrastructure.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import br.com.myself.domain.Entrada

@Dao
interface EntradaDAO {
    
    @Query("SELECT * FROM Entrada WHERE data LIKE :yearLike ORDER BY data DESC, id DESC")
    fun findAllByYear(yearLike: String): PagingSource<Int, Entrada>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persist(entrada: Entrada): Long
    
    @Delete
    suspend fun delete (entrada: Entrada)
    
    @Query("SELECT COUNT(*) FROM Entrada WHERE data LIKE :yearLike")
    fun countByYear(yearLike: String): LiveData<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entradas: List<Entrada>)
    
    @Query("DELETE FROM Entrada")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM Entrada WHERE id=:itemId")
    fun findById(itemId: Long): LiveData<Entrada>

}