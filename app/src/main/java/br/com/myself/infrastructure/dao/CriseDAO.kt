package br.com.myself.infrastructure.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.myself.domain.Crise

@Dao
interface CriseDAO {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun persist(crise: Crise): Long
    
    @Delete
    fun delete(crise: Crise)
    
    @Query("SELECT * FROM Crise ORDER BY data DESC")
    fun findAll(): LiveData<List<Crise>>

}
