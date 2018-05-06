/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.sample.controller.utils.SessionUtils;
import org.sample.model.dao.CustomerDAO;
import org.sample.model.entities.Customer;
import org.sample.model.entities.User;

/**
 *
 */
@ManagedBean(name = "customerBean")
@SessionScoped
public class CustomerBean implements Serializable {

    private List<Customer> listOfCustomers;
    private User connectedUser;
    private Customer selectedCustomer;
    private int currentCustomerIndex;
    private CustomerDAO customerDAO;
    private boolean newUserMode;
    private String firstNameFilter;
    private String lastNameFilter;
    private String cityFilter;
    private String countryFilter;

    @PostConstruct
    public void init() {
        connectedUser = SessionUtils.getConnectedUser();
        customerDAO = new CustomerDAO();
        listOfCustomers = customerDAO.findListOfCustomers();
    }

    public void getCustomer(int step) {
        currentCustomerIndex = currentCustomerIndex + step;
        try {
            selectedCustomer = listOfCustomers.get(currentCustomerIndex);
        } catch (IndexOutOfBoundsException ex) {

        }
    }

    public void showExistingCustomerDialog(Customer aCustomer, Integer index) throws IOException {
        selectedCustomer = aCustomer;
        newUserMode = false;
        currentCustomerIndex = index;
        FacesContext.getCurrentInstance().getExternalContext().redirect("customerEditor.xhtml");
    }

    public void showNewCustomerDialog() throws IOException {
        selectedCustomer = new Customer();
        newUserMode = true;
        FacesContext.getCurrentInstance().getExternalContext().redirect("customerEditor.xhtml");
    }

    public void deleteCustomer(Customer aCustomer) {
        customerDAO.deleteCustomer(aCustomer);
        listOfCustomers = customerDAO.findListOfCustomers();
    }

    public String disconnect() {
        SessionUtils.getSession().setAttribute("user", null);
        SessionUtils.getSession().invalidate();
        return "login?faces-redirect=true";
    }

    public void saveCustomer() {
        customerDAO.saveCustomer(selectedCustomer, newUserMode);
        listOfCustomers = customerDAO.findListOfCustomers();
        newUserMode = false;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Le client a été enregistrer avec succès !"));
    }

    public void doSearch() {
        listOfCustomers = customerDAO.findListOfCustomersWithFilters(firstNameFilter, lastNameFilter, cityFilter, countryFilter);
    }

    public List<Customer> getListOfCustomers() {
        return listOfCustomers;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    
    public boolean isNewUserMode() {
        return newUserMode;
    }

    public void setNewUserMode(boolean newUserMode) {
        this.newUserMode = newUserMode;
    }

    public String getCityFilter() {
        return cityFilter;
    }

    public void setCityFilter(String cityFilter) {
        this.cityFilter = cityFilter;
    }

    public String getCountryFilter() {
        return countryFilter;
    }

    public void setFirstNameFilter(String firstNameFilter) {
        this.firstNameFilter = firstNameFilter;
    }

    public String getFirstNameFilter() {
        return firstNameFilter;
    }

    public void setCountryFilter(String countryFilter) {
        this.countryFilter = countryFilter;
    }

    public String getLastNameFilter() {
        return lastNameFilter;
    }

    public void setLastNameFilter(String lastNameFilter) {
        this.lastNameFilter = lastNameFilter;
    }

}