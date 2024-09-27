package com.example.galaxystore.viewmodels
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.galaxystore.cart_room_db.ProductDb
import com.example.galaxystore.cart_room_db.ProductEntity
import com.example.galaxystore.models.Products
import com.example.galaxystore.models.ProductsOrder
import com.example.galaxystore.models.Users
import com.example.galaxystore.notifications.Data
import com.example.galaxystore.notifications.Notification
import com.example.galaxystore.phone_pay_payment.ApiController
import com.example.galaxystore.utils.Constants
import com.example.galaxystore.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(application: Application) : AndroidViewModel(application) {


    private val sharedPreferences = application.getSharedPreferences("galaxy",Context.MODE_PRIVATE)
    private val productCartDao = ProductDb.getDatabase(application).productDao()

    private val _paymentStatus = MutableStateFlow(false)
    val paymentStatus = _paymentStatus

    //Get All Products From Firebase
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

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented") }
        }
        firebaseDatabaseRef.addValueEventListener(eventListener)
        awaitClose {
            firebaseDatabaseRef.removeEventListener(eventListener)
        }
    }

   //Room Database
    suspend fun insertCartData(productEntity: ProductEntity) {
        productCartDao.addToCart(productEntity)
    }

    fun getProductsFromRoom() : LiveData<List<ProductEntity>> {
        return productCartDao.getProduct()
    }

    suspend fun removeCartProduct(productId:String) {
        productCartDao.removeCartProduct(productId)
    }

    suspend fun deleteAllCartProduct(){ productCartDao.deleteAllCartProduct()}

    //SharedPreferences
    fun saveUserAddress(){
        sharedPreferences.edit().putBoolean("address",true).apply() }

    fun getAddress() : MutableLiveData<Boolean>{
        val status = MutableLiveData<Boolean>()
        status.value = sharedPreferences.getBoolean("address",false)
        return status }

    //Firebase:--AddItem Count
    fun addItemCount(products: Products,itemCount:Int) {
        val firebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("Admins")
        firebaseDatabaseRef.child("products/${products.productId}").child("itemCount").setValue(itemCount)

    }

    //Firebase:--Add User Address
    fun addUserAddress(address:String,name:String){
       val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(Utils.getCurrentUserId())
        dbRef.child("address").setValue(address)
        dbRef.child("name").setValue(name)
    }

    //Retrofit
    suspend fun checkPayment(headers:Map<String,String>){
        val response = ApiController.retrofit.checkStatus(headers,Constants.MERCHANT_ID,Constants.transactionId)
        _paymentStatus.value = response.body()!=null && response.body()!!.success
    }

    //Firebase:--Get User Address
    fun getUserAddress(callback : (String?)->Unit){
        val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(Utils.getCurrentUserId()).child("address")
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

    //Firebase:--Save Ordered Products
    fun saveOrderedProduct(productsOrder: ProductsOrder){
        FirebaseDatabase.getInstance().reference.child("Admins").child("orders").child(productsOrder.orderId!!).setValue(productsOrder)
    }


    //Firebase:--Update Product Stock
    fun updateProductStock(stock:Int,products: ProductEntity){
        val firebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("Admins")

        firebaseDatabaseRef.child("products/${products.productId}").child("itemCount").setValue(0)

        firebaseDatabaseRef.child("products/${products.productId}").child("productStock").setValue(stock)

    }

    //Firebase:--GetOrdered Products
    fun getAllOrders() : Flow<List<ProductsOrder>> = callbackFlow{

        val firebaseDatabaseRef = FirebaseDatabase.getInstance().reference.child("Admins").child("orders").orderByChild("orderStatus")

        val eventListener = object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = ArrayList<ProductsOrder>()
                for (item in snapshot.children)
                {
                    val product = item.getValue(ProductsOrder::class.java)
                    if (product?.orderUserId == Utils.getCurrentUserId()) {
                        productList.add(product) }
                }
                trySend(productList)
            }

            override fun onCancelled(error: DatabaseError) { TODO("Not yet implemented") }
        }
        firebaseDatabaseRef.addValueEventListener(eventListener)
        awaitClose { firebaseDatabaseRef.removeEventListener(eventListener) }
    }

    //Firebase:--Send Notification
    fun sendNotification(adminUid : String,title:String,msg:String){
       val getToken = FirebaseDatabase.getInstance().reference.child("Admins").child("admin").child(adminUid).child("adminToken").get()

        getToken.addOnCompleteListener {
            val token = it.result.getValue(String::class.java)
            val notification = Notification(token, Data(title,msg))

            ApiController.notificationApi.sendNotification(notification).enqueue(object : Callback<Notification> {

                override fun onResponse(call: Call<Notification>, response: Response<Notification>) {

                    if (response.isSuccessful){
                        Log.d("tocken","Tokent:--"+token.toString()) }
                }

                override fun onFailure(call: Call<Notification>, t: Throwable) {
                    TODO("Not yet implemented") }
            })
        }
    }

    //Firebase:--User Logout
    fun logout()  {  FirebaseAuth.getInstance().signOut()}

    fun getUserProfile(callback : (Users?)->Unit){
        val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(Utils.getCurrentUserId())
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val address = snapshot.getValue(Users::class.java)
                    callback(address)
                }
                else{   callback(null) }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null) }
        })
    }
}