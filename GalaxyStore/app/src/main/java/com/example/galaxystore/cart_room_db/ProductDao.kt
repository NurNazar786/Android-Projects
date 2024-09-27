package com.example.galaxystore.cart_room_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addToCart(productEntity: ProductEntity)

    @Query("SELECT EXISTS(SELECT * FROM product WHERE productId=:pId)")
    fun isExist(pId:Int): LiveData<Boolean>

    @Query("SELECT * FROM product")
    fun getProduct(): LiveData<List<ProductEntity>>

    @Query("DELETE FROM product WHERE productId = :pId")
    suspend fun removeCartProduct(pId:String)

//    @Query("UPDATE product SET productCount=:count,productPrice=:price WHERE productId=:id")
//    suspend fun updateUser(id:Int,count:Int,price:String)

    @Query("DELETE FROM product")
    suspend fun deleteAllCartProduct()

}