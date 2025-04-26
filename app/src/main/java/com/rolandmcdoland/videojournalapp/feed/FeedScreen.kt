package com.rolandmcdoland.videojournalapp.feed

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rolandmcdoland.videojournalapp.R
import com.rolandmcdoland.videojournalapp.ui.theme.VideoJournalAppTheme

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier
) {
    // TODO: Add feed items handling
    FeedScreenStateless(listOf())
}

@Composable
fun FeedScreenStateless(
    feedItems: List<String>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { extraPadding ->
        Box(modifier = modifier) {
            Feed(
                feedItems = feedItems,
                modifier = modifier.padding(top = extraPadding.calculateTopPadding())
            )
            BottomBar(
                paddingNavBar = extraPadding.calculateBottomPadding(),
                modifier = modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@Composable
fun BottomBar(
    paddingNavBar: Dp,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.requiredHeight(88.dp + paddingNavBar)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((maxHeight / 2) + (8.dp + (paddingNavBar / 2)))
                .background(MaterialTheme.colorScheme.onSurface)
                .align(Alignment.BottomStart)
        )
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 16.dp + paddingNavBar)
        ) {
            IconButton(onClick = { }, modifier = Modifier.size(72.dp)) {
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
    feedItems: List<String>,
    modifier: Modifier = Modifier
) {
    if(feedItems.isNotEmpty()) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(feedItems) {
                FeedItem(it)
            }
        }
    } else {
        EmptyFeedMessage()
    }
}

@Composable
fun FeedItem(
    description: String,
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
                // TODO: Add player surface here
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = if(isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
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
            feedItems = listOf("Description 1", "Description 2", "Description 3")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    VideoJournalAppTheme {
        BottomBar(
            paddingNavBar = 0.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeedPreview() {
    VideoJournalAppTheme {
        Feed(
            feedItems = listOf("Description 1", "Description 2", "Description 3")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FeedItemPreview() {
    VideoJournalAppTheme {
        FeedItem(
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eu felis sed mi fringilla consectetur dignissim et est. Cras quis faucibus libero. Praesent a ipsum a sem posuere sollicitudin sed vel elit. Praesent non venenatis mauris. Aliquam consequat commodo enim, a rhoncus tortor convallis pharetra. Praesent rhoncus accumsan lacinia. Nunc dictum auctor magna, nec lobortis urna. Duis enim quam, euismod ut velit eu, aliquet lacinia lorem. Vivamus arcu orci, malesuada vitae lorem eget, consectetur porta nisi. Etiam ut est pulvinar, condimentum augue sit amet, finibus magna. Sed tempor sit amet lorem in commodo. Phasellus posuere ipsum neque, sed dignissim enim vestibulum non."
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
