package com.pride.stepupentrepreneurprototype.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.pride.stepupentrepreneurprototype.R
import com.pride.stepupentrepreneurprototype.databinding.FragmentUserTypeBinding

class UserTypeFragment : Fragment() {

    private lateinit var binding: FragmentUserTypeBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserTypeBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        binding.materialCardView3.setOnClickListener {

            updateUserDetails("entrepreneur")
        }
        binding.materialCardView.setOnClickListener {

            updateUserDetails("investor")
        }
        binding.materialCardView2.setOnClickListener {

            updateUserDetails("admin")
        }
        return binding.root
    }

    private fun updateUserDetails(userType: String) {

        val userId = auth.currentUser?.uid.toString()
        val userDetails = hashMapOf(
            "user_type" to userType
        )
        db.collection("users").document(userId)
            .set(userDetails, SetOptions.merge())
            .addOnSuccessListener {

                findNavController().navigate(R.id.action_userTypeFragment_to_entrepreneurHomeFragment)
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
    }
}