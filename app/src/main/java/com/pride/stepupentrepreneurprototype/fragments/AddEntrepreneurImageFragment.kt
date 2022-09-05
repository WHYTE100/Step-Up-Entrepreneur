package com.pride.stepupentrepreneurprototype.fragments

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pride.stepupentrepreneurprototype.R
import com.pride.stepupentrepreneurprototype.databinding.FragmentAddEntrepreneurImageBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddEntrepreneurImageFragment : Fragment() {

    private lateinit var binding: FragmentAddEntrepreneurImageBinding

  
    private val storageRef = Firebase.storage.reference
    private lateinit var imageUri: Uri

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        Picasso.get().load(uri).fit().centerCrop().into( binding.profileImageView)
        if (uri != null) {
            imageUri = uri
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddEntrepreneurImageBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        binding.button.setOnClickListener {

            Dexter.withContext(requireContext())
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                getContent.launch("image/*")
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // Remember to invoke this method when the custom rationale is closed
                        // or just by default if you don't want to use any custom rationale.
                        token?.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    Toast.makeText(requireContext(), "Error $it", Toast.LENGTH_SHORT).show()
                }
                .check()

        }

        binding.continueButton.setOnClickListener {
            saveProfile()
        }

        return binding.root
    }

    private fun saveProfile() {
        val userId = auth.currentUser?.uid
        val imagesRef: StorageReference = storageRef.child("profile_images/$userId.jpg")
        imagesRef.putFile(imageUri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val userDetails = hashMapOf(
                    "profile_url" to downloadUri.toString(),
                    //"uid" to userId
                )
                db.collection("users").document(userId.toString())
                    .set(userDetails, SetOptions.merge())
                    .addOnSuccessListener {

                        //findNavController().navigate(R.id.action_registrationFragment_to_userTypeFragment)
                        Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
            } else {
                // Handle failures
                // ...
            }
        }
    }
}