package com.banana.FirstRestApp.controllers;

import com.banana.FirstRestApp.dto.PersonDTO;
import com.banana.FirstRestApp.models.Person;
import com.banana.FirstRestApp.services.PeopleService;
import com.banana.FirstRestApp.util.PersonErrorResponse;
import com.banana.FirstRestApp.util.PersonNotCreatedException;
import com.banana.FirstRestApp.util.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper mapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper mapper) {
        this.peopleService = peopleService;
        this.mapper = mapper;
    }

    @GetMapping()
    public List<PersonDTO> getPeople() {
//        return peopleService.findAll();
        return peopleService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
        // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
      // Статус - 200
//        return peopleService.findOne(id);
        return convertToPersonDTO(peopleService.findOne(id));
        // Jackson конвертирует в JSON
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        peopleService.save(convertToPerson(personDTO));

        // отправляем HTTP ответ с пустым телом и статусом 200
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id wasn't found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        /* ResponseEntity - обёртка над нашим объектом response. Возвращаем
         сообщение, время и статус 404 */
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    private Person convertToPerson(PersonDTO personDTO) {
//        ModelMapper modelMapper = new ModelMapper();
//        // ModelMapper берёт на себя связь DTO-Model. Код ручной привязки комментируем.
            // поручим Spring создавать bean modelMapper в SpringApplication.run
//        Person person = new Person();
//
//        person.setName(personDTO.getName());
//        person.setAge(person.getAge());
//        person.setEmail(person.getEmail());

        return mapper.map(personDTO, Person.class);
    }
    private PersonDTO convertToPersonDTO(Person person) {
        return mapper.map(person, PersonDTO.class);
    }
}
