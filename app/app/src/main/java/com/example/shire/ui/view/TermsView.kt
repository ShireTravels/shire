package com.example.shire.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shire.R
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(
    onNavigateUp: () -> Unit,
    onAcceptTerms: () -> Unit = {},
    onRejectTerms: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val preferences = viewModel.preferences.collectAsStateWithLifecycle().value
    val termsStateText = when (preferences?.termsAccepted) {
        true -> stringResource(id = R.string.terms_accepted)
        false -> stringResource(id = R.string.terms_rejected)
        null -> stringResource(id = R.string.terms_pending)
    }

    val termsStateColor = when (preferences?.termsAccepted) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.error
        null -> MaterialTheme.colorScheme.tertiary
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            stringResource(id = R.string.terms_title), 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(id = R.string.terms_subtitle), 
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, 
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.updateTermsAccepted(false)
                            onRejectTerms()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.reject))
                    }

                    Button(
                        onClick = {
                            viewModel.updateTermsAccepted(true)
                            onAcceptTerms()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(id = R.string.accept))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle(title = "📋 " + stringResource(id = R.string.terms_title))

                Surface(
                    color = termsStateColor.copy(alpha = 0.12f),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.current_status, termsStateText),
                        color = termsStateColor,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TermsSection(
                    title = stringResource(id = R.string.terms_h1),
                    content = stringResource(id = R.string.terms_p1)
                )
                TermsSection(
                    title = stringResource(id = R.string.terms_h2),
                    content = stringResource(id = R.string.terms_p2)
                )
                TermsSection(
                    title = stringResource(id = R.string.terms_h3),
                    content = stringResource(id = R.string.terms_p3)
                )
                TermsSection(
                    title = stringResource(id = R.string.terms_h4),
                    content = stringResource(id = R.string.terms_p4)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun TermsSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsScreenPreview() {
    ShireTheme {
        TermsScreen(onNavigateUp = {})
    }
}
