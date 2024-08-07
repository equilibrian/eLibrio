package su.elibrio.mobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import su.elibrio.mobile.R
import su.elibrio.mobile.model.database.repository.Book

@Composable
fun Book(
    navController: NavController,
    modifier: Modifier = Modifier,
    book: Book?,
    showTitle: Boolean = true,
    aspectRatio: Float = 0.75f
) {
    val ctx = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("book/${book?.id}")
            }
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            val imageLoader = ImageLoader.Builder(ctx)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()

            AsyncImage(
                model = book?.coverPageSrc,
                contentDescription = null,
                imageLoader = imageLoader,
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .aspectRatio(aspectRatio)
                    .fillMaxWidth(),
                placeholder = painterResource(id = R.drawable.no_cover),
                error = painterResource(id = R.drawable.no_cover),
                contentScale = ContentScale.FillBounds,
            )
        }

        if (showTitle) {
            Text(
                text = book!!.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}