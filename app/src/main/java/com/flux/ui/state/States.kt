package com.flux.ui.state

data class States (
    val notesState: NotesState,
    val eventState: EventState,
    val habitState: HabitState,
    val todoState: TodoState,
    val workspaceState: WorkspaceState,
    val settings: Settings
)