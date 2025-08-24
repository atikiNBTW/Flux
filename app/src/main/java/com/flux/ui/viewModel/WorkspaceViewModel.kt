package com.flux.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flux.data.model.WorkspaceModel
import com.flux.data.repository.WorkspaceRepository
import com.flux.ui.effects.ScreenEffect
import com.flux.ui.events.WorkspaceEvents
import com.flux.ui.state.WorkspaceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkspaceViewModel @Inject constructor(
    val repository: WorkspaceRepository
) : ViewModel() {

    private val _state: MutableStateFlow<WorkspaceState> = MutableStateFlow(WorkspaceState())
    private val _effect: Channel<ScreenEffect> = Channel()
    val state: StateFlow<WorkspaceState> = _state.asStateFlow()
    val effect = _effect.receiveAsFlow()

    init { loadWorkspace()  }

    private fun setEffect(builder: () -> ScreenEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
    fun onEvent(event: WorkspaceEvents) { viewModelScope.launch { reduce(event = event) } }
    private fun updateState(reducer: (WorkspaceState) -> WorkspaceState) { _state.value = reducer(_state.value) }

    private fun reduce(event: WorkspaceEvents) {
        when (event) {
            is WorkspaceEvents.DeleteSpace -> deleteWorkspace(event.space)
            is WorkspaceEvents.UpsertSpace -> upsertWorkspace(event.space)
            is WorkspaceEvents.UpsertSpaces -> togglePinWorkspaces(event.spaces)
        }
    }

    private fun togglePinWorkspaces(spaces: List<WorkspaceModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            val isAllPinned=spaces.all { it.isPinned }
            if(isAllPinned){
                val updatedWorkspaces = spaces.map { it.copy(isPinned = false) }
                repository.upsertWorkspaces(updatedWorkspaces)
            }
            else{
                val updatedWorkspaces = spaces.map { it.copy(isPinned = true) }
                repository.upsertWorkspaces(updatedWorkspaces)
            }
        }
    }

    private fun loadWorkspace() {
        updateState { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.loadAllWorkspaces()
                .collect { data -> updateState { it.copy(isLoading = false, allSpaces = data) } }
        }
    }

    private fun upsertWorkspace(data: WorkspaceModel) {
        viewModelScope.launch(Dispatchers.IO) { repository.upsertWorkspace(data) }
    }

    private fun deleteWorkspace(space: WorkspaceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWorkspace(space)
            setEffect { ScreenEffect.ShowSnackBarMessage("Workspace Deleted") }
        }
    }
}