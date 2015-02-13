package com.excilys.computerdatabase.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excilys.computerdatabase.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

  Page<Company> findAll(Pageable pageable);
}
