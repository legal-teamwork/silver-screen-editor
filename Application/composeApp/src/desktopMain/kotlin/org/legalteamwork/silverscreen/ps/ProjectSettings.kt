package org.legalteamwork.silverscreen.ps

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import io.github.oshai.kotlinlogging.KotlinLogging
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.save.Project

private val logger = KotlinLogging.logger {}

@Composable
private inline fun<T> InputField(
    title: String,
    targetVar: MutableState<T>,
    crossinline fromString: (String) -> T? = { null },
    enabled: Boolean = true
) {
    TextField(
        label = { Text(title, color = Color.White) },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        value = targetVar.value.toString(),
        onValueChange = { newValue: String ->
            fromString(newValue)?.let {
                logger.info { "$title parameter change via input: $it" }
                targetVar.value = it
            }
        }
    )
}

@Composable
private inline fun<T> InputFieldWithOptions(
    title: String,
    targetVar: MutableState<T>,
    options: List<T>,
    crossinline fromString: (String) -> T? = { null },
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
        TextField(
            label = { Text(title, color = Color.White) },
            enabled = enabled,
            modifier = Modifier
                .weight(1.0f)
                .fillMaxHeight(),
            textStyle = TextStyle(color = Color.White),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            value = targetVar.value.toString(),
            onValueChange = { newValue: String ->
                fromString(newValue)?.let {
                    logger.info { "$title parameter change via input: $it" }
                    targetVar.value = it
                }
            }
        )
        Box {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .background(color = Color(0xFF3C3C3C))
                    .fillMaxHeight()
            ) {
                Icon(Icons.Default.ArrowDropDown, "Show menu")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(color = Color.DarkGray)
            ) {
                options.forEach { v ->
                    DropdownMenuItem(
                        onClick = {
                            logger.info { "$title parameter change via choice: $v" }
                            targetVar.value = v
                            expanded = false
                        },
                    ) {
                        Text(v.toString(), color = Color.White)
                    }
                }
            }
        }
    }
}

object ProjectSettingsWindow {
    val fpsOptions = listOf(30.0, 60.0, 90.0, 120.0, 240.0)
    val resolutionOptions = listOf(
        "640x480 (SD; 480p; 4:3)",
        "1280x720 (HD; 720p; 16:9)",
        "1920x1080 (Full HD; 1080p; 16:9)",
        "2560x1440 (Quad HD; 1440p; 16:9)",
        "3840x2160 (Ultra HD; 4K; 16:9)",
        "7680x4320 (Full Ultra HD; 8K; 16:9)"
    )

    var opened = mutableStateOf(false)
    private val mBitrate = mutableStateOf(14100)
    private val mFPS = mutableStateOf(30.0)
    private val mResolution = mutableStateOf(resolutionOptions[0])

    fun sync() {
        logger.info { "Loading project parameters into settings window..." }
        Project.change {
            mBitrate.value = bitrate
            mFPS.value = fps
            mResolution.value = resolution
        }
    }

    fun apply() {
        logger.info { "Applying changes to project..." }
        Project.change {
            bitrate = mBitrate.value
            fps = mFPS.value
            resolution = mResolution.value
        }
    }

    val isOpened
        get() = opened.value
    fun open() { sync(); opened.value = true }
    fun close() { opened.value = false }

    @Composable
    fun compose() {
        Surface(color = Color.DarkGray) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column {
                    Column(
                        modifier = Modifier.weight(1.0f).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InputField(
                            title = Strings.BITRATE,
                            targetVar = mBitrate,
                            fromString = String::toIntOrNull,
                        )
                        InputFieldWithOptions(
                            title = Strings.FPS,
                            targetVar = mFPS,
                            fromString = String::toDoubleOrNull,
                            options = fpsOptions
                        )
                        InputFieldWithOptions(
                            title = Strings.RESOLUTION,
                            targetVar = mResolution,
                            enabled = false,
                            options = resolutionOptions
                        )
                    }

                    Row {
                        Button(
                            modifier = Modifier.padding(10.dp).weight(1.0f),
                            onClick = { apply(); close() }
                        ) { Text(Strings.SAVE_AND_CLOSE) }

                        Button(
                            modifier = Modifier.padding(10.dp).weight(1.0f),
                            onClick = { apply() }
                        ) { Text(Strings.APPLY) }

                        Button(
                            modifier = Modifier.padding(10.dp).weight(1.0f),
                            onClick = { close() }
                        ) { Text(Strings.CLOSE) }
                    }
                }
            }
        }
    }
}

