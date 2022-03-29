

A simple jee webapp using the [HttpSessionReplacer](https://github.com/AmadeusITGroup/HttpSessionReplacer)


## Assemble

```
mvn clean package
```


## Run 

```
docker-compose up
```


## Test

Set your name in session

```
curl -X POST http://localhost:8080/webapp-jee7-1.0-SNAPSHOT/rest/greetings/meet/Simone -v
```

Get personal greetings

```
curl -H 'Cookie: JSESSIONID=XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl' http://localhost:8080/webapp-jee7-1.0-SNAPSHOT/rest/greetings/
```


You should verify that session is now stored on redis.

```
redis-monitor_1  | 1648547220.003343 [0 172.21.0.3:42514] "ZADD" "com.amadeus.session:all-sessions-set:APPNAME" "1.648547250001E12" "XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl:6d262646d564"
redis-monitor_1  | 1648547220.004128 [0 172.21.0.3:42514] "EXPIRE" "com.amadeus.session::APPNAME:{XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl}" "330"
webapp_1         | 29-Mar-2022 09:47:00.004 INFO [http-nio-8080-exec-2] com.amadeus.session.SessionManager.getSession Session with sessionId: 'IdAndSource [id=XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl; cookie=true]' but it was not in repository!
redis-monitor_1  | 1648547220.006180 [0 172.21.0.3:42514] "HMGET" "com.amadeus.session::APPNAME:{XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl}" "name"
redis-monitor_1  | 1648547220.042912 [0 172.21.0.3:42514] "MULTI"
redis-monitor_1  | 1648547220.042995 [0 172.21.0.3:42514] "HMSET" "com.amadeus.session::APPNAME:{XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl}" "#:owner" "6d262646d564" "#:lastAccessed" "\x00\x00\x01\x7f\xd5\x12J!" "#:maxInactiveInterval" "\x00\x00\x00\x1e"
redis-monitor_1  | 1648547220.043060 [0 172.21.0.3:42514] "EXEC"
redis-monitor_1  | 1648547220.043824 [0 172.21.0.3:42514] "ZADD" "com.amadeus.session:all-sessions-set:APPNAME" "1.648547250001E12" "XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl:6d262646d564"
redis-monitor_1  | 1648547220.044580 [0 172.21.0.3:42514] "EXPIRE" "com.amadeus.session::APPNAME:{XzWp4p8MTKMdGq9o9RUFx3EwcMrdCaZo5rYtzqBl}" "330"
redis-monitor_1  | 1648547220.166382 [0 172.21.0.3:42508] "ZRANGEBYSCORE" "com.amadeus.session:all-sessions-set:APPNAME" "1.648547040165E12" "1.648547220165E12"
redis-monitor_1  | 1648547220.166862 [0 172.21.0.3:42508] "ZRANGEBYSCORE" "com.amadeus.session:all-sessions-set:APPNAME" "0.0" "1.648547040165E12"
```