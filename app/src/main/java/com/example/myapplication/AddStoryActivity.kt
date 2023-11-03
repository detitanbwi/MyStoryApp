package com.example.myapplication

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.ResultState
import com.example.myapplication.databinding.ActivityAddStoryBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class AddStoryActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private var currentImageUri: Uri? = null
    private val MAXIMAL_SIZE = 1000000 //1 MB
    private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var token: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocation: LatLng


    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Posting...")
        progressDialog.setCancelable(false)

        val userPreference = UserPreference(this)
        val userId = userPreference.getUser().userId
        token = userPreference.getUser().token.toString()
        val name = userPreference.getUser().name

        with(binding){
            btnImageGalery.setOnClickListener {
                openImagePicker()
            }
            btnImageKamera.setOnClickListener {
                startCamera()
            }
            buttonAdd.setOnClickListener {
                progressDialog.show()
                if(cbLocation.isChecked){
                    uploadImage(token,edAddDescription.text.toString(),myLocation.latitude,myLocation.longitude)
                }else{
                    uploadImage(token,edAddDescription.text.toString())
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap
            currentImageUri = getImageUri(this)
            binding.ivAddStory .setImageBitmap(imageBitmap)
        }else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val imageUri = data?.data
            currentImageUri = imageUri
            binding.ivAddStory .setImageURI(imageUri)
        }
    }

    private fun uploadImage(token: String, description: String, lat: Double? = null, lng: Double? = null) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            viewModel.uploadImage("Bearer $token" ,imageFile, description, lat, lng).observe(this) { result ->
                progressDialog.dismiss()
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {

                        }

                        is ResultState.Success -> {
                            showToast(result.data.message,true)
                            val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
                            homeViewModel.getStories(token)
                            val resultIntent = Intent()
                            resultIntent.putExtra("isSuccess", true)
                            setResult(RESULT_OK, resultIntent)
                            finish()
                        }

                        is ResultState.Error -> {
                            showToast(result.error,false)
                        }
                    }

                }

            }
        } ?: showToast(getString(R.string.empty_image_warning),false)
    }
    private fun openImagePicker() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
            startActivityForResult(it,PICK_IMAGE_REQUEST)
        }
    }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    myLocation = LatLng(location.latitude,location.longitude)
                    binding.cbLocation.text = "Tell Location (${location.latitude},${location.longitude})"

                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let {
                Log.d("Image URI", "showImage: $it")
                binding.ivAddStory.setImageURI(it)
            }
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    private fun getImageUri(context: Context): Uri {
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
            }
            uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            // content://media/external/images/media/1000000062
            // storage/emulated/0/Pictures/MyCamera/20230825_155303.jpg
        }
        return uri ?: getImageUriForPreQ(context)
    }
    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
        //content://com.dicoding.picodiploma.mycamera.fileprovider/my_images/MyCamera/20230825_133659.jpg
    }
    private fun showToast(message: String,isSuccess: Boolean) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (isSuccess){
            finish()
        }
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}