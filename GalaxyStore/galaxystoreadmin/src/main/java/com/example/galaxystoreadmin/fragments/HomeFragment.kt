package com.example.galaxystoreadmin.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.adapters.CategoryAdapter
import com.example.galaxystoreadmin.adapters.ProductAdapter
import com.example.galaxystoreadmin.databinding.EditProductDialogBinding
import com.example.galaxystoreadmin.databinding.FragmentHomeBinding
import com.example.galaxystoreadmin.models.Category
import com.example.galaxystoreadmin.models.Products
import com.example.galaxystoreadmin.utils.Constants
import com.example.galaxystoreadmin.utils.Utils
import com.example.galaxystoreadmin.viewmodels.ProductViewModel
import kotlinx.coroutines.launch



class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var categoryList : ArrayList<Category>
    private lateinit var adapter: ProductAdapter


    @Deprecated("Deprecated in Java")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.addFloatingBtn.setOnClickListener { findNavController().navigate(R.id.action_home_to_addProduct) }
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         getCategoryList()
         getProductList("All")
         searchProducts()


    }




    private fun getCategoryList() {
        categoryList = ArrayList()
        for (i in 0 until Constants.categoryIcon.size)
        {
           categoryList.add(Category(Constants.categoryName[i],Constants.categoryIcon[i])) }

        binding.categoriesList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.categoriesList.adapter =  CategoryAdapter(categoryList,::onClickedItem)
    }

    private fun getProductList(name: String?) {

        binding.simmerEffect.visibility = View.VISIBLE
        binding.productList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = ProductAdapter(::onEditBtnClicked)
        binding.productList.adapter = adapter

        lifecycleScope.launch {
          productViewModel.getAllProduct(name).collect{

             if (it.isEmpty()) {
                 binding.productList.visibility=View.GONE
                 binding.noProducts.visibility = View.VISIBLE }

              else{
                 binding.productList.visibility=View.VISIBLE
                 binding.noProducts.visibility = View.GONE }

                 adapter.differ.submitList(it)
                 adapter.productList = it as ArrayList<Products>
                 binding.simmerEffect.visibility = View.GONE
          } } }


    private fun searchProducts() {
        binding.searchBar.addTextChangeListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(search: CharSequence?, p1: Int, p2: Int, p3: Int) {
               val query = search.toString().trim()
                adapter.filter.filter(query) }

            override fun afterTextChanged(p0: Editable?) { }

        })

    }

    private fun onClickedItem(category: Category){    getProductList(category.name)   }

    private fun onEditBtnClicked(products: Products){
        val editProduct = EditProductDialogBinding.inflate(LayoutInflater.from(requireContext()))

        editProduct.apply {
            editName.setText(products.productName)
            editPrice.setText(products.productPrice.toString())
            editStock.setText(products.productStock.toString())
            editDiscount.setText(products.productDiscount.toString())
            editCategory.setText(products.productCategory)
            editDesc.setText(products.productDesc) }

        val alertDialog = AlertDialog.Builder(requireContext()).setView(editProduct.root).create()
        alertDialog.show()

        editProduct.editBtn.setOnClickListener {
            editProduct.apply {
                editName.isEnabled     = true
                editPrice.isEnabled    = true
                editStock.isEnabled    = true
                editDiscount.isEnabled = true
                editCategory.isEnabled = true
                editDesc.isEnabled     = true
                saveBtn.isEnabled      = true } }

        //Set Category Items
       val setCategory = resources.getStringArray(R.array.category)
        editProduct.editCategory.setAdapter(ArrayAdapter(requireContext(),R.layout.category_list,setCategory))

        editProduct.saveBtn.setOnClickListener {
            val name= editProduct.editName.text.toString().trim()
            val price= editProduct.editPrice.text.toString().trim()
            val stock= editProduct.editStock.text.toString().trim()
            val discount= editProduct.editDiscount.text.toString().trim()
            val category= editProduct.editCategory.text.toString().trim()
            val desc= editProduct.editDesc.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || stock.isEmpty() || discount.isEmpty() || category.isEmpty() || desc.isEmpty())
               {  Utils.showToast(requireContext(),"Please fill all fields")  }

            else {
                val productPrice:Double = price.toDouble()
                Utils.showDialog(requireContext(),"Updating Products...")
                val record = mapOf("productName" to name,"productPrice" to productPrice,"productStock" to stock.toInt(),"productDiscount" to discount.toInt(),"productCategory" to category,"productDesc" to desc)
                productViewModel.updateProduct(products,record)

                lifecycleScope.launch {
                    productViewModel.isProductUpdated.collect{
                        if (it){ Utils.apply {
                                hideDialog()
                                showToast(requireContext(),"Updated successful...") } } } }

                alertDialog.dismiss() }
        } }




}