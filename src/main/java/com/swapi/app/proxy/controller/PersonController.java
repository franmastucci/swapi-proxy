package  com.swapi.app.proxy.controller;
import com.swapi.app.proxy.dto.PersonInfoResponse;

import com.swapi.app.proxy.service.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@Tag(name = "Person", description = "Endpoints for retrieving person information")
public class PersonController {

    @Autowired
    private PersonService personService;


    @GetMapping(path = "/person-info")
    @Operation(summary = "Get information about a person",
            description = "Retrieves detailed information about a person from the Star Wars universe.")
    public PersonInfoResponse getPersonInfo(
            @Parameter(description = "Name of the person", required = true)
            @PathParam(value = "name") String name) {
        return personService.getPersonInfo(name);
    }

}
