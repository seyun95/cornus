## mocha 프로젝트 관련 문서를 정리한다.

 - /api
 - /help
 
### mocha 구동하기
 $ docker run -p 8080:8080 --name mocha -t seyun95/mocha

### leopard 구동하기
 $ docker run -p 4000:4000 --name leopard --link mocha -t thehead19/leopard
 