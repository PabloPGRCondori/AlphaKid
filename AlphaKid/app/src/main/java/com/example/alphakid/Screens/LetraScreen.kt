package com.example.alphakid.Screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.alphakid.ViewModel.LetraViewModel
import com.example.alphakid.databinding.FragmentLetraBinding

class LetraScreen : Fragment() {

    private var _binding: FragmentLetraBinding? = null
    private val binding get() = _binding!!
    private val letraViewModel: LetraViewModel by viewModels()

    private var letraActual: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLetraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        letraViewModel.letras.observe(viewLifecycleOwner, Observer { letras ->
            letraActual = letras.random().letra
            binding.letraText.text = letraActual
        })

        binding.validarButton.setOnClickListener {
            validarLetra(binding.inputLetra.text.toString())
        }

        binding.siguienteButton.setOnClickListener {
            letraViewModel.fetchLetras()
        }
    }

    private fun validarLetra(letraIngresada: String) {
        if (letraIngresada.equals(letraActual, ignoreCase = true)) {
            Toast.makeText(requireContext(), "¡Correcto!", Toast.LENGTH_SHORT).show()
            // Aquí puedes reproducir el audio si es correcto
        } else {
            Toast.makeText(requireContext(), "Incorrecto, inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}