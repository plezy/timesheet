package lu.plezy.timesheet.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lu.plezy.timesheet.entities.Customer;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.repository.CustomerRepository;
import lu.plezy.timesheet.repository.UsersRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    private int defaultPageSize = 10;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UsersRepository usersRepository;

    /**
     * returns first page of a paged list of all active customers. Page sise is
     * equals to the deafault page size.
     * 
     * @return first customer page, with default size
     */
    @GetMapping(value = "/list")
    @PreAuthorize("isAuthenticated()")
    public Page<Customer> getCustomers() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("name"));
        return customerRepository.findAllActive(p);
    }

    /**
     * returns first page of a paged list of all active customers.
     * 
     * @param page page to fetch (0 based)
     * 
     * @return the page of default size
     */
    @GetMapping(value = "/list/{page}")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public Page<Customer> getCustomers(@PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, defaultPageSize, Sort.by("name"));
        return customerRepository.findAllActive(p);
    }

    /**
     * returns a page of a paged list of all active users.
     * 
     * @param page page to fetch (0 based)
     * @param size page's size
     * 
     * @return the requested page of the given size
     */
    @GetMapping(value = "/list/{page}/{size}")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public Page<Customer> getCustomers(@PathVariable("page") int page, @PathVariable("size") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("name"));
        return customerRepository.findAllActive(p);
    }

    /**
     * returns first page of a paged list of all active customers. Page sise is
     * equals to the deafault page size.
     * 
     * @return first customer page, with default size
     */
    @GetMapping(value = "/list/deleted")
    @PreAuthorize("isAuthenticated()")
    public Page<Customer> getAllCustomers() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("name"));
        return customerRepository.findAllNotArchived(p);
    }

    /**
     * returns first page of a paged list of all active customers.
     * 
     * @param page page to fetch (0 based)
     * 
     * @return the page of default size
     */
    @GetMapping(value = "/list/deleted/{page}")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public Page<Customer> getAllCustomers(@PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, defaultPageSize, Sort.by("name"));
        return customerRepository.findAllNotArchived(p);
    }

    /**
     * returns a page of a paged list of all active users.
     * 
     * @param page page to fetch (0 based)
     * @param size page's size
     * 
     * @return the requested page of the given size
     */
    @GetMapping(value = "/list/deleted/{page}/{size}")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public Page<Customer> getAllCustomers(@PathVariable("page") int page, @PathVariable("size") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("name"));
        return customerRepository.findAllNotArchived(p);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public Customer addCustomer(@Valid @RequestBody Customer newCustomer, Authentication authentication) {
        Optional<User> loggedUser = usersRepository.findByUsername(authentication.getName());
        if (!loggedUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authenticated user not Found");
        }
        newCustomer.setCreatedOn(new Date());
        newCustomer.setCreatedBy(loggedUser.get());
        newCustomer.setUpdatedOn(newCustomer.getCreatedOn());
        newCustomer.setUpdatedBy(newCustomer.getCreatedBy());
        Customer addedCustomer = customerRepository.save(newCustomer);
        return addedCustomer;
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public Customer updateUser(@PathVariable("id") long id, @Valid @RequestBody Customer updatedCustomer, Authentication authentication) {
        Optional<User> loggedUser = usersRepository.findByUsername(authentication.getName());
        if (!loggedUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authenticated user not Found");
        }
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()) {
            updatedCustomer.setCreatedOn(result.get().getCreatedOn());
            updatedCustomer.setCreatedBy(result.get().getCreatedBy());
            if (loggedUser.isPresent()) {
                updatedCustomer.setUpdatedBy(loggedUser.get());
            } else {
                updatedCustomer.setUpdatedBy(updatedCustomer.getCreatedBy());
            }
            updatedCustomer.setUpdatedOn(new Date());
            customerRepository.save(updatedCustomer);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found");
        }
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Delete a Customer. The customer is first logically deleted and then an attempt to
     * delete the customer physically is performed.
     * 
     * @param id Customer's ID
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public void deleteCustomer(@PathVariable("id") long id) {
        Optional<Customer> result = customerRepository.findById(id);

        if (result.isPresent()) {
            Customer customer = result.get();

            deleteCustomerLogically(customer);

            //deleteCustomerPhysically(customer.getId());
        }
    }

    /**
     * Deletes a customer logically.
     * 
     * @param user Customer entity
     */
    @Transactional
    private void deleteCustomerLogically(Customer customer) {
        customer.setDeleted(true);
        // update user for logical deletion
        log.info("Perform customer logical deletion");
        customerRepository.save(customer);
    }

    /**
     * Attempt to delete physically a customer with ID. If an exception occurs it is
     * trapped in order to avoir returning an error.
     * 
     * @param id Customer's ID
     */
    @Transactional
    private void deleteCustomerPhysically(Long id) {
        // attempt physical deletion is possible ...
        log.info("Attempt customer physical deletion");
        try {
            customerRepository.deleteById(id);
        } catch (Exception ex) {
            log.info("Customer can not be deleted physically ...");
        }
    }

    /**
     * Deletes customer's in list.
     * 
     * @param custumerIds customer's IDs list
     */
    @DeleteMapping(path = "/list/{ids}")
    @PreAuthorize("hasAuthority('MANAGE_CUSTOMERS')")
    public void deleteCustomers(@PathVariable("ids") String custumerIds) {
        log.info("Deleting custumer's Ids : " + custumerIds);
        List<Long> idsList = Arrays.asList(custumerIds.split(",")).stream().map(Long::parseLong)
                .collect(Collectors.toList());
        log.info("list of ids : " + idsList);

        for (Long id : idsList) {
            Optional<Customer> result = customerRepository.findById(id);
            if (result.isPresent()) {
                Customer customer = result.get();

                deleteCustomerLogically(customer);

                //deleteCustomerPhysically(customer.getId());
            }
        }

    }

    /**
     * Undelete (logically customer) user.
     * 
     * @param id
     */
    @PutMapping(value = "/undelete/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void undeleteCustomer(@PathVariable("id") long id) {
        Optional<Customer> result = customerRepository.findById(id);

        if (result.isPresent()) {
            Customer customer = result.get();
            customer.setDeleted(false);

            // update user to reset logical deletion
            log.info("Undelete customer");
            customerRepository.save(customer);
        }
    }

}