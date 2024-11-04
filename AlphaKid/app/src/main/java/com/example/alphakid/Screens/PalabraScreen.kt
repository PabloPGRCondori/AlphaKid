package com.example.alphakid.Screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.alphakid.databinding.FragmentPalabraBinding
import com.example.alphakid.ViewModel.PalabraViewModel

class PalabraScreen : Fragment() {

    private var _binding: FragmentPalabraBinding? = null
    private val binding get() = _binding!!
    private val palabraViewModel: PalabraViewModel by viewModels()

    private var palabraActual: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPalabraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        palabraViewModel.palabras.observe(viewLifecycleOwner, Observer { palabras ->
            palabraActual = palabras.random().palabra
            binding.palabraText.text = palabraActual
        })

        binding.validarButton.setOnClickListener {
            validarPalabra(binding.inputPalabra.text.toString())
        }

        binding.siguienteButton.setOnClickListener {
            palabraViewModel.fetchPalabras()
        }
    }

    private fun validarPalabra(palabraIngresada: String) {
        if (palabraIngresada.equals(palabraActual, ignoreCase = true)) {
            Toast.makeText(requireContext(), "¡Correcto!", Toast.LENGTH_SHORT).show()
            // Reproduce el audio si es correcto
        } else {
            Toast.makeText(requireContext(), "Incorrecto, inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}