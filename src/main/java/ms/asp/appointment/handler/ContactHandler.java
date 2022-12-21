package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;

import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.model.ContactModel;
import ms.asp.appointment.service.ContactService;

@Component
public class ContactHandler extends AbstractHandler<Contact, Long, ContactModel, ContactService> {

    public ContactHandler(ContactService service) {
	super(service, new ContactModel());
    }
}
