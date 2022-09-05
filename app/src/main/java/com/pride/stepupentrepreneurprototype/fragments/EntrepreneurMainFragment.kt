package com.pride.stepupentrepreneurprototype.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pride.stepupentrepreneurprototype.R
import com.pride.stepupentrepreneurprototype.databinding.FragmentEntreprenuerHomeBinding

class EntrepreneurMainFragment : Fragment() {

    private lateinit var binding: FragmentEntreprenuerHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEntreprenuerHomeBinding.inflate(inflater, container, false)

        replaceFragment(EntrepreneurHomeFragment())

        // Initialize Firebase Auth
        /*auth = Firebase.auth
        binding.textView3.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_entrepreneurHomeFragment_to_loginFragment)
        }*/
        binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){

                R.id.page_1 -> replaceFragment(EntrepreneurHomeFragment())
                R.id.page_2 -> replaceFragment(EntrepreneurAcademyFragment())
                R.id.page_3 -> replaceFragment(EntrepreneurProfileFragment())

                else ->{}
            }

            true

        }

        return binding.root
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.entrepreneurHomeFrame,fragment)
        fragmentTransaction.commit()


    }

}