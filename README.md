Тестовое задание на позицию java-разработчик
Это окнсольное приложение которое иммитирует работу предприятия
Для использования в папку tables загрузить фалйы с раширением .xlsx( если приложение будет собираться через maven то рядом с .jar создать данную папку)
Обрабатывает входные данные по шаблону, в константах можно задать на какой строке будут начинаться данные

После добавления файлов для обработки, можно запускать приложение,выбирать нужный файл, и программа выдаст желаемый результат

Коротко как работает:
В задании было прописано обработать детали за минимальное время(оптимальное). Это можно достичь либо сокращение времени обработки детали(путь от начала до конца), но сделать мы этого не можем по заданию,следовательно, достичь минималдьного времени можно только, правильным распределением сотрудников.
Тут я руководствовался логикой,что оптимально будет тогда,когда рабочие не будут стоять без дела,если в каком-то цеху есть деталь в буффере.
Программа изначально распределяет сотрудников в начале, и в процессе работы перекидывает с отдного цеха на другой
