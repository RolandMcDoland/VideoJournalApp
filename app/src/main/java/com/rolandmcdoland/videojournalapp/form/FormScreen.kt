package com.rolandmcdoland.videojournalapp.form

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rolandmcdoland.videojournalapp.R
import com.rolandmcdoland.videojournalapp.ui.LoadingScreen
import com.rolandmcdoland.videojournalapp.ui.theme.VideoJournalAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FormScreen(
    videoUri: Uri,
    onVideoSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FormViewModel = koinViewModel()
) {
    val context = LocalContext.current

    var description by rememberSaveable { mutableStateOf("") }

    var thumbnailUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            thumbnailUri = uri
        }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.insertState) {
        when(viewModel.insertState) {
            is InsertSate.Success -> {
                onVideoSaved()
            }
            is InsertSate.Error -> {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.general_error),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
            else -> { /* do nothing */ }
        }
    }

    if(viewModel.insertState is InsertSate.Loading) {
        LoadingScreen()
    } else {
        FormScreenStateless(
            description = description,
            onDescriptionChange = { desc ->
                description = desc
            },
            onPickImageClick = {
                pickImageLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            thumbnailUri = thumbnailUri,
            onSaveClick = {
                scope.launch {
                    viewModel.insertVideo(
                        videoUri.toString(),
                        description,
                        thumbnailUri?.toString()
                    )
                }
            },
            modifier = modifier
        )
    }
}

@Composable
fun FormScreenStateless(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onPickImageClick: () -> Unit,
    thumbnailUri: Uri?,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { extraPadding ->
        Column(modifier = Modifier.fillMaxHeight()) {
            Column(modifier = modifier
                .weight(1f)
                .padding(top = extraPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState())) {
                DescriptionSection(
                    description = description,
                    onDescriptionChange = onDescriptionChange
                )
                ThumbnailSection(
                    onPickImageClick = onPickImageClick,
                    thumbnailUri = thumbnailUri
                )
            }
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp + extraPadding.calculateBottomPadding()
                    )
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }
        }
    }
}

@Composable
fun DescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .padding(horizontal = 16.dp, vertical = 36.dp)
        .fillMaxWidth()) {
        Text(
            text = stringResource(R.string.description_headline),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ThumbnailSection(
    thumbnailUri: Uri?,
    onPickImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()) {
        Text(
            text = stringResource(R.string.thumbnail_headline),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        GlideImage(
            model = thumbnailUri,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChoosePictureButton(
            onPickImageClick = onPickImageClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ChoosePictureButton(
    onPickImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onPickImageClick,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.choose_picture)
        )
    }
}

@Preview
@Composable
fun FormScreenPreview() {
    VideoJournalAppTheme {
        FormScreenStateless(
            description = "",
            onDescriptionChange = { },
            onPickImageClick = { },
            thumbnailUri = null,
            onSaveClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionSectionPreview() {
    VideoJournalAppTheme {
        DescriptionSection(
            description = "",
            onDescriptionChange = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ThumbnailSectionPreview() {
    VideoJournalAppTheme {
        ThumbnailSection(
            thumbnailUri = null,
            onPickImageClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChoosePictureButtonPreview() {
    VideoJournalAppTheme {
        ChoosePictureButton(
            onPickImageClick = { }
        )
    }
}