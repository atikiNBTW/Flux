package com.flux.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Workspaces
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.flux.R
import com.flux.data.model.WorkspaceModel
import com.flux.other.icons
import com.flux.ui.events.WorkspaceEvents

@Composable
fun WorkSpacesCard(
    workspace: WorkspaceModel,
    onClick: ()->Unit,
    onWorkspaceEvents: (WorkspaceEvents)->Unit
){
    var showDeleteAlert by remember { mutableStateOf(false) }

    if(showDeleteAlert){
        DeleteAlert(onDismissRequest = {
            showDeleteAlert=false
        }, onConfirmation = {
            showDeleteAlert=false
            onWorkspaceEvents(WorkspaceEvents.DeleteSpace(workspace))
        })
    }

    Row(modifier = Modifier.clickable{onClick()}) {
        Row(Modifier.padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(icons[workspace.icon], null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                if(workspace.passKey.isNotBlank()) Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Text(workspace.title, color = MaterialTheme.colorScheme.primary, maxLines = 1, modifier = Modifier.alpha(0.90f), overflow = TextOverflow.Ellipsis, fontSize = 18.sp)
            }
            WorkspacePreviewMore(
                isPinned = workspace.isPinned,
                onDelete = { showDeleteAlert=true },
                onTogglePinned = { onWorkspaceEvents(WorkspaceEvents.UpsertSpace(workspace.copy(isPinned = !workspace.isPinned))) }
            )
        }
    }
}

@Composable
fun PinnedSpacesCard(
    radius: Int,
    isLocked: Boolean=false,
    cover: String,
    title: String,
    iconIndex: Int,
    onClick: ()->Unit
){
    ElevatedCard(
        shape = shapeManager(radius = radius*2),
        modifier = Modifier.height(160.dp).width(140.dp).padding(2.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp))
    ) {
        Column(Modifier.fillMaxSize()){
            Box(modifier = Modifier.fillMaxWidth()){
                if(cover.isBlank()){
                    Box(Modifier.fillMaxWidth().height(80.dp).alpha(0.1f).background(MaterialTheme.colorScheme.onSurface))
                }
                else{
                    AsyncImage(model = cover.toUri(), modifier = Modifier.height(80.dp).alpha(0.8f), contentDescription = null, contentScale = ContentScale.Crop)
                }
                Row(modifier = Modifier.align(Alignment.BottomStart).padding(top = 70.dp, start = 2.dp)) {
                    Icon(icons[iconIndex], null)
                    if(isLocked) Icon(Icons.Default.Lock, null)
                }

            }
            Text(title, modifier = Modifier.padding(start = 4.dp), maxLines = 2, overflow = TextOverflow.Clip)
        }
    }
}

@Composable
fun EmptySpaces(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Workspaces,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(stringResource(R.string.Empty_Workspace))
    }
}