package com.gultekinahmetabdullah.softedu.learning.question

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
    fun SingleChoiceQuestion(
        @StringRes titleResourceId: Int,
        @StringRes directionsResourceId: Int,
        possibleAnswers: List<Language>,
        selectedAnswer: Language?,
        onOptionSelected: (Language) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        /*
        QuestionWrapper(
            titleResourceId = titleResourceId,
            directionsResourceId = directionsResourceId,
            modifier = modifier.selectableGroup(),
        ) {
            possibleAnswers.forEach {
                val selected = it == selectedAnswer
                RadioButtonChoice(
                    onOptionSelected = { onOptionSelected(it) },
                    modifier = Modifier.padding(vertical = 8.dp),
                    selected = selected,
                    text = stringResource(id = it.stringResourceId)
                )
            }
        }
         */
    }

    @Composable
    fun RadioButtonChoice(
        text: String,
        selected: Boolean,
        onOptionSelected: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
            border = BorderStroke(
                width = 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            ),
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .selectable(
                    selected,
                    onClick = onOptionSelected,
                    role = Role.RadioButton
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(8.dp))

                Text(text, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                Box(Modifier.padding(8.dp)) {
                    RadioButton(selected, onClick = null)
                }
            }
        }
    }
    data class Language(@StringRes val stringResourceId: Int)