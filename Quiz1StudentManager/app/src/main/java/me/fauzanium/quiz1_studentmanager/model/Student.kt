package me.fauzanium.quiz1_studentmanager.model

import java.util.UUID

data class Student(
    val id: String = UUID.randomUUID().toString(),
    val nim: String,
    val name: String,
    val programStudi: String
)
