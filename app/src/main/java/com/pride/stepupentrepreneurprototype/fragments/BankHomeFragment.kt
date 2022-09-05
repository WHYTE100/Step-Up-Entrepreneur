package com.pride.stepupentrepreneurprototype.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.pride.stepupentrepreneurprototype.R
import com.pride.stepupentrepreneurprototype.databinding.FragmentBankHomeBinding
import com.pride.stepupentrepreneurprototype.databinding.FragmentRegistrationBinding

class BankHomeFragment : Fragment() {

    private lateinit var binding: FragmentBankHomeBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBankHomeBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.textView4.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_bankHomeFragment_to_loginFragment)
        }
        return binding.root
    }

}