package mx.edu.itm.link.dadm_u3p4.utils

import android.content.Context
import android.widget.Toast

class MyUtils {

    companion object {

        fun String.toast(context: Context) {
            Toast.makeText(context, this, Toast.LENGTH_LONG).show()
        }

    }

}