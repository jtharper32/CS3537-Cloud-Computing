package src.main.java.edu.appstate.cs.cloud.blackjack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/persons")
public class PersonEndpoint {
    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping(value = "/init")
    public boolean initPersons() {
        Person person = new Person.Builder()
                .withName("DefaultPlayer")
                .withBalance(100.0)
                .build();
        personService.createPerson(person);
        return true;
    }

    @GetMapping(value = "/{name}")
    public Person getPersonByName(@PathVariable String name) {
        return personService.getPersonByName(name);
    }

    @PostMapping
    public void createPerson(@RequestBody Person person) {
        personService.createPerson(person);
    }

    @PutMapping(value = "/{name}")
    public void updatePerson(@PathVariable String name, @RequestBody Person person) {
        personService.updatePerson(person);
    }
}