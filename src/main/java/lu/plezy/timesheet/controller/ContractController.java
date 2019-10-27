package lu.plezy.timesheet.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lu.plezy.timesheet.entities.Contract;
import lu.plezy.timesheet.entities.ContractTypeEnum;
import lu.plezy.timesheet.entities.messages.ContractDto;
import lu.plezy.timesheet.entities.messages.ContractType;
import lu.plezy.timesheet.i18n.StaticText;
import lu.plezy.timesheet.repository.ContractRepository;
import lu.plezy.timesheet.repository.UsersRepository;

@RestController
@RequestMapping(path = "/contract")
public class ContractController {

    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    private int defaultPageSize = 10;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    UsersRepository usersRepository;

    /**
     * Returns complete list of available roles in the application.
     * 
     * @return List of all available roles in the application
     */
    @GetMapping(value = "/types")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public List<ContractType> getAllContractTypes() {
        List<ContractType> response = new ArrayList<>();

        for (ContractTypeEnum contractTypeEnum : EnumSet.allOf(ContractTypeEnum.class)) {
            ContractType entry = new ContractType(contractTypeEnum.name());
            entry.setContractTypeDescription(StaticText.getInstance().getText(contractTypeEnum.toString()));
            response.add(entry);
        }
        return response;
    }

    
    /**
     * returns first page of a paged list of all active contracts. Page sise is
     * equals to the default page size.
     * 
     * @return first page of contract's list, with default size
     */
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getContracts() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("orderDate"));
        //return contractRepository.findAllActive(p);
        Page<Contract> pageContent = contractRepository.findAllActive(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns first page of a paged list of all active contracts.
     * 
     * @param page page to fetch (0 based)
     * 
     * @return the contract's list page of default size
     */
    @GetMapping(value = "/list/{page}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getContracts(@PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, defaultPageSize, Sort.by("orderDate"));
        //return contractRepository.findAllActive(p);
        Page<Contract> pageContent = contractRepository.findAllActive(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns a page of a paged list of all active contracts.
     * 
     * @param page page to fetch (0 based)
     * @param size page's size
     * 
     * @return the requested page of the given size
     */
    @GetMapping(value = "/list/{page}/{size}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getContracts(@PathVariable("page") int page, @PathVariable("size") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("orderDate"));
        // return contractRepository.findAllActive(p);
        Page<Contract> pageContent = contractRepository.findAllActive(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns first page of a paged list of all active contracts included deleted. Page sise is
     * equals to the default page size.
     * 
     * @return first page of contract's list, with default size
     */
    @GetMapping(value = "/list/deleted")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getAllContracts() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("orderDate"));
        // return contractRepository.findAllNotArchived(p);
        Page<Contract> pageContent = contractRepository.findAllNotArchived(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns first page of a paged list of all active contracts included deleted.
     * 
     * @param page page to fetch (0 based)
     * 
     * @return the contract's list page of default size
     */
    @GetMapping(value = "/list/deleted/{page}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getAllContracts(@PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, defaultPageSize, Sort.by("orderDate"));
        // return contractRepository.findAllNotArchived(p);
        Page<Contract> pageContent = contractRepository.findAllNotArchived(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns a page of a paged list of all active contracts included deleted.
     * 
     * @param page page to fetch (0 based)
     * @param size page's size
     * 
     * @return the requested page of the given size
     */
    @GetMapping(value = "/list/deleted/{page}/{size}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getAllContracts(@PathVariable("page") int page, @PathVariable("size") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("orderDate"));
        // return contractRepository.findAllNotArchived(p);
        Page<Contract> pageContent = contractRepository.findAllNotArchived(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns first page of a paged list of all active contracts included deleted. Page sise is
     * equals to the default page size.
     * 
     * @return first page of contract's list, with default size
     */
    @GetMapping(value = "/list/archived")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getArchivedContracts() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("orderDate"));
        // return contractRepository.findAllArchived(p);
        Page<Contract> pageContent = contractRepository.findAllArchived(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns first page of a paged list of all active contracts included deleted.
     * 
     * @param page page to fetch (0 based)
     * 
     * @return the contract's list page of default size
     */
    @GetMapping(value = "/list/archived/{page}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getArchivedContracts(@PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, defaultPageSize, Sort.by("orderDate"));
        // return contractRepository.findAllArchived(p);
        Page<Contract> pageContent = contractRepository.findAllArchived(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * returns a page of a paged list of all active contracts included deleted.
     * 
     * @param page page to fetch (0 based)
     * @param size page's size
     * 
     * @return the requested page of the given size
     */
    @GetMapping(value = "/list/archived/{page}/{size}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Page<ContractDto> getArchivedContracts(@PathVariable("page") int page, @PathVariable("size") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("orderDate"));
        // return contractRepository.findAllArchived(p);
        Page<Contract> pageContent = contractRepository.findAllArchived(p);
        return new PageImpl<ContractDto>(pageContent.stream()
            .map(contract->ContractDto.convertToDto(contract))
            .collect(Collectors.toList()), p, pageContent.getTotalElements());
    }

    /**
     * Get a Contract by id.
     * 
     * @param id Contract's ID
     * @param Authentication User's logged details
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Optional<Contract> getContract(@PathVariable("id") long id) {
        return contractRepository.findById(id);
    }

    /**
     * Adds a Contract
     * @param newContract contract to be added
     * @param authentication User's logged details
     * @return created Contract
     */
    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Contract addCustomer(@Valid @RequestBody Contract newContract, Authentication authentication) {    
        return contractRepository.save(newContract);
    }

    /**
     * Delete a Contract. The contract is first logically deleted and then an attempt to
     * delete the contract physically is performed.
     * 
     * @param id Contract's ID
     * @param Authentication User's logged details
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public void deleteContract(@PathVariable("id") long id) {
        Optional<Contract> result = contractRepository.findById(id);

        if (result.isPresent()) {
            Contract contract = result.get();

            deleteContractLogically(contract);

            deleteContractPhysically(contract.getId());
        }
    }

    /**
     * Deletes a contract logically.
     * 
     * @param contract Contract entity
     */
    @Transactional
    private void deleteContractLogically(Contract contract) {
        contract.setDeleted(true);
        // update user for logical deletion
        log.info("Perform contract logical deletion");
        contractRepository.save(contract);
    }

    /**
     * Attempt to delete physically a contract with ID. If an exception occurs it is
     * trapped in order to avoid returning an error.
     * 
     * @param id Contract's ID
     */
    @Transactional
    private void deleteContractPhysically(Long id) {
        // attempt physical deletion is possible ...
        log.info("Attempt contract physical deletion");
        try {
            contractRepository.deleteById(id);
        } catch (Exception ex) {
            log.info("Contract can not be deleted physically ...");
        }
    }

    /**
     * Deletes contracts in list.
     * 
     * @param contractIds contracts IDs list
     */
    @DeleteMapping(path = "/list/{ids}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public void deleteContracts(@PathVariable("ids") String contractIds) {
        log.info("Deleting contracts with Ids : " + contractIds);
        List<Long> idsList = Arrays.asList(contractIds.split(",")).stream().map(Long::parseLong)
                .collect(Collectors.toList());
        log.info("list of ids : " + idsList);

        for (Long id : idsList) {
            Optional<Contract> result = contractRepository.findById(id);
            if (result.isPresent()) {
                Contract contract = result.get();

                deleteContractLogically(contract);
                
                deleteContractPhysically(contract.getId());
            }
        }

    }

    /**
     * Undelete (logically contract) user.
     * 
     * @param id Contract's id
     * 
     */
    @PutMapping(value = "/undelete/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Contract undeleteContract(@PathVariable("id") long id) {
        Optional<Contract> result = contractRepository.findById(id);

        if (result.isPresent()) {
            Contract contract = result.get();
            contract.setDeleted(false);
            contract.setArchived(false);
            // update user to reset logical deletion
            log.info("Undelete contract");
            contractRepository.save(contract);
        }
        return result.isPresent() ? result.get() : null;
    }

    /**
     * archive contract.
     * 
     * @param id Contract's id
     * 
     */
    @PutMapping(value = "/archive/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Contract archiveContract(@PathVariable("id") long id) {
        Optional<Contract> result = contractRepository.findById(id);

        if (result.isPresent()) {
            Contract contract = result.get();
            contract.setArchived(true);
            log.info("Archive contract");
            contractRepository.save(contract);
            return contract;
        }
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Unachive user.
     * 
     * @param id Contract's id
     * 
     */
    @PutMapping(value = "/unarchive/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public Contract unarchivContract(@PathVariable("id") long id) {
        Optional<Contract> result = contractRepository.findById(id);

        if (result.isPresent()) {
            Contract contract = result.get();
            contract.setDeleted(false);
            contract.setArchived(false);
            log.info("Unarchive contract");
            contractRepository.save(contract);
        }
        return result.isPresent() ? result.get() : null;
    }

}