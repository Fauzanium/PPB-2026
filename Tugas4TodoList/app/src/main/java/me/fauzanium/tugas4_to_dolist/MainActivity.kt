package me.fauzanium.tugas4_to_dolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.fauzanium.tugas4_to_dolist.ui.theme.Tugas4TodoListTheme
import java.util.UUID


data class Task(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val isDone: Boolean = false,
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas4TodoListTheme {
                TodoListApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListApp() {
    val tasks = remember { mutableStateListOf<Task>() }

    var newTaskDescription by remember { mutableStateOf("") }

    val activeTasks = tasks.filter { !it.isDone }
    val doneTasks = tasks.filter { it.isDone }

    fun addTask() {
        if (newTaskDescription.isNotBlank()) {
            tasks.add(Task(description = newTaskDescription.trim()))
            newTaskDescription = "" // reset input setelah nambah
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "To-Do List",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTaskDescription,
                    onValueChange = { newTaskDescription = it },
                    label = { Text("Task Description") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { addTask() },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "To Do (${activeTasks.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                if (activeTasks.isEmpty()) {
                    item {
                        Text(
                            text = "No active tasks.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                        )
                    }
                } else {
                    items(activeTasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onToggleDone = { toggledTask ->
                                val index = tasks.indexOfFirst { it.id == toggledTask.id }
                                if (index != -1) {
                                    tasks[index] = tasks[index].copy(isDone = !toggledTask.isDone)
                                }
                            },
                            onSaveEdit = { editedTask, newDescription ->
                                val index = tasks.indexOfFirst { it.id == editedTask.id }
                                if (index != -1) {
                                    tasks[index] = tasks[index].copy(description = newDescription)
                                }
                            },
                            onDelete = { deletedTask ->
                                tasks.removeIf { it.id == deletedTask.id }
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Done (${doneTasks.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                if (doneTasks.isEmpty()) {
                    item {
                        Text(
                            text = "No completed tasks yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                        )
                    }
                } else {
                    items(doneTasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onToggleDone = { toggledTask ->
                                val index = tasks.indexOfFirst { it.id == toggledTask.id }
                                if (index != -1) {
                                    tasks[index] = tasks[index].copy(isDone = !toggledTask.isDone)
                                }
                            },
                            onSaveEdit = { editedTask, newDescription ->
                                val index = tasks.indexOfFirst { it.id == editedTask.id }
                                if (index != -1) {
                                    tasks[index] = tasks[index].copy(description = newDescription)
                                }
                            },
                            onDelete = { deletedTask ->
                                tasks.removeIf { it.id == deletedTask.id }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onToggleDone: (Task) -> Unit,
    onSaveEdit: (Task, String) -> Unit,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedDescription by remember(task.description, isEditing) { mutableStateOf(task.description) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isDone) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )

                IconButton(
                    onClick = {
                        if (editedDescription.isNotBlank()) {
                            onSaveEdit(task, editedDescription.trim())
                            isEditing = false
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = {
                        editedDescription = task.description
                        isEditing = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel edit",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { onToggleDone(task) }
                )

                Text(
                    text = task.description,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isDone) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

                IconButton(onClick = { isEditing = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit task",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = { onDelete(task) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete task",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListAppPreview() {
    Tugas4TodoListTheme {
        Surface {
            TodoListApp()
        }
    }
}
