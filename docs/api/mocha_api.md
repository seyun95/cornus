# Mocha API Design (작성중)

## Summary
API for Mocha

## create : tdigest 생성
** Parameters **

 - name: model name
 - time_slice: time slice

** Example request **

    GET /create?name=rule1&time_slice=1h

** Example response **
    
    HTTP/1.1 200 OK
    Content-Type: application/json
	{
    	"result": "true",
    }


## add: tdigest 데이타 추가
** Parameters **

 - name: model name
 - time: yyyy-MM-dd HH:mm:SS
 - value: learning value (ex. count, sum, avg)

** Example request **

    GET /add?name=rule1&time="2018-11-13 10:50:00"&value=12

** Example response **
    
    HTTP/1.1 200 OK
    Content-Type: application/json
	{
    	"result": "true",
    }

## quantile: quantile 값 얻어오기
** Parameters **

 - name: model name
 - time: yyyy-MM-dd HH:mm:SS

## centroids: get centroid
name

## update: tdigest 데이터 보정
name
centroid
value

## delete: tdigest 삭제
name

## save: 저장하기
name
## load: 불러오기
name