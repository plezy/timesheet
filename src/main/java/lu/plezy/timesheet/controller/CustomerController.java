package lu.plezy.timesheet.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lu.plezy.timesheet.entities.Customer;
import lu.plezy.timesheet.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    private int defaultPageSize = 10;

    @Autowired
    CustomerRepository customerRepository;

    /**
     * returns first page of a paged list of all active customers. Page sise is
     * equals to the deafault page size.
     * 
     * @return first customer page, with default size
     */
    @GetMapping(value = "/list")
    @PreAuthorize("isAuthenticated()")
    public Page<Customer> getUsers() {
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

}