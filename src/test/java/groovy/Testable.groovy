package groovy

import com.alibaba.fastjson.JSON
import jodd.http.HttpRequest

trait Testable {

    def ip = "http://192.168.1.27"
    def baseUrl = ip + ":8090/"
    def fileUrl = ip + ":8084/"
    def activitiUrl = ip + ":8083/"


    def get(url, Map map) {
        HttpRequest.get(baseUrl + url).query(map).send().bodyText()
    }

    def getValide(url, Map map) {
        HttpRequest.get(fileUrl + url).query(map).send().bodyText()
    }


    def getAsMap(url, Map map) {
        asMap(HttpRequest.get(baseUrl + url).query(map).send().bodyText())
    }


    def postAsMap(path, Map map) {
        def bodytest = HttpRequest.post(baseUrl + path).form(map).send().bodyText()
        asMap(bodytest)
    }


    def postBody(path, Map map, Map header) {

        def body = HttpRequest.post(baseUrl + path).body(JSON.toJSONString(map))

        header.each { k, v -> body.header(k, v) }

        def bodytest = body.send().bodyText()

        asMap(bodytest)
    }


    def postFile(path, File file, String name, String value) {
        def res = HttpRequest.post(baseUrl + path).form("file", file).header(name, value).send().bodyText()
        asMap(res)
    }


    def postAsList(url, Map map) {
        JSON.parseObject(HttpRequest.post(baseUrl + url).form(map).send().bodyText(), List.class)
    }


    def asMap(def json) {
        JSON.parseObject(json, Map.class)
    }


    /**
     * login
     */

//    def getToken() {
//        def response = postBody("/", [serviceName: "authService", methodName: "getToken"], ["GW_APP": "RPC"])
//        response["access_token"]
//    }
//
//
//    def getValidCode(token) {
//
//        def path = "getValidCode/create.do"
//        getValide(path, [token: token])
//
//        def response = postBody('/', [serviceName: "authService", methodName: "getValidCode", parameters: [token: token]], [:])
//        response["data"]
//    }


//    def dispatchRpc() {
//
//        when:
//
//        def response = postBody("/", [serviceName: "authService", methodName: "getToken"], ["GW_APP": "RPC"])
//        then:
//
//        response["code"] == "0"
//
//        response["msg"] == "access_token获取成功"
//
//    }


    def dispatchLoginRpc() {

        def tokenResponse = postBody("/", [serviceName: "authService", methodName: "getToken"], ["GW_APP": "RPC"])

        def token = tokenResponse["access_token"]

        def path = "getValidCode/create.do"

        getValide(path, [token: token])

        def validCodeResponse = postBody('/', [serviceName: "authService", methodName: "getValidCode", parameters: [token: token]], [:])

        def validCode = validCodeResponse["data"]
//        when:
        def response = postBody("/", [serviceName: "accountService", methodName: "login",
                                      parameters : [username: "aaaa ", password: "321654", validCode: validCode, deviceType: "android"]], ["GW_APP": "RPC", token: token])
//        then:
//        resonse["code"] == 0;
        if(response["code"] == 0){
            return token;
        }else{
            System.err.println("登录失败！")
        }
    }




    def setBaseUrl(url) {
        baseUrl = url
    }

    def getBaseUrl(){
        baseUrl
    }

    def setFileUrl(url) {
        fileUrl = url
    }

    def getFileUrl(){
        fileUrl
    }

    def setActivitiUrl(url) {
        activitiUrl = url
    }

    def getActivitiUrl(){
        activitiUrl
    }


    def setIp(ipaddress) {
        ip = ipaddress
    }

    def getIp(){
        ip
    }
}