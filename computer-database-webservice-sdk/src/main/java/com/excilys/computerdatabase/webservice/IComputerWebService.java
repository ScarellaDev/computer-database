package com.excilys.computerdatabase.webservice;

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

import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.dto.ComputerDto;

/**
* Interface implemented by services to manage Computer objects.
*
* @author Jeremy SCARELLA
*/
@Service
@Path("/computer")
public interface IComputerWebService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  ComputerDto getById(@PathParam("id") Long id);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<ComputerDto> getAll();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  Response addByComputer(ComputerDto computerDto);

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  Response updateByComputer(ComputerDto computer);

  @DELETE
  @Path("/{id: [0-9]+}")
  Response removeById(@PathParam("id") Long id);

}
