package com.excilys.computerdatabase.webservice.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.dto.ComputerDto;
import com.excilys.computerdatabase.dto.ComputerDtoConverter;
import com.excilys.computerdatabase.service.ICompanyService;
import com.excilys.computerdatabase.service.IComputerService;
import com.excilys.computerdatabase.webservice.IComputerWebService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Service
@Path("/computer")
@Api(value = "/computer", description = "Computer related operations")
public class ComputerWebService implements IComputerWebService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerWebService.class);

  @Autowired
  private IComputerService    computerService;

  @Autowired
  private ICompanyService     companyService;

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Find Computer by id")
  public ComputerDto getById(
      @ApiParam(value = "ID of Computer to fetch", required = true) @PathParam("id") Long id) {

    Computer computer = computerService.getById(id);

    return ComputerDtoConverter.toDto(computer);
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all Computers")
  public List<ComputerDto> getAll() {

    List<Computer> computers = computerService.getAll();

    return ComputerDtoConverter.toDto(computers);
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Add a Computer")
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "The company specified does not exist"),
      @ApiResponse(code = 201, message = "Add Success") })
  public Response addByComputer(
      @ApiParam(value = "Computer object", required = true) ComputerDto computerDto) {

    Company company = null;
    if (computerDto.getCompanyId() > 0) {
      // A valid companyId was supplied
      company = companyService.getById(computerDto.getCompanyId());
      if (company == null) {
        // Requested company does not exist
        LOGGER.warn("add(): Attempted to create a computer linked an invalid company");
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    Computer computer = ComputerDtoConverter.toComputer(computerDto, "yyyy-MM-dd");
    computer.setCompany(company);

    Computer addedComputer = computerService.addByComputer(computer);

    URI location = null;
    try {
      location = new URI(String.format("/computer/%d", addedComputer.getId()));
      LOGGER.debug("add(): location is set to {}", location);
    } catch (URISyntaxException e) {
      LOGGER.warn("add(): Failed to generate location URI: {}", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    return Response.created(location).status(Status.CREATED).build();

  }

  @Override
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Update an existing Computer")
  @ApiResponses(value = { @ApiResponse(code = 404, message = "Requested Computer does not exist"),
      @ApiResponse(code = 200, message = "Update Success") })
  public Response updateByComputer(
      @ApiParam(value = "Computer object with the updated details. The ID property must be set.", required = true) ComputerDto computerDto) {

    Computer computer = computerService.getById(computerDto.getId());
    if (computer == null) {
      LOGGER.warn("update(): Attempted to update a computer that does not exist");
      return Response.status(Status.BAD_REQUEST).build();
    }

    computerService.updateByComputer(ComputerDtoConverter.toComputer(computerDto, "yyyy-MM-dd"));

    URI location = null;
    try {
      location = new URI(String.format("/computer/%d", computer.getId()));
      LOGGER.debug("update(): location is set to {}", location);
    } catch (URISyntaxException e) {
      LOGGER.warn("update(): Failed to generate location URI: {}", e);
      return Response.status(Status.INTERNAL_SERVER_ERROR).build();
    }

    return Response.created(location).status(Status.CREATED).build();
  }

  @Override
  @DELETE
  @Path("/{id: [0-9]+}")
  @ApiOperation(value = "Remove a Computer")
  @ApiResponses(value = { @ApiResponse(code = 204, message = "Remove Success"),
      @ApiResponse(code = 400, message = "Failed to remove the Computer") })
  public Response removeById(
      @ApiParam(value = "ID of the Computer to remove", required = true) @PathParam("id") Long id) {

    Computer removedComputer = computerService.removeById(id);

    Status status = removedComputer != null ? Status.NO_CONTENT : Status.BAD_REQUEST;

    return Response.status(status).build();
  }
}
