package com.excilys.computerdatabase.webservice.impl;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.webservice.ICompanyWebService;

@Service
@Path("/company")
public class CompanyWebService implements ICompanyWebService {

  @Autowired
  private ICompanyService companyService;

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  public Company getById(@PathParam("id") Long id) {
    return companyService.getById(id);
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Company> getAll() {
    return companyService.getAll();
  }

  @Override
  @DELETE
  @Path("/{id: [0-9]+}")
  public Response removeById(@PathParam("id") Long id) {
    Company removedCompany = companyService.removeById(id);

    Status status = removedCompany != null ? Status.NO_CONTENT : Status.BAD_REQUEST;

    return Response.status(status).build();
  }
}
