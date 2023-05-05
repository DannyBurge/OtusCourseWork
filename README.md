## Описание

Приложение сервис по доставке еды OTUS Kitchen. 

Полноценно функционирует при работа [сервера](https://github.com/DannyBurge/OTUS-Kitchen-server)

В файле [source/network/src/main/java/com/otuscourcework/network/NetworkRepository.kt](https://github.com/DannyBurge/OtusCourseWork/blob/master/source/network/src/main/java/com/otuscourcework/network/NetworkRepository.kt)
заменить ***BASE_URL*** на необходимый.

## Экраны

### 1. Авторизация: 

<img src="https://i.imgur.com/LvPiFA3.png" align="right" width="200px"/> 

* Возможно задать числовой пароль 
* Вход по биометрии     
* Отключается в настройках

<br clear="right"/>

---

### 2. Домашняя страница:

<img src="https://i.imgur.com/Usao6Av.png" align="right" width="200px"/> 
<img src="https://i.imgur.com/shSmqZq.png" align="right" width="200px"/> 

* Отображение баланса
* Пополнение баланса путем ввода промокода 
* Фильтрации по категориям
* Фильтрация по нахождению в избранном
* Позицию можно добавить в избранное

<br clear="right"/>

---

### 3. Подробности о позиции:

<img src="https://i.imgur.com/A39eIzL.png" align="right" width="200px"/> 
<img src="https://i.imgur.com/GeBILcs.png" align="right" width="200px"/> 

* Расширяемое Bottom Sheet
* Дополнительная информация о позиции
* Добавление в корзину с выбором порции
* Позицию можно добавить в избранное

<br clear="right"/>

---

### 4. Корзина:

<img src="https://i.imgur.com/bPtGepc.png" align="right" width="200px"/> 
<img src="https://i.imgur.com/DfQbe8O.png" align="right" width="200px"/> 

* Списать или получить бонусы
* Создать заказ
* Очистить корзину

<br clear="right"/>

###

<img src="https://i.imgur.com/0q9xwis.png" align="right" width="200px"/> 
<img src="https://i.imgur.com/MlLRdbo.png" align="right" width="200px"/> 

* Выбрать текущий адрес доставки или добавить новый

<br clear="right"/>

---

### 5. Заказы:

<img src="https://i.imgur.com/vR9GeqU.png" align="right" width="200px"/> 
<img src="https://i.imgur.com/pbTWUcD.png" align="right" width="200px"/> 

* Заказы разворачиваются и сворачиваются по нажатию
* Активные заказы разворачиваются автоматически

<br clear="right"/>

---

### 6. Уведомления:

<img src="https://i.imgur.com/PYvycA1.png" align="right" width="200px"/> 

* При нажатии на уведомление выполняется переход на нужный заказ

<br clear="right"/>

---

### 7. Настройки:

<img src="https://i.imgur.com/TozYQaf.png" align="right" width="200px"/> 

* Включение защищенного входа
* Задание числового пароля при активации
* Включение или отключение биометрии

<br clear="right"/>

---

## Стек

* Kotlin
* Single Activity
* MVVM + State
* Coroutine + Flow
* Cicerone
* Hilt
* Retrofit + Moshi
* Многомодульность
* Gradle Version Catalog
* Detekt
* JUnit + Mockito
* Espresso
