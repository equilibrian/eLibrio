package su.elibrio.mobile.view.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.model.fb.FictionBook
import su.elibrio.mobile.utils.Utils

@Composable
fun BookView(modifier: Modifier = Modifier, book: Book) {
    var image by remember { mutableStateOf<Bitmap?>(null) }
    var title by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(book) {
        if (book is FictionBook) {
            val imgId = book.description.titleInfo.coverPage?.image?.imageId
            var base64: String? = null
            book.binaries?.forEach { binary ->
                if (binary.id == imgId) base64 = binary.value
            }
            image = base64?.let { Utils.base64ToBitmap(it) }

            title = book.description.titleInfo.bookTitle
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        image?.let {
            Card(
                shape = RoundedCornerShape(2.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .aspectRatio(0.75f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        title?.let {
            Text(
                text = it,
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