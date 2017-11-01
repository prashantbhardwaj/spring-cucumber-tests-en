package name.alexkosarev.sandbox.web.controllers;

import lombok.RequiredArgsConstructor;
import name.alexkosarev.sandbox.entities.Contact;
import name.alexkosarev.sandbox.repositories.ContactRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("contacts")
@RequiredArgsConstructor
public class ContactsController {

    private final ContactRepository repository;

    @GetMapping
    public ResponseEntity<List<Contact>> findAll() {
        return ResponseEntity.ok((List<Contact>) repository.findAll());
    }
}
