package com.example.todo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.todo.databinding.FragmentAddBinding
import java.util.Calendar
import java.util.Date

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()
    private var currentDate:Date? = null

    private var listener: OnNewTaskAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNewTaskAddedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnNewTaskAddedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = binding.spinnerImportance
        spinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.importance_options,
            android.R.layout.simple_spinner_item
        )

        binding.switchCalendar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showDatePicker()
            } else {
                binding.date.visibility = View.INVISIBLE
            }
        }

        binding.delete.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.save.setOnClickListener {
            val selectedImportance = spinner.selectedItem.toString()
            val newTask = TodoItem(
                "1",
                binding.editText.text.toString(),
                getImportanceFromString(selectedImportance),
                currentDate,
                false,
                Date()
            )
            try {
                listener?.onNewTaskAdded(newTask)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.date.text = selectedDate

                currentDate = selectedCalendar.time
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun getImportanceFromString(importance: String): Importance {
        return when (importance) {
            "Низкая" -> Importance.LOW
            "Обычная" -> Importance.NORMAL
            "Высокая" -> Importance.HIGH
            else -> Importance.NORMAL
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}