# BotTG

### Необходимые условия

Убедитесь, что на вашем компьютере для разработки установлены следующие предварительные компоненты:

- Java (используется в качестве основного языка для этого проекта), версия 17.0.8
- Maven (инструмент сборки)

### Токен доступа к боту Telegram

Вам нужно будет создать собственного бота в Telegram для получения токена доступа:

1. Найдите телеграмм-бота «BotFather» (имя пользователя телеграммы: @BotFather).
2. Создайте нового бота, следуя инструкциям BotFather.
3. После успешного создания вы получите токен доступа.

### API-ключи

Чтобы запустить это приложение, вам необходимо получить следующие ключи API:

- Ключ API Яндекс Погоды
- Ключ API Яндекс Расписаний

### Свойства конфигурации

В дополнение к ключам API вам потребуется создать и настроить файл `config.properties` по следующему пути: `src/main/resources/config.properties`. 

Файл должен содержать следующие пары ключ-значение:<br>

Telecentre->Railway.url = http://tosamara.ru/spravka/ostanovki/996/<br>
Railway->Telecentre.url = http://tosamara.ru/spravka/ostanovki/833/<br>

siteWeatherSamara.url = https://yandex.ru/pogoda/samara?lat=53.195876&lon=50.100199<br>
siteWeatherNovokybishevsk.url = https://yandex.ru/pogoda/?lat=53.09946823&lon=49.94777679<br>

SCHEDULE_API_KEY = 'Yandex Schedule API key'<br>
SCHEDULE_API_URL = https://api.rasp.yandex.net/v3.0/search/?<br>

WEATHER_API_URL = https://api.weather.yandex.ru/v2/informers?<br>
WEATHER_API_KEY = 'Yandex Weather API key'<br>

TOKEN_TG = 'Telegram Access Token<br>
botUsername = 'Your botname'<br>

### Установка

Загрузите или клонируйте репозиторий на свой локальный компьютер и `cd` в каталог проекта.<br>
bash git clone https://github.com/Danila1J/WeatherBotTG.git cd WeatherBotTG mvn clean install

### Использование

Чтобы взаимодействовать с ботом, просто зайдите в чат и введите `/start` или нажмите кнопку «Старт».<br>
Бот подскажет варианты. Выберите 1 из 3 категорий: «Погода», «Расписание поездов», «Маршруты».

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/f0ea00a6-0964-4be7-9f2e-64e553c0d2df" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/0e58a703-2293-437a-b642-b64308765cfa" width="250" height="500">

В категории «Погода» выберите «Самара» или «Новокуйбышевск». Бот предоставит вам текущую и прогнозируемую погоду для выбранного города.

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/bb16f624-1182-4d4b-a923-4c8e78198dbd" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/8ce58e5f-99f7-4765-bebe-74b110527ef2" width="250" height="500">

В категории «Расписание электричек» выберите станцию отправления, а затем выберите станцию назначения. Бот предоставит вам расписание поездов между выбранными станциями.

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/8c75b51d-e2d9-4690-8cb3-0edd0c98e170" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/6ddc013e-f9d7-450c-a489-166bc76443d4" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/ee407724-357f-4e7c-919c-708846c9d549" width="250" height="500">


В категории «Маршруты» выберите свой маршрут, и бот предоставит вам данные о прибытии автобусов или троллейбусов в режиме реального времени. Сообщение обновляется каждые 30 секунд в течение 10 минут.

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/d1a89184-d463-42b5-9a99-90e0fb7c089a" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/589fca23-92e0-47d7-9eca-3d3bc2db6689" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/d76b172a-b777-4cb7-af6b-0dc0aa887cf5" width="250" height="500">


### Содействие

Будe рад вашей помощи в улучшении этого проекта! Пожалуйста, не стесняйтесь создавать запрос на слияние или создавать проблему в репозитории.

### Лицензия
WeatherBotTG распространяется под лицензией [Apache 2.0](https://github.com/Danila1J/WeatherBotTG/blob/master/LICENSE).
