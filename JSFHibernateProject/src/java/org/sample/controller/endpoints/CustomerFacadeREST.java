/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.endpoints;

import org.sample.controller.utils.TokenStore;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import org.sample.controller.utils.RESTUtils;
import org.sample.model.dao.CustomerDAO;
import org.sample.model.dto.CustomerDTO;

/**
 *
 */
@Path("customers")
public class CustomerFacadeREST {

    private final CustomerDAO customerDAO;

    public CustomerFacadeREST() {
        customerDAO = new CustomerDAO();

    }

    @GET
    public String findAll(@HeaderParam("token") String token) {
        if (TokenStore.validateToken(token)) {
            ArrayList<CustomerDTO> list = customerDAO.findListOfCustomersDTO();
            return RESTUtils.JSONFactory.toJson(list);
        } else {
            return "Invalid Token";
        }
    }

}
