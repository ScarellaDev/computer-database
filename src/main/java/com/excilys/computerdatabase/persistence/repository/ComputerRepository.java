package com.excilys.computerdatabase.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excilys.computerdatabase.domain.Computer;

public interface ComputerRepository extends JpaRepository<Computer, Long> {

  List<Computer> findByCompanyId(Long id);

  void deleteByCompanyId(Long id);

  Page<Computer> findByNameStartingWithOrCompanyNameStartingWith(String name, String companyName,
      Pageable pageable);

}
