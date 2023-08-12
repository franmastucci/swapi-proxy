package  com.swapi.app.proxy.controller;
import com.swapi.app.proxy.dto.proxy.PersonInfoResponse;
import com.swapi.app.proxy.service.ContextService;
import com.swapi.app.proxy.service.SwapiProxyService;
import jakarta.websocket.server.PathParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/swapi-proxy")
public class SwapiProxyController {

    @Autowired
    private SwapiProxyService swapiProxyService;

    @Autowired
    private ContextService contextService;

    @GetMapping(path = "/init")
    public ResponseEntity<Void> filmsInitialization() {
        contextService.getFilms();
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "/person-info")
    public PersonInfoResponse getPersonInfo(@PathParam(value = "name") String name) {
        return swapiProxyService.getPersonInfo(name);
    }

}
