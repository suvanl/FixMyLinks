package com.suvanl.fixmylinks.ui.components.radio

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.suvanl.fixmylinks.R
import com.suvanl.fixmylinks.ui.util.PreviewContainer

data class RadioOptionData(
    val id: String,
    val title: String?,
    val description: String
) {
    companion object {
        /**
         * [Saver] that converts a [RadioOptionData] object into a Saveable, to ensure that it can
         * be stored in a Bundle.
         */
        val saver: Saver<RadioOptionData, *> = listSaver(
            save = { listOf(it.id, it.title ?: "", it.description) },
            restore = {
                RadioOptionData(
                    id = it[0],
                    title = it[1],
                    description = it[2]
                )
            }
        )
    }
}

object RadioOptionDefaults {
    val spacerHeight = 40.dp
}

@Composable
fun RadioOption(
    data: RadioOptionData,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    spacerHeight: Dp = RadioOptionDefaults.spacerHeight,
) {
    val rowContentDescription = stringResource(
        R.string.radio_option_content_description,
        data.title ?: data.description
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .selectable(
                    selected = isSelected,
                    onClick = onClick,
                    role = Role.RadioButton
                )
                .semantics { contentDescription = rowContentDescription },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null  // null recommended for accessibility with screen readers
            )

            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                if (data.title != null) {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(spacerHeight))
    }
}

@Preview(
    showBackground = true,
    widthDp = 320
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RadioOptionWithTitlePreview() {
    val option = RadioOptionData(
        id = "sample",
        title = "Sample option",
        description = "Sample description text - lorem ipsum sit dolor amet etc etc etc"
    )

    PreviewContainer {
        RadioOption(
            data = option,
            isSelected = true,
            spacerHeight = 0.dp,
            onClick = { /* do nothing */ }
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 320
)
@Preview(
    name = "Dark",
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RadioOptionPreview() {
    val option = RadioOptionData(
        id = "sample",
        title = null,
        description = "Sample option"
    )

    PreviewContainer {
        RadioOption(
            data = option,
            isSelected = true,
            spacerHeight = 0.dp,
            onClick = { /* do nothing */ }
        )
    }
}