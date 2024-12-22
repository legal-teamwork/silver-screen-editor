package org.legalteamwork.silverscreen.ps

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import io.github.oshai.kotlinlogging.KotlinLogging
import androidx.compose.ui.unit.*
import org.legalteamwork.silverscreen.resources.AppTheme
import org.legalteamwork.silverscreen.resources.ResourceManagerTheme
import org.legalteamwork.silverscreen.resources.Strings
import org.legalteamwork.silverscreen.save.Project
import org.legalteamwork.silverscreen.save.Resolution

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
    crossinline toString: (T) -> Any? = { it },
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
            value = toString(targetVar.value).toString(),
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
                        Text(toString(v).toString(), color = Color.White)
                    }
                }
            }
        }
    }
}

object ProjectSettingsWindow {
    val fpsOptions = listOf(30.0, 60.0, 90.0, 120.0, 240.0)

    var opened = mutableStateOf(false)
    private val mBitrate = mutableStateOf(14100)
    private val mFPS = mutableStateOf(30.0)
    private val mResolution = mutableStateOf(2)

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
        Surface(
            color = AppTheme.SETTINGS_BACKGROUND_COLOR,
            //border = BorderStroke(2.dp, AppTheme.SETTINGS_BORDER_COLOR),
            //shape = RoundedCornerShape(8.dp),
            ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
                    //.border(2.dp, AppTheme.RENDER_BORDER_COLOR, shape = RoundedCornerShape(8.dp))
            ) {
                Column {
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Text(text = Strings.BITRATE, color = AppTheme.SETTINGS_TEXT_COLOR, fontSize = 15.sp, modifier = Modifier.padding(start = 24.dp))

                            InputField(
                                title = "",
                                targetVar = mBitrate,
                                fromString = String::toIntOrNull,
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Text(text = Strings.FPS, color = AppTheme.SETTINGS_TEXT_COLOR, fontSize = 15.sp, modifier = Modifier.padding(start = 46.dp))

                            InputFieldWithOptions(
                                title = "",
                                targetVar = mFPS,
                                fromString = String::toDoubleOrNull,
                                options = fpsOptions
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Text(text = Strings.RESOLUTION, color = AppTheme.SETTINGS_TEXT_COLOR, fontSize = 15.sp)

                            InputFieldWithOptions(
                                title = "",
                                targetVar = mResolution,
                                enabled = false,
                                options = (0..<Resolution.available.size).toList(),
                                toString = { Resolution.available[it] }
                            )
                        }
                    }

                    val buttonColor = AppTheme.SETTINGS_MAIN_COLOR
                    val contentColor = AppTheme.SETTINGS_ACTIVE_COLOR

                    Row {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = buttonColor,
                                contentColor = contentColor
                            ),
                            modifier = Modifier.padding(10.dp).weight(1.0f),
                            onClick = { apply(); close() }
                        ) { Text(Strings.SAVE_AND_CLOSE) }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = buttonColor,
                                contentColor = contentColor
                            ),
                            modifier = Modifier.padding(10.dp).weight(1.0f),
                            onClick = { apply() }
                        ) { Text(Strings.APPLY) }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = buttonColor,
                                contentColor = contentColor
                            ),
                            modifier = Modifier.padding(10.dp).weight(1.0f),
                            onClick = { close() }
                        ) { Text(Strings.CLOSE) }
                    }
                }
            }
        }
    }
}

