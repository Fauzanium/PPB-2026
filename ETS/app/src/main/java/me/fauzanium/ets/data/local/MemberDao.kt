package me.fauzanium.ets.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import me.fauzanium.ets.data.model.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY id DESC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: Int): Member?

    @Query("SELECT * FROM members WHERE nomorInduk = :nomorInduk LIMIT 1")
    suspend fun getMemberByNomorInduk(nomorInduk: String): Member?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("DELETE FROM members WHERE id = :id")
    suspend fun deleteMemberById(id: Int)

    @Query("SELECT * FROM members WHERE namaLengkap LIKE '%' || :query || '%' OR nomorInduk LIKE '%' || :query || '%'")
    fun searchMembers(query: String): Flow<List<Member>>
}
