package com.example.galaxystoreadmin.viewmodels

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.galaxystoreadmin.models.Products
import com.example.galaxystoreadmin.models.ProductsOrder
import com.example.galaxystoreadmin.notification.ApiController
import com.example.galaxystoreadmin.notification.Data
import com.example.galaxystoreadmin.notification.Notification
import com.example.galaxystoreadmin.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ProductViewModel : ViewModel() {

    private val _isUploadedImage = MutableStateFlow(false)
    var isUploadedImage : StateFlow<Boolean> = _isUploadedImage

    private val _downloadedImageUrl = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    var downloadedImageUrl : StateFlow<ArrayList<String?>> = _downloadedImageUrl

    private val _isProductSaved = MutableStateFlow(false)
    var isProductSaved : StateFlow<Boolean> = _isProductSaved

    private val _isProductUpdated = MutableStateFlow(false)
    var isProductUpdated : StateFlow<Boolean> = _isProductUpdated


    fun saveImage(imageUri : ArrayList<Uri>){

        val imageUrl = ArrayList<String?>()

        imageUri.forEach{uri->
           val imgRef = FirebaseStorage.getInstance().reference.child(Utils.getCurrentUserId()).child("images").child(UUID.randomUUID().toString())

            imgRef.putFile(uri).continueWithTask {
                imgRef.downloadUrl

            }.addOnCompleteListener{
                val url = it.result
                imageUrl.add(url.toString())

                if (imageUrl.size == imageUri.size)
                { _isUploadedImage.value = true
                    _downloadedImageUrl.value = imageUrl } }
            } }

    //firebaseDatabaseRef.child("products/${products.productId}").updateChildren(record)
    fun saveProduct(products: Products) {
        val firebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("Admins")

        firebaseDatabaseRef.child("products/${products.productId}").setValue(products).addOnSuccessListener {
                        _isProductSaved.value = true }
    }


    fun getAllProduct(name: String?): Flow<List<Products>> = callbackFlow {
        val firebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("Admins").child("products")

        val eventListener = object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = ArrayList<Products>()

                for (item in snapshot.children)
                {
                    val product = item.getValue(Products::class.java)
                    if (name=="All" || product?.productCategory==name) {
                        productList.add(product!!) }
                     }
                trySend(productList)
               }

            override fun onCancelled(error: DatabaseError) { TODO("Not yet implemented") }
        }
        firebaseDatabaseRef.addValueEventListener(eventListener)
        awaitClose { firebaseDatabaseRef.removeEventListener(eventListener) }
    }

    fun updateProduct(products: Products, record: Map<String, Any>) {
        val firebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("Admins")

        firebaseDatabaseRef.child("products/${products.productId}").updateChildren(record).addOnSuccessListener {
            _isProductUpdated.value = true }
    }


    fun getAllOrders() : Flow<List<ProductsOrder>> = callbackFlow{

        val firebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("Admins").child("orders").orderByChild("orderStatus")

        val eventListener = object : ValueEventListener{

            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = ArrayList<ProductsOrder>()

                for (item in snapshot.children)
                {
                    val product = item.getValue(ProductsOrder::class.java)
                        productList.add(product!!)
                }
                trySend(productList)
            }

            override fun onCancelled(error: DatabaseError) { TODO("Not yet implemented") }
        }
        firebaseDatabaseRef.addValueEventListener(eventListener)
        awaitClose { firebaseDatabaseRef.removeEventListener(eventListener) }
    }


    fun updateOrderStatus(orderId:String,status:Int){
        FirebaseDatabase.getInstance().reference.child("Admins").child("orders").child(orderId).child("orderStatus").setValue(status)
    }

    suspend fun sendNotification(orderId : String,title:String,msg:String){
        val getToken =  FirebaseDatabase.getInstance().reference.child("Admins").child("orders").child(orderId).child("orderUserId").get()

        getToken.addOnCompleteListener {
            val userId = it.result.getValue(String::class.java)
            val userToken = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("userToken").get()
            userToken.addOnCompleteListener {
                val notification = Notification(it.result.getValue(String::class.java), Data(title,msg))

                ApiController.notificationApi.sendNotification(notification).enqueue(object :
                    Callback<Notification> {

                    override fun onResponse(call: Call<Notification>, response: Response<Notification>) {

                        if (response.isSuccessful){
                            Log.d("token","Tokent:--"+it.result.getValue(String::class.java).toString())

                        }
                        else{
                            Log.d("takon","Tokent:--"+it.result.getValue(String::class.java).toString())
                        }

                    }

                    override fun onFailure(call: Call<Notification>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }


        }
    }


    fun getAdminAccount(callback : (String?)->Unit){
       val dbRef = FirebaseDatabase.getInstance().reference.child("Admins").child("admin").child(Utils.getCurrentUserId()).child("phoneNumber")
       // val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(Utils.getCurrentUserId()).child("address")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val address = snapshot.getValue(String::class.java)
                    callback(address)
                }
                else{   callback(null) }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null) }
        })
    }



}