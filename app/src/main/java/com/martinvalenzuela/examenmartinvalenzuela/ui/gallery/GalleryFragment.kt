package com.martinvalenzuela.examenmartinvalenzuela.ui.gallery

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.martinvalenzuela.examenmartinvalenzuela.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    lateinit var name:EditText
    lateinit var lastName:EditText
    lateinit var email:EditText
    lateinit var city:EditText
    lateinit var button: Button
    var edited = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        name = binding.name
        lastName=binding.lastName
        email=binding.email
        city=binding.city
        button=binding.button

        button.setOnClickListener{
            register(name.text.toString(), lastName.text.toString(), email.text.toString(), city.text.toString())
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onPause() {
        super.onPause()
        if(!edited)
            return
        name.setText("")
        lastName.setText("")
        email.setText("")
        city.setText("")
    }

    override fun onResume() {
        super.onResume()
        edited = false
    }

    private fun register(nameString:String, lastNameString:String, emailString:String, cityString:String){
        if (nameString=="" || lastNameString == "" || emailString == "" || cityString == ""){
            Toast.makeText(activity,"Favor de llenar todos los campos", Toast.LENGTH_LONG).show()
            return
        }
        val sharedPref = this?.activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString("name", nameString)
        editor?.putString("lastName", lastNameString)
        editor?.putString("email", emailString)
        editor?.putString("city", cityString)
        editor?.apply()

        val navigationView = activity?.findViewById(com.martinvalenzuela.examenmartinvalenzuela.R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(com.martinvalenzuela.examenmartinvalenzuela.R.id.textView) as TextView
        navUsername.text = nameString + " " + lastNameString
        val navEmail = headerView.findViewById<View>(com.martinvalenzuela.examenmartinvalenzuela.R.id.email) as TextView
        navEmail.text = emailString


        Toast.makeText(activity,"Datos Registrados", Toast.LENGTH_LONG).show()
        edited = true

    }
}