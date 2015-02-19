package com.excilys.computerdatabase.webservice;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.domain.Company;

/**
* Interface implemented by services to manage Company objects.
*
* @author Jeremy SCARELLA
*/
@Service
@Path("/company")
public interface ICompanyWebService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{id: [0-9]+}")
  Company getById(@PathParam("id") Long id);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<Company> getAll();

  @DELETE
  @Path("/{id: [0-9]+}")
  Response removeById(@PathParam("id") Long id);
}