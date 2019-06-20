package groovy

import spock.lang.Specification

class TestLogin extends Specification implements Testable{

    def token = dispatchLoginRpc();

}
