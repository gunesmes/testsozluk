package testrisk.dictionary

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}



fun parseTextLink(text: String): String {
    val pattern = """\[(.*?)\]\((.*?)\)""".toRegex()
    var newText : String = text

    val matches = pattern.findAll(text)

    matches.forEach {
        var linkText = it.groups[1]?.value.toString()
        var link = it.groups[2]?.value.toString()

        newText = newText
            .replace("[$linkText]($link)", "<a href='$link'>$linkText</a>")
            .replace("\n", "<br>")
    }

    return newText
}

fun removeNewLines(text: String): String {
    val pattern = """(\w+)\n(\w+)""".toRegex()
    var newText : String = text

    val matches = pattern.findAll(text)

    matches.forEach {
        var pre = it.groups[1]?.value.toString()
        var post = it.groups[2]?.value.toString()

        newText = newText
            .replace("$pre\n$post", "$pre $post")
    }

    return newText
}