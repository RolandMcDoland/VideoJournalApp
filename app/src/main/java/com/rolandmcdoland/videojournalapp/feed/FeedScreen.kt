package com.rolandmcdoland.videojournalapp.feed

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.rolandmcdoland.videojournalapp.R
import com.rolandmcdoland.videojournalapp.ui.theme.VideoJournalAppTheme
import org.koin.androidx.compose.koinViewModel
import videojournal.videodb.VideoEntity
import java.io.File
import androidx.core.net.toUri

@Composable
fun FeedScreen(
    onVideoRecorded: () -> Unit,
    onRequestFileUri: () -> Uri?,
    onShareClick: (Uri) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val captureVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) {
            onVideoRecorded()
        } else {
            Toast
                .makeText(
                    context,
                    context.getString(R.string.video_capture_error),
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted) {
            onRequestFileUri()?.let {
                captureVideoLauncher.launch(
                    it
                )
            }
        } else {
            Toast
                .makeText(
                    context,
                    context.getString(R.string.camera_permission_message),
                    Toast.LENGTH_SHORT
                )
                .show()
        }

    }

    val videos by viewModel.videos.collectAsState(emptyList())

    FeedScreenStateless(
        videos,
        onRequestPlayer = { videoUri -> viewModel.requestPlayer(videoUri) },
        onCaptureVideoClick = {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                onRequestFileUri()?.let {
                    captureVideoLauncher.launch(
                        it
                    )
                }
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        onShareClick = onShareClick,
        modifier = modifier
    )
}

@Composable
fun FeedScreenStateless(
    feedItems: List<VideoEntity>,
    onRequestPlayer: (String) -> Player,
    onCaptureVideoClick: () -> Unit,
    onShareClick: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { extraPadding ->
        Box(modifier = modifier) {
            Feed(
                feedItems = feedItems,
                onRequestPlayer = onRequestPlayer,
                extraBottomPadding = extraPadding.calculateBottomPadding(),
                onShareClick = onShareClick,
                modifier = modifier.padding(top = extraPadding.calculateTopPadding())
            )
            BottomBar(
                paddingNavBar = extraPadding.calculateBottomPadding(),
                onCaptureVideoClick = onCaptureVideoClick,
                modifier = modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
fun BottomBar(
    paddingNavBar: Dp,
    onCaptureVideoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.requiredHeight(88.dp + paddingNavBar)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((maxHeight / 2) + (8.dp + (paddingNavBar / 2)))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .align(Alignment.BottomStart)
        )
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 16.dp + paddingNavBar)
        ) {
            IconButton(onClick = onCaptureVideoClick, modifier = Modifier.size(72.dp)) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_video),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun Feed(
    feedItems: List<VideoEntity>,
    onRequestPlayer: (String) -> Player,
    extraBottomPadding: Dp,
    onShareClick: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    var playingVideoId by rememberSaveable { mutableLongStateOf(-1L) }

    if(feedItems.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 88.dp + extraBottomPadding),
            modifier = modifier.fillMaxSize()
        ) {
            items(feedItems) {
                FeedItem(
                    video = it,
                    isPlaying = it.id == playingVideoId,
                    onIsPlayingChanged = { videoId -> playingVideoId = videoId },
                    onRequestPlayer = onRequestPlayer,
                    onShareClick = onShareClick
                )
            }
        }
    } else {
        EmptyFeedMessage()
    }
}

@Composable
fun FeedItem(
    video: VideoEntity,
    isPlaying: Boolean,
    onIsPlayingChanged: (Long) -> Unit,
    onRequestPlayer: (String) -> Player,
    onShareClick: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                VideoPlayer(
                    video = video,
                    isPlaying = isPlaying,
                    onIsPlayingChanged = onIsPlayingChanged,
                    onRequestPlayer = onRequestPlayer
                )
                video.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { onShareClick(video.videoUri.toUri()) },
                        modifier = Modifier
                            .padding(end = 8.dp, bottom = 8.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentDescription = stringResource(R.string.play_video),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoPlayer(
    video: VideoEntity,
    isPlaying: Boolean,
    onIsPlayingChanged: (Long) -> Unit,
    onRequestPlayer: (String) -> Player,
    modifier: Modifier = Modifier
) {
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        if (isPlaying) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).also {
                        it.player = onRequestPlayer(video.videoUri)
                        it.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    }
                },
                update = {
                    when (lifecycle) {
                        Lifecycle.Event.ON_PAUSE -> {
                            it.onPause()
                            it.player?.pause()
                        }
                        Lifecycle.Event.ON_RESUME -> {
                            it.onResume()
                        }
                        else -> Unit
                    }
                },
                modifier = Modifier
                    .defaultMinSize(minHeight = 256.dp)
                    .fillMaxWidth()
            )
        } else {
            GlideImage(
                model = video.thumbnailUri,
                contentDescription = null,
                loading = placeholder(R.drawable.video_placeholder),
                failure = placeholder(R.drawable.video_placeholder),
                modifier = Modifier.fillMaxWidth()
            )
            IconButton(
                onClick = { onIsPlayingChanged(video.id) },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.play_video),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyFeedMessage(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.empty_feed),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
        )
    }
}

@Preview
@Composable
fun FeedScreenPreview() {
    VideoJournalAppTheme {
        FeedScreenStateless(
            feedItems = listOf(
                VideoEntity(0L, 0L, "", "Video 1", ""),
                VideoEntity(1L, 0L, "", "Video 2", ""),
                VideoEntity(2L, 0L, "", "Video 3", "")
            ),
            onRequestPlayer = { TODO("Not required for preview") },
            onCaptureVideoClick = { },
            onShareClick = {  }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    VideoJournalAppTheme {
        BottomBar(
            paddingNavBar = 0.dp,
            onCaptureVideoClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeedPreview() {
    VideoJournalAppTheme {
        Feed(
            feedItems = listOf(
                VideoEntity(0L, 0L, "", "Video 1", ""),
                VideoEntity(1L, 0L, "", "Video 2", ""),
                VideoEntity(2L, 0L, "", "Video 3", "")
            ),
            onRequestPlayer = { TODO("Not required for preview") },
            extraBottomPadding = 0.dp,
            onShareClick = {  }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeedItemPreview() {
    VideoJournalAppTheme {
        FeedItem(
            video = VideoEntity(
                id = 0L,
                timestamp = 0L,
                videoUri = "",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eu felis sed mi fringilla consectetur dignissim et est. Cras quis faucibus libero. Praesent a ipsum a sem posuere sollicitudin sed vel elit. Praesent non venenatis mauris. Aliquam consequat commodo enim, a rhoncus tortor convallis pharetra. Praesent rhoncus accumsan lacinia. Nunc dictum auctor magna, nec lobortis urna. Duis enim quam, euismod ut velit eu, aliquet lacinia lorem. Vivamus arcu orci, malesuada vitae lorem eget, consectetur porta nisi. Etiam ut est pulvinar, condimentum augue sit amet, finibus magna. Sed tempor sit amet lorem in commodo. Phasellus posuere ipsum neque, sed dignissim enim vestibulum non.",
                thumbnailUri = ""
            ),
            isPlaying = false,
            onIsPlayingChanged = { },
            onRequestPlayer = { TODO("Not required for preview") },
            onShareClick = {  }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VideoPlayerPreview() {
    VideoJournalAppTheme {
        VideoPlayer(
            video = VideoEntity(
                id = 0L,
                timestamp = 0L,
                videoUri = "",
                description = "",
                thumbnailUri = ""
            ),
            isPlaying = false,
            onIsPlayingChanged = { },
            onRequestPlayer = { TODO("Not required for preview") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyFeedMessagePreview() {
    VideoJournalAppTheme {
        EmptyFeedMessage()
    }
}
