package name.alexkosarev.sandbox.repositories;

import name.alexkosarev.sandbox.entities.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
}
