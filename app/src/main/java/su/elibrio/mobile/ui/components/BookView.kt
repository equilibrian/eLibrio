package su.elibrio.mobile.ui.components

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import su.elibrio.mobile.BookActivity
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.utils.Utils

@Composable
fun BookView(modifier: Modifier = Modifier, book: Book) {
    val ctx = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(ctx, BookActivity::class.java).apply {
                    putExtra("BOOK_ID", book.id)
                }
                ctx.startActivity(intent)
            }
    ) {
        Card(
            shape = RoundedCornerShape(2.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Image(
                bitmap = Utils.loadBitmap(ctx, book.coverPageSrc).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .aspectRatio(0.75f)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
        }

        Text(
            text = book.title,
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