package br.com.myself.infrastructure.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.myself.domain.Registro

@Dao
interface RegistroDAO {
    
    @Query("SELECT * FROM Registro WHERE data LIKE :mes AND data LIKE :ano ORDER BY data DESC, id DESC")
    fun findAllRegistrosByData(mes: String, ano: String): LiveData<List<Registro>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun persist(registro: Registro)

    @Query("SELECT * FROM Registro WHERE descricao LIKE :buscar")
    fun findAllRegistrosByDescricao(buscar: String): LiveData<List<Registro>>
    
    @Query("SELECT * FROM Registro WHERE id =:registroId")
    fun findById(registroId: Long): LiveData<Registro>

    @Update
    suspend fun persist(registro: Array<Registro>)

    @Delete
    fun delete(registro: Registro)

}
