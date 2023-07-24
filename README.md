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

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/871ee306-df76-4372-a754-e145faf7a42e" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/3c29fab2-a910-459b-a861-216ff9866dd0" width="250" height="500">

В категории «Погода» выберите «Самара» или «Новокуйбышевск». Бот предоставит вам текущую и прогнозируемую погоду для выбранного города.

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/0c17375c-056b-400c-9c8b-48df88788424" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/9ea03597-3d27-4568-9420-390841619044" width="250" height="500">

В категории «Расписание электричек» выберите станцию отправления, а затем выберите станцию назначения. Бот предоставит вам расписание поездов между выбранными станциями.

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/ac4746b0-b2cc-49ef-9319-5cb46145007b" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/c964d7ea-8461-494b-94a8-e05e7f50b0c7" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/c38909e1-2138-4ed3-9472-17554d41438e" width="250" height="500">

В категории «Маршруты» выберите свой маршрут, и бот предоставит вам данные о прибытии автобусов или троллейбусов в режиме реального времени. Сообщение обновляется каждые 30 секунд в течение 10 минут.

<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/eb81508f-78ca-40c4-ba57-70f70e5ac123" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/6b552f93-0351-44e8-8d16-1ce345244b30" width="250" height="500">
<img src="https://github.com/Danila1J/WeatherBotTG/assets/42370968/606aab9d-95bc-45fe-a32c-d62518996e7d" width="250" height="500">

### Содействие

Будe рад вашей помощи в улучшении этого проекта! Пожалуйста, не стесняйтесь создавать запрос на слияние или создавать проблему в репозитории.

### Лицензия
WeatherBotTG распространяется под лицензией [Apache 2.0](https://github.com/Danila1J/WeatherBotTG/blob/master/LICENSE).
