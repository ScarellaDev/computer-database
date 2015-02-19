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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Service
@Path("/company")
@Api(value = "/company", description = "Company related operations")
public class CompanyWebService implements ICompanyWebService {

  @Autowired
  private ICompanyService companyService;

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Find company by ID")
  public Company getById(
      @ApiParam(value = "ID of Company to fetch", required = true) @PathParam("id") Long id) {
    return companyService.getById(id);
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all companies")
  public List<Company> getAll() {
    return companyService.getAll();
  }

  @Override
  @DELETE
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Remove a company and all associated computers")
  @ApiResponses(value = { @ApiResponse(code = 204, message = "Company removed successfully") })
  public Response removeById(
      @ApiParam(value = "ID of Company to remove", required = true) @PathParam("id") Long id) {
    Company removedCompany = companyService.removeById(id);

    Status status = removedCompany != null ? Status.NO_CONTENT : Status.BAD_REQUEST;

    return Response.status(status).build();
  }
}
