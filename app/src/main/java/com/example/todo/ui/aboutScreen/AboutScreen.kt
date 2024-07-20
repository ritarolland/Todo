package com.example.todo.ui.aboutScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.example.todo.R
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.DivViewFacade
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div2.DivData
import org.json.JSONObject

@Composable
fun AboutScreen(navController: NavHostController) {
    val currentContext = LocalContext.current
    val contextWrapper = remember { ContextThemeWrapper(currentContext, R.style.Theme_Todo) }
    val configuration = remember { createDivConfiguration(contextWrapper, navController) }
    val divData = remember { getData(currentContext) }

    if (divData == null) {
        Log.e("BBB", "DivData is null")
        return
    }

    AndroidView(
        factory = { context ->
            Div2View(
                Div2Context(
                    contextWrapper,
                    configuration,
                    R.style.Theme_Todo,
                    context as LifecycleOwner
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { div2View ->
        Log.d("BBB", "Setting data to Div2View")
        div2View.setData(divData, DivDataTag("tag"))
    }
}

private fun createDivConfiguration(context: Context, navController: NavHostController): DivConfiguration {
    val imageLoader = PicassoDivImageLoader(context)
    return DivConfiguration.Builder(imageLoader)
        .actionHandler(NavigationDivActionHandler(navController))
        .build()
}

private fun getData(context: Context): DivData? {

    return try {
        val inputStream = context.assets.open("about_screen.json")
        val data = inputStream.bufferedReader().use { it.readText() }
        Log.d("BBB", "Loaded JSON: $data")

        val jsonObject = JSONObject(data)
        Log.d("BBB", "Parsed JSON: $jsonObject")

        val cardObject = jsonObject.getJSONObject("card")
        val environment = DivParsingEnvironment(ParsingErrorLogger.LOG)
        DivData(environment, cardObject)
    } catch (e: Exception) {
        Log.e("BBB", "Error reading file", e)
        null
    }
        /*return try {
            val inputStream = context.assets.open("about_screen.json")
            val data = inputStream.bufferedReader().use { it.readText() }
            Log.d("BBB", "Loaded JSON: $data")

            val jsonObject = JSONObject(data)
            Log.d("BBB", "Parsed JSON: $jsonObject")

            val environment = DivParsingEnvironment(ParsingErrorLogger.LOG)
            DivData(environment, jsonObject)
        } catch (e: Exception) {
            Log.e("BBB", "Error reading file", e)
            e.printStackTrace()
            null
        }*/

    /*return try {
        val inputStream = context.assets.open("about_screen.json")
        val data = inputStream.bufferedReader().use { it.readText() }
        Log.d("BBB", "Loaded JSON: $data")

        val jsonObject = JSONObject(data)
        Log.d("BBB", "Parsed JSON: $jsonObject")

        // Извлекаем объект card
        val cardObject = jsonObject.optJSONObject("card")
        if (cardObject == null) {
            Log.e("BBB", "No 'card' object found in JSON")
            return null
        }

        val logId = cardObject.optString("log_id", null)
        if (logId == null) {
            Log.e("BBB", "No 'log_id' found in 'card' object")
            return null
        }

        // Извлекаем массив состояний из объекта card
        val statesArray = cardObject.optJSONArray("states")
        if (statesArray == null || statesArray.length() == 0) {
            Log.e("BBB", "No states found in JSON")
            return null
        }

        // Получаем первый элемент из массива состояний
        val stateObject = statesArray.getJSONObject(0)
        val divObject = stateObject.optJSONObject("div")

        if (divObject == null) {
            Log.e("BBB", "No 'div' object found in the state")
            return null
        }

        Log.d("BBB", "Loaded JSON Object: $divObject")

        // Создаем DivParsingEnvironment и DivData
        val environment = DivParsingEnvironment(ParsingErrorLogger.LOG)
        DivData(environment, divObject)
    } catch (e: Exception) {
        Log.e("BBB", "Error reading file", e)
        e.printStackTrace()
        null
    }*/
}

@Preview(showBackground = true)
@Composable
fun AppInfoScreenPreview() {
    val navController = rememberNavController()
    AboutScreen(navController = navController)
}