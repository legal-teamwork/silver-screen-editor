package org.legalteamwork.silverscreen.menu

/**
 * Контекст, необходимый для передачи в компоузы айтемов функции взаимодействия со всплывающем окном всего меню
 */
interface MenuScope {
    fun onMenuClose()
}
