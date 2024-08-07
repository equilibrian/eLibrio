package su.elibrio.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import su.elibrio.mobile.R

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    minimizedMaxLines: Int = 6,
    style: TextStyle
) {
    var expanded by remember { mutableStateOf(false) }
    var hasVisualOverflow by remember { mutableStateOf(false) }

    fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }

    Box(modifier = modifier.noRippleClickable {}) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            onTextLayout = { hasVisualOverflow = it.hasVisualOverflow },
            style = style
        )
        if (hasVisualOverflow) {
            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
                verticalAlignment = Alignment.Bottom
            ) {
                val lineHeightDp: Dp = with(LocalDensity.current) { style.lineHeight.toDp() }
                Spacer(
                    modifier = Modifier
                        .width(48.dp)
                        .height(lineHeightDp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                            )
                        )
                )
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(start = 4.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { expanded = !expanded }
                        ),
                    text = stringResource(id = R.string.st_show_more),
                    color = MaterialTheme.colorScheme.primary,
                    style = style
                )
            }
        }
    }
}
