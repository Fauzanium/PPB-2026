Nama: Fauzan Hasyim
NRP: 5053251044

Tugas 5 Membuat To-Do List

Aplikasi pengelolaan tugas To-Do List berbasis Android Jetpack Compose yang menerapkan komponen Material Design 3. Dan pemisahan daftar berdasarkan status penyelesaian tugas (*Active* dan *Done*).

---
### 1. Model Data (`Task`)

Struktur data untuk merepresentasikan satu item tugas.

```kotlin
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val isDone: Boolean = false,
)

```

* **`id`**: Menghasilkan ID unik secara otomatis menggunakan `UUID.randomUUID()` untuk mengidentifikasi tiap tugas di dalam daftar.
* **`description`**: Menyimpan teks deskripsi tugas.
* **`isDone`**: Menandai status penyelesaian tugas dengan nilai awal `false`.

---

### 2. Komponen Utama & Pengelolaan State (`TodoListApp`)

Fungsi composable utama yang mengatur layout dan logika state aplikasi.

```kotlin
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
            newTaskDescription = "" 
        }
    }
    
}

```

* **`mutableStateListOf<Task>()`**: Menyimpan daftar tugas dalam list agar UI otomatis melakukan update saat tugas ditambah, diubah, atau dihapus.
* `activeTasks` & `doneTasks**`: Memfilter daftar tugas menjadi dua grup terpisah berdasarkan atribut `isDone`.
* **`addTask()`**: Menambahkan tugas baru jika input tidak kosong, kemudian membesihkan isi kolom input.

---

### 3. Formulir Input Tugas

Bagian untuk menerima input teks dari pengguna dan menambahkan tugas baru.

```kotlin
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

```

* **`OutlinedTextField`**: Komponen input teks bergaya outline untuk mengetikkan nama tugas baru. `Modifier.weight(1f)` membuat bidang input mengambil sisa ruang horizontal yang tersedia.
* **`Button`**: Tombol untuk mengeksekusi function `addTask()`.

---

### 4. Daftar Tugas Dinamis (`LazyColumn`)

Menampilkan daftar tugas secara efisien menggunakan `LazyColumn`.

```kotlin
LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.fillMaxSize()
) {
    item {
        Text(text = "To Do (${activeTasks.size})", ...)
    }

    if (activeTasks.isEmpty()) {
        item { Text(text = "No active tasks.", ...) }
    } else {
        items(activeTasks, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onToggleDone = { ... },
                onSaveEdit = { ... },
                onDelete = { ... }
            )
        }
    }
    
}

```

* **`LazyColumn`**: Komponen daftar yang hanya merender elemen yang terlihat di layar untuk menghemat penggunaan memori.
* **`items(..., key = { it.id })`**: Merender daftar item secara dinamis dengan menyertakan `key` berbasis ID unik agar animasi dan state tiap item terjaga dengan baik saat posisi list berubah.

---

### 5. Komponen Item Tugas (`TaskItem`)

Komponen visual untuk menampilkan satu item tugas beserta kontrol perubahannya (Checkbox, Edit, Delete).

```kotlin
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
    // ...
}

```

* **`isEditing`**: State lokal untuk menentukan apakah baris tugas sedang dalam mode edit teks atau mode tampilan normal.
* **Modus Tampilan Normal**:
* **`Checkbox`**: Memicu lambda `onToggleDone` untuk mengubah status `isDone`.
* **`Text` dengan `TextDecoration.LineThrough**`: Memberikan efek dicoret pada teks jika tugas sudah selesai (`isDone == true`).
* **`IconButton` (Edit & Delete)**: Menjalankan tombol aksi ubah mode edit atau menghapus item via `onDelete`.


* **Modus Mode Edit**:
* Mengganti tampilan teks menjadi `OutlinedTextField` beserta tombol simpan (`Check`) dan batal (`Close`).



---
