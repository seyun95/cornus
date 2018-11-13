# Mocha API Design (작성중)

## Summary
API 사용 방법 정리

## create : tdigest 생성
** Parameters **
 - name: 모델 이름
 - time_slice: time slice

**Example request**:

    GET /create?name=rule1&time_slice=1h

**Example response**:
    
    HTTP/1.1 200 OK
    Content-Type: application/json
	{
    	"result": "true",
    }

## add: tdigest 데이타 추가
name
time
value

## quantile: quantile 값 얻어오기
name
value

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