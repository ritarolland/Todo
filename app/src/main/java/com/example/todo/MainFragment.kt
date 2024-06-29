package com.example.todo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.FragmentMainBinding
import java.util.Date

class MainFragment : Fragment(), OnNewTaskAddedListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter : TodoAdapter

    private val tasks = mutableListOf(
        TodoItem("1", "Task 1", Importance.NORMAL, Date(), false, Date()),
        TodoItem("2", "Task 2 врслсоиоиам ылтмлиалом олавмо лтоми омао аомловииаоимовим воаимоиоимаоим ваоииам авмоиолмаи воилмаорвоам ирвприопиолипв прирвоипомио мваооиммлиоам о оаивм оаимвои оавми", Importance.HIGH, null, true, Date(), Date()),
        TodoItem("3", "Task 3", Importance.LOW, Date(), false, Date()),
        TodoItem("1", "Task 1", Importance.NORMAL, Date(), false, Date()),
        TodoItem("2", "Task 2", Importance.HIGH, null, true, Date(), Date()),
        TodoItem("3", "Task 3", Importance.LOW, Date(), false, Date()),
        TodoItem("1", "Task 1", Importance.NORMAL, Date(), false, Date()),
        TodoItem("2", "Task 2", Importance.HIGH, null, true, Date(), Date()),
        TodoItem("3", "Task 3", Importance.LOW, Date(), false, Date()),
        TodoItem("1", "Task 1", Importance.NORMAL, Date(), false, Date()),
        TodoItem("2", "Task 2", Importance.HIGH, null, true, Date(), Date()),
        TodoItem("3", "Task 3", Importance.LOW, Date(), false, Date()),
        TodoItem("1", "Task 1", Importance.NORMAL, Date(), false, Date()),
        TodoItem("2", "Task 2", Importance.HIGH, null, true, Date(), Date()),
        TodoItem("3", "Task 3", Importance.LOW, Date(), false, Date()),
        TodoItem("1", "Task 1", Importance.NORMAL, Date(), false, Date()),
        TodoItem("2", "Task 2", Importance.HIGH, null, true, Date(), Date()),
        TodoItem("3", "Task 3", Importance.LOW, Date(), false, Date()),
        "Новое"

    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingButton.setOnClickListener { openAddFragment() }
        taskAdapter = TodoAdapter(tasks,
            onTaskClick = { todoItem ->
                val bundle = Bundle().apply {
                    putParcelable("todo_item_key", todoItem)
                }
                val fragment = AddFragment().apply {
                    arguments = bundle
                }
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onEndItemClick = {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, AddFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }, updateCompletedTasksCount = {updateCompletedTasksCount()})
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }

        updateCompletedTasksCount()

        binding.eye.setOnClickListener{taskAdapter.toggleShowCompletedTasks()}

        val swipeHandler = SwipeToDeleteCallback(requireContext(), taskAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    @SuppressLint("SetTextI18n")
    private fun updateCompletedTasksCount() {
        val completedTasksCount = taskAdapter.countCompletedTasks()
        binding.done.text = "Выполнено - $completedTasksCount"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTodoList(newList: List<TodoItem>) {
        tasks.clear()
        tasks.addAll(newList)
        taskAdapter.notifyDataSetChanged()
    }

    private fun openAddFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, AddFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNewTaskAdded(task: TodoItem) {
        taskAdapter.run {
            addTask(task)
            notifyDataSetChanged()
        }
    }
}