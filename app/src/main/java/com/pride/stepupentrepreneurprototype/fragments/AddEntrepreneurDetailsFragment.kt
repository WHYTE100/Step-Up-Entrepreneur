package com.pride.stepupentrepreneurprototype.fragments

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.pride.stepupentrepreneurprototype.R
import com.pride.stepupentrepreneurprototype.databinding.FragmentAddEntrepreneurDetailsBinding
import java.util.*

class AddEntrepreneurDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAddEntrepreneurDetailsBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddEntrepreneurDetailsBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        val genderItems = listOf("Male", "Female")
        val genderAdapter = ArrayAdapter(requireContext(), R.layout.list_item, genderItems)
        (binding.genderEditText as? AutoCompleteTextView)?.setAdapter(genderAdapter)

        val districtsList = listOf("Balaka", "Blantyre", "Chikwawa", "Chiradzulu", "Chitipa", "Dedza","Dowa", "Karonga", "Kasungu", "Lilongwe", "Likoma", "Machinga", "Mangochi", "Mulanje", "Mwanza", "Mchinji", "Mzimba", "Neno", "Nkhotakota", "Ntcheu", "Ntchisi", "Nkhata Bay", "Nsanje", "Phalombe", "Rumphi", "Salima", "Thyolo", "Zomba")
        val districtsAdapter = ArrayAdapter(requireContext(), R.layout.list_item, districtsList)
        (binding.homeDistrictEditText as? AutoCompleteTextView)?.setAdapter(districtsAdapter)

        binding.dobEditText.setOnClickListener {
            selectDOB()
        }
        binding.continueButton.setOnClickListener {

            val userId = auth.currentUser?.uid
            val user = auth.currentUser

            val firstName = binding.firstNameEditText.text.toString()
            val surname = binding.surnameEditText.text.toString()
            val gender = binding.genderEditText.text.toString()
            val dob = binding.dobEditText.text.toString()
            val pob = binding.placeOfBirthEditText.text.toString()
            val homeDistrict = binding.homeDistrictEditText.text.toString()
            val ta = binding.taEditText.text.toString()
            val currentAddress = binding.currentAddressEditText.text.toString()
            val currentDistrict = binding.currentResidenceEditText.text.toString()
            val nationalID = binding.idEditText.text.toString()
            val kin = binding.kinNameEditText.text.toString()
            val disability = binding.disabilityEditText.text.toString()

            val profileUpdates = userProfileChangeRequest {
                displayName = "$firstName $surname"
                //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
            }
            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "User profile updated.")
                    }
                }

            val userDetails = hashMapOf(
                "first_name" to firstName,
                "surname" to surname,
                "gender" to gender,
                "full_name" to "$firstName $surname",
                "date_of_birth" to dob,
                "place_of_birth" to pob,
                "home_district" to homeDistrict,
                "traditional_authority" to ta,
                "current_address" to currentAddress,
                "current_residence" to currentDistrict,
                "national_id" to nationalID,
                "kin_name" to kin,
                "disability" to disability
            )

            db.collection("users").document(userId!!)
                .set(userDetails, SetOptions.merge())
                .addOnSuccessListener {

                    findNavController().navigate(R.id.action_addEntrepreneurImageFragment_to_addEntrepreneurVerificationIdFragment)
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
        }

        return binding.root
    }

    private fun selectDOB() {
        // Get Current Date
        val cal: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate: String = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                binding.dobEditText.setText(selectedDate)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = cal.timeInMillis
        datePickerDialog.show()
    }
}