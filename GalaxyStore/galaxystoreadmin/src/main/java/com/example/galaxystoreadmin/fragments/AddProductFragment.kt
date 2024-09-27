package com.example.galaxystoreadmin.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.adapters.ProductImagesAdapter
import com.example.galaxystoreadmin.databinding.FragmentAddProductBinding
import com.example.galaxystoreadmin.models.Products
import com.example.galaxystoreadmin.utils.Utils
import com.example.galaxystoreadmin.viewmodels.ProductViewModel
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {

    private var _binding:FragmentAddProductBinding?=null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()

    private val imageUri : ArrayList<Uri> = arrayListOf()

    private val selectedImage =  registerForActivityResult(ActivityResultContracts.GetMultipleContents()){uriList->
        val fiveImages = uriList.take(5)
        imageUri.clear()
        imageUri.addAll(fiveImages)
        binding.rvProductImages.adapter = ProductImagesAdapter(imageUri) }


    private lateinit var setCategory:Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View
    {
        _binding = FragmentAddProductBinding.inflate(inflater,container,false)

        //Set Category Items
        setCategory = resources.getStringArray(R.array.category)
        binding.edCategory.setAdapter(ArrayAdapter(requireContext(),R.layout.category_list,setCategory))

        binding.selectImages.setOnClickListener {
            binding.rvProductImages.visibility = View.VISIBLE
            selectedImage.launch("image/*")  }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserInput()

    }

    private fun getUserInput() {

        binding.addProductBtn.setOnClickListener {
            val productName = binding.edName.text.toString().trim()
            val productPrice = binding.edPrice.text.toString().trim()
            val productStock = binding.edStock.text.toString().trim()
            val productDiscount = binding.edDiscount.text.toString().trim()
            val productCategory = binding.edCategory.text.toString().trim()
            val productDesc = binding.edDesc.text.toString().trim()



            if (productName.isEmpty() || productPrice.isEmpty() || productStock.isEmpty() || productDiscount.isEmpty() || productCategory.isEmpty() || productDesc.isEmpty())
            { Utils.showToast(requireContext(),"Please fill all the fields") }

            else if (imageUri.isEmpty()){
                    Utils.showToast(requireContext(),"Please select some images") }
            else{
                val price:Double = productPrice.toDouble()
                val products = Products(
                               productId = Utils.getRandomId(),
                               productName = productName,
                               productPrice = price,
                               productStock = productStock.toInt(),
                               productDiscount =  productDiscount.toInt(),
                               productCategory = productCategory,
                               productDesc = productDesc,
                               itemCount=0 ,
                               adminUid = Utils.getCurrentUserId())

                saveImages(products)
            }
        } }

    private fun saveImages(products: Products) {
        Utils.showDialog(requireContext(),"Uploading product images...")
        productViewModel.saveImage(imageUri)

        lifecycleScope.launch {
            productViewModel.isUploadedImage.collect{
                if (it){
                    Utils.apply {
                        hideDialog()
                        showToast(requireContext(),"Images uploaded successful...") }

                         productSaveProcess(products)
                }
            }
    }
    }

    private fun productSaveProcess(products: Products) {
        Utils.showDialog(requireContext(),"Products Publishing...")

        lifecycleScope.launch {
            productViewModel.downloadedImageUrl.collect{
                val imageUrls = it
                products.productImages = imageUrls
                saveProducts(products)
            }
        }
}

    private fun saveProducts(products: Products) {
        productViewModel.saveProduct(products)

        lifecycleScope.launch {
            productViewModel.isProductSaved.collect{
              if (it){
                  Utils.hideDialog()
                  Utils.showToast(requireContext(),"Product publishing success...")
                  emptyAllFields()
              } } }
    }


    private fun emptyAllFields(){
        binding.edName.text!!.clear()
        binding.edDesc.text!!.clear()
        binding.edPrice.text!!.clear()
        binding.edStock.text!!.clear()
        binding.edDiscount.text!!.clear()
        binding.rvProductImages.visibility = View.GONE
    }

}