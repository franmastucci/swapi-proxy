package  com.swapi.app.proxy.controller;
import com.swapi.app.proxy.dto.PersonInfoResponse;
import com.swapi.app.proxy.service.SwapiProxyService;
import com.swapi.app.proxy.service.PersonService;
import jakarta.websocket.server.PathParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/swapi-proxy")
public class PersonController {

    @Autowired
    private PersonService personService;


    @GetMapping(path = "/person-info")
    public PersonInfoResponse getPersonInfo(@PathParam(value = "name") String name) {
        return personService.getPersonInfo(name);
    }

}
