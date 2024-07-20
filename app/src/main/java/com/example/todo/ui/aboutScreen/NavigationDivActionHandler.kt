package com.example.todo.ui.aboutScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class NavigationDivActionHandler(private val navController: NavHostController) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url = action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)


        return when (url.scheme) {
            SCHEME_NAVIGATION -> handleNavigationAction(url, view.view.context)
            SCHEME_MAILTO -> handleEmailAction(url, view.view.context)
            else -> super.handleAction(action, view, resolver)
        }
    }

    private fun handleNavigationAction(action: Uri, context: Context): Boolean {
        return when (action.host) {
            "navigate" -> {
                navController.popBackStack()
                true
            }
            else -> false
        }
    }

    private fun handleEmailAction(action: Uri, context: Context): Boolean {
        return if (action.scheme == SCHEME_MAILTO) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = action
            }
            context.startActivity(intent)
            true
        } else {
            false
        }
    }

    companion object {
        const val SCHEME_NAVIGATION = "navigation-action"
        const val SCHEME_MAILTO = "mailto"
    }
}