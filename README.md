# Silver Screen Editor - десктопный видеоредактор с современным UI на Jetbrains Compose

![image](https://github.com/user-attachments/assets/538d5a72-cd71-4d72-af62-70366cff84b0)

## Статус сборки, версия
1.0.0 (pre-alpha)

## Основные возможности
- Базовый режим работы с видео дорожкой
- Обрезка видеофрагментов на дорожке
- Удаление видеофрагментов на дорожке
- Наложение эффектов
- Поддержка формата MP4 (на данный момент)
- Предварительный просмотр видео
- Управление воспроизведением
- Настройки проекта (битрейт, FPS, разрешение)
- Горячие клавиши (Ctrl+Z - отмена, Ctrl+Shift+Z - вернуть,
  Del - удалить выделенные блоки видео с таймлайна, Alt+C - нарезать видео по слайдеру, 
  Shift+Колесо мыши - прокрутка таймлайна, + другие, подписаны в меню File)

## Технологии
- GUI: Jetbrains Compose for Desktop
- Backend: Kotlin

- Video Processing: JavaCV (FFmpeg, OpenCV)
- Сериализация: kotlinx.serialization
- Логирование: kotlin-logging, logback

![2024-12-20_14-30](https://github.com/user-attachments/assets/754bf070-80a9-4b4e-a1c8-e3dc3805d7b6)

## Требования для запуска
- ОС:
  - Linux (современное ядро,  например, 5.10 или выше)
  - Windows 10 и выше
- Оперативная память: 8 GB (Рекомендуется 16 GB)

- Необходимое ПО:
  - JDK: 17 или выше
  - Kotlin: 1.8.20 или выше
  - OpenCV (включен в сборку)

## Инструкция по установке и запуску

1. Склонировать репозиторий
   ```bash
   git clone https://github.com/legal-teamwork/silver-screen-editor.git
   
2. Открыть проект в IntelliJ IDEA

3. Нажать на иконку Gradle в правой панели IDEA

4. Выбрать SilverScreenEditor > Tasks > compose desktop > runDistributable

5. Соберется исполнительный файл в папке `composeApp\build\compose\binaries\main\app\org.legalteamwork.silverscreen`, который автоматически запустится

## Структура проекта
- Application: главный модуль приложения, содержит конфигурацию сборки и основной код приложения.
- composeApp: модуль Compose Multiplatform, содержит UI и логику приложения.
- src/desktopMain: код, специфичный для десктопной версии приложения.
- src/commonMain: общий код для всех платформ.

## Контакты для связи
Если у вас есть вопросы или предложения, пожалуйста, свяжитесь с нами через [GitHub Issues](https://github.com/legal-teamwork/silver-screen-editor/issues)

## Roadmap улучшений
- Два режима работы: базовый для начинающих и расширенный для профессионалов (Интуитивно понятное управление в стиле InShot и KineMaster)
- Богатые возможности для создания YouTube/TikTok-контента
- Расширенная поддержка форматов включая RAW и ProRes
- Продвинутая цветокоррекция
- Система плагинов для расширения функционала
- Расширенная работа со звуком:
  - Многодорожечное редактирование
  - Встроенные аудио эффекты
  - Профессиональные эквалайзеры
- Эффекты и графика:
  - Инструменты для создания спецэффектов
  - Работа с 3D-анимацией
- Умная автоматизация:
  - Автоматическая цветокоррекция
  - Стабилизация видео
  - AI-ассистент

## Дополнительно
- [Макет](https://www.figma.com/design/ySFG5GAiNJNX59Y3lJAube/Untitled?node-id=0-1&t=0E2U2U6dK1W6fdVZ-1)
- [Анализ конкурентов](analysis.md)
- [Task List](https://github.com/orgs/legal-teamwork/projects/3)
- [Retrospective](https://docs.google.com/spreadsheets/d/1xqDXDvht4POZUiHmGrPIutCRE7hXpFMqWUFsSu-6_8k/edit?usp=sharing)
- [Документация по Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
