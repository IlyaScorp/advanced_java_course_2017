## Как разработать и запустить чат-бота для групп в ok.ru

### Архитектура и высокоуровневые компоненты

Чат-бот - это web приложение необходимое для того, чтобы автоматическим образом реагировать на сообщения, 
отправляемые участниками в чате. Например, оно может отвечать на простейшие вопросы пользователя к группе в ok.ru.
Такое приложение является web-сервером в терминах архитектуры
клиент-сервер для сервиса ok.ru, отсылающего нотификации по https протоколу приложению чат-бота.
Правила отправки таких сообщений описаны 
[на страничке описания BOT API ok.ru](https://apiok.ru/dev/graph_api/methods/graph.user/graph.user.subscribe/post).
Полностью воможности бот API описаны [здесь](https://apiok.ru/dev/graph_api/bot_api). 

Бот, реализованный на java и размещённый в репозитории   
[advanced_java_course_2017](https://github.com/TimurTechnopolis/advanced_java_course_2017/tree/master/season1/episode6/boatswain-ok-bot),
принимает web запросы от сервиса нотификаций ok.ru, реализуя простейший HTTP сервер с помощью класса, входящего в **jdk**,
`com.sun.net.httpserver.HttpServer`. С помощью библиотеки 
[Apache HTTP Client](https://hc.apache.org/httpcomponents-client-4.5.x/index.html) версии 4.5.3 запрограмирован http 
клиент для BOT API.

Приложение бота запускается внутри [docker](https://www.docker.com/what-docker) контейнера, поэтому для построения 
конечного артефакта (файла для размещения на сервере, где будет работать бот) используется 
[gradle плагин для докера](https://github.com/bmuschko/gradle-docker-plugin).
В качестве базового для докер образа приложения используется [alpine+jdk9](https://hub.docker.com/r/jfisbein/alpine-oracle-jdk9/),
состоящий из [alpine linux](https://alpinelinux.org/about/) версии 3.6 и oracle jdk 9.0.1.

### Без чего нельзя собрать рабочее приложение
Здесь описаны **prerequisites**, без чего приложение нельзя будет собрать, а также без чего оно не будет работать.
* Компьютер должен быть подключен к сети интернет для скачивания зависимостей (java-библиотек и базового образа)
* На компьютере должен быть установлен **oracle jdk 1.8** 
[с сайта oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) или его аналог.
**Важно**, чтобы сборка приложения происходила именно на с помощью **jdk 1.8**, поскольку **gradle** работает нестабильно 
на **jdk 9**. Чтобы убедиться, что при запуске **gradle** из командной строки используется правильная версия java, нужно 
выполнить команду в консоли и проверить, что переменная указывает на нужную версию __jdk__.
```
echo $JAVA_HOME
```
или для _Windows_
```
echo %JAVA_HOME%
```
* На компьютере установлен **gradle** версии 4.3. Его можно установить с сайта https://gradle.org/, следуя инструкциям, 
либо установив утилиту [sdkman](http://sdkman.io/install.html), которая позволит устанавливать **gradle** любой версии 
и переключаться между ними.
* На компьютере должен быть установлен **docker CE** c сайта https://www.docker.com/community-edition или используя 
любимый пакетный менеджер.
* Поскольку бот разработан для чата пользователя с группой в ok.ru, то необходимо в настройках группы получить AUTH TOKEN 
для запросов к BOT API. Токен нужно поместить в файл `application.properties`. Процедура получения токена описана 
[здесь](https://apiok.ru/dev/graph_api/bot_api)

### Инструкция для сборки и запуска образа приложения
#####1. Построение образа
Для того, чтобы собрать docker образ приложения, необходимо в командной строке, находясь в корне проекта приложения, 
выполнить команду
```
gradle clean buildDockerImage 
```
При успешном выполенении команды в самом конце в поток вывода будет записано
```
> Task :dockerBuildImage
Created image with ID 'e7a3961fe99d'.
BUILD SUCCESSFUL in 7s
```
`e7a3961fe99d` - это идентификатор докер-образа приложения. Выполнив команду
```
docker images 
```
можно будет увидеть построенный образ в списке досткупных образов в локальном репозитории.
```
REPOSITORY                                       TAG        IMAGE ID       CREATED        SIZE
edu.tecnopolis.advjava/boatswain-ok-bot          0.0.1      e7a3961fe99d   33 hours ago   506MB
```
в данном случае `edu.tecnopolis.advjava/boatswain-ok-bot` - полное (fully qualified) название приложения, `0.0.1` - версия 
приложения.
Если приложение запускается локально, то загружать в докер образ в удалённый docker репозиторий смысла нет и шаги __2__ 
и __3__ можно не выполнять.
#####2. Загрузка образа в удалённый репозиторий
Напротив, если приложение планируется разворачивать на удалённом сервере (например, в облачной инфраструктуре), то 
необходимо выложить докер образ в репозиторий, доступный с удалённого сервера для развёртывания приложения (к примеру, в 
[docker hub](https://hub.docker.com/)).
Например, для того, чтобы загрузить образ в публичный репозиторий `http://dockerregistry.jtechnopolis.pw:5443` необходимо
из текущего образа сформировать новый командой `docker tag`, добавив к имени образа `dockerregistry.jtechnopolis.pw:5443`
```
docker tag e7a3961fe99d dockerregistry.jtechnopolis.pw:5443/edu.tecnopolis.advjava/boatswain-ok-bot:0.0.1
``` 
После этого сформируется новый образ с тем же идентификатором, что можно увидеть выполнив `docker images` 
```
REPOSITORY                                                                    TAG     IMAGE ID        CREATED        SIZE
dockerregistry.jtechnopolis.pw:5443/edu.tecnopolis.advjava/boatswain-ok-bot   0.0.1   e7a3961fe99d    33 hours ago   506MB
edu.tecnopolis.advjava/boatswain-ok-bot                                       0.0.1   e7a3961fe99d    33 hours ago   506MB
```
Вновь сформированный образ можно загрузить в репозиторий командой `docker push`
```
docker push dockerregistry.jtechnopolis.pw:5443/edu.tecnopolis.advjava/boatswain-ok-bot
``` 
#####3. Скачивание образа из удалённого репозитория
Скачать образ из удалённого репозитория можно комнандой `docker pull`
```
docker pull dockerregistry.jtechnopolis.pw:5443/edu.tecnopolis.advjava/boatswain-ok-bot
```
После этого в локальном репозитории появится соответвующий образ, что можно проверит командой `docker images`
#####4. Запуск приложения в докер контейнере
Если приложение запускается локально (на том же сервере, где производилась сборка образа), то шаги **2** и **3** можно 
опустить. **Важно**, чтобы сервер, где работает приложение был доступен в интернете напрямую по статическому IP адресу, 
доменному имени или через [NAT](https://ru.wikipedia.org/wiki/NAT), чтобы сервис нотификаций ok.ru мог отослать http 
запрос на этот сервер.

Для формирования докер контейнера из образа и запуска приложения в контейнер необходимо выполнить команду
```
doсker run -d -p 8080:8080 --name boatswain "dockerregistry.jtechnopolis.pw:5443/edu.tecnopolis.advjava/boatswain-ok-bot:0.0.1"
```
где ключ `-d` означает, что приложение должно быть запущено в фоновом режиме, ключ `--name` используется для создания 
имени докер контейнера, а ключ `-p 8080:8080` значит, что порт 8080 должен быть проброшен из контейнера в операционную 
систему, на которой работает docker engine, и быть доступным также на порте 8080. Именно на этот порт будут поступать 
запросы из сервиса нотификаций ok.ru.
После запуска команды в случае успешного старта контейнера командой `docker ps` можно вывести все запущенные контейнеры
```
CONTAINER ID        IMAGE                                                                                  COMMAND                  CREATED       STATUS       PORTS                      NAMES
c5930b8e27e6        dockerregistry.jtechnopolis.pw:5443/edu.tecnopolis.advjava/boatswain-ok-bot:0.0.1      "/boatswain-ok-bot-0…"   2 days ago    Up 2 days    0.0.0.0:8080->8080/tcp     boatswain
```
Как только контейнер сформирован, его можно запускать или останавливать командами 
```
docker start c5930b8e27e6
``` 
```
docker stop c5930b8e27e6
``` 
Кроме этого можно выполнять команды, используя имя контейнера 
```
docker start boatswain
``` 
```
docker stop boatswain
```
Логи приложения (контейнера) можно смотреть командой
```
docker logs boatswain
```

#### Рабочие версии ботов
Рабочие версии ботов можно увидеть в группе [https://ok.ru/touchbank](https://ok.ru/touchbank).
Исходный код бота 'боцман' расположен https://github.com/TimurTechnopolis/advanced_java_course_2017/tree/master/season1/episode6/boatswain-ok-bot 
и 'валютчик' расположен https://github.com/TimurTechnopolis/advanced_java_course_2017/tree/currency-bot/season1/episode6/boatswain-ok-bot

 
    


