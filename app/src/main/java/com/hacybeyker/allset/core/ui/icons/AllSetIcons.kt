package com.hacybeyker.allset.core.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

/** Icons missing from material-icons-core, built in code with the same Material paths. */
object AllSetIcons {

    val Remove: ImageVector by lazy {
        materialIcon(name = "Filled.Remove") {
            materialPath {
                moveTo(19f, 13f)
                horizontalLineTo(5f)
                verticalLineToRelative(-2f)
                horizontalLineToRelative(14f)
                verticalLineToRelative(2f)
                close()
            }
        }
    }
}
