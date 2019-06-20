package groovy

class TestGateWay extends TestLogin implements Testable{

    def getEmployee(){
        when:
        def response = postBody("/", [serviceName: "rosterService", methodName: "selectAllByPage"]
                , ["GW_APP": "RPC", token: token])
        then:
        response["code"] == 0
    }

}
