package com.nyayasetu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF1A237E)
private val NavyLight = Color(0xFF283593)
private val Gold = Color(0xFFFFD54F)
private val GoldDark = Color(0xFFFFC107)
private val SurfaceDark = Color(0xFF0D1B2A)
private val SurfaceCard = Color(0xFF1B2838)
private val TextPrimary = Color(0xFFE0E0E0)
private val TextSecondary = Color(0xFFB0BEC5)

data class HomeAction(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val gradientColors: List<Color> = listOf(Navy, NavyLight)
)

private val homeActions = listOf(
    HomeAction("File FIR", Icons.Filled.Description, "fir_form", listOf(Color(0xFF1A237E), Color(0xFF3949AB))),
    HomeAction("Upload FIR\nDocument", Icons.Filled.UploadFile, "fir_upload", listOf(Color(0xFF0D47A1), Color(0xFF1976D2))),
    HomeAction("Record\nVoice FIR", Icons.Filled.Mic, "fir_voice", listOf(Color(0xFF4A148C), Color(0xFF7B1FA2))),
    HomeAction("AI Case\nAnalysis", Icons.Filled.Psychology, "analysis", listOf(Color(0xFF1B5E20), Color(0xFF388E3C))),
    HomeAction("Batch Evidence\nAnalysis", Icons.Filled.FindInPage, "evidence_analysis", listOf(Color(0xFFE65100), Color(0xFFF57C00))),
    HomeAction("Find\nLawyers", Icons.Filled.Gavel, "lawyers", listOf(Color(0xFF880E4F), Color(0xFFC2185B))),
    HomeAction("Legal\nFeed", Icons.AutoMirrored.Filled.Feed, "feed", listOf(Color(0xFF006064), Color(0xFF00838F))),
    HomeAction("My\nConversations", Icons.Filled.Forum, "conversations", listOf(Color(0xFF33691E), Color(0xFF558B2F)))
)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(SurfaceDark, Color(0xFF1A1A2E), Color(0xFF16213E))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // ── Header ──────────────────────────────────────────
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Nyaya Setu",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        ),
                        color = Gold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Your Legal Companion",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                // Logout button
                IconButton(
                    onClick = onLogout,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.08f)
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Greeting card ───────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Navy, Color(0xFF303F9F))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Welcome Back!",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Access all legal services from one place. Tap any option below to get started.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Services",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Grid of circular action buttons ────────────────
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(homeActions) { action ->
                    HomeActionButton(
                        action = action,
                        onClick = { onNavigate(action.route) }
                    )
                }
            }

            // ── Footer ─────────────────────────────────────────
            Text(
                text = "Nyaya Setu • Justice for All",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun HomeActionButton(
    action: HomeAction,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(action.gradientColors)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = action.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium
            ),
            color = TextPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
