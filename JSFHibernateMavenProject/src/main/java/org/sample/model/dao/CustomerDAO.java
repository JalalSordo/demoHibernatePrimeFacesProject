/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.sample.model.dto.CustomerDTO;
import org.sample.model.entities.Address;
import org.sample.model.entities.Customer;
import org.sample.model.entities.Store;

/**
 *
 */
public class CustomerDAO implements Serializable {

    private final SessionFactory sessionFactory;

    public CustomerDAO() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<Customer> findListOfCustomers() {
        Session session = sessionFactory.openSession();
        List<Customer> listOfCustomers = session.getNamedQuery("findAllCustomers")
                .list();
        session.close();
        return listOfCustomers;
    }

    public void deleteCustomer(Customer aCustomer) {
        Session session = sessionFactory.openSession();
        //this is necessary because we didn't map this tables so we will have to remove them with native SQL.
        //after that we can remov ethe customer.
        session.createSQLQuery("delete from rental where customer_id=" + aCustomer.getCustomerId()).executeUpdate();
        session.createSQLQuery("delete from payment where customer_id=" + aCustomer.getCustomerId()).executeUpdate();

        session.delete(aCustomer);
        session.flush();
        session.close();

    }

    public void saveCustomer(Customer aCustomer) {

    }

    public void saveCustomer(Customer aCustomer, boolean isNew) {
        Session session = sessionFactory.openSession();
        if (isNew) {
            aCustomer.setStore((Store) session.load(Store.class, new Byte("1")));
            aCustomer.setAddress((Address) session.load(Address.class, new Short("1")));
            aCustomer.setCreateDate(new Date());
            session.persist(aCustomer);
        } else {
            aCustomer.setLastUpdate(new Date());
            session.merge(aCustomer);

        }
        session.flush();
        session.close();
    }

    public Customer getCustomerByID(Short id) {
        Session session = sessionFactory.openSession();
        Customer customer = (Customer) session.getNamedQuery("findCustomerById")
                .setShort("id", id)
                .uniqueResult();
        session.close();
        return customer;
    }

    public ArrayList<CustomerDTO> findListOfCustomersDTO() {
        ArrayList<CustomerDTO> listOfCustomersDTO = new ArrayList<CustomerDTO>();
        ArrayList<Customer> listOfCustomers = (ArrayList<Customer>) findListOfCustomers();
        for (Customer customer : listOfCustomers) {
            CustomerDTO dto = new CustomerDTO();
            dto.setCustomerId(customer.getCustomerId());
            dto.setFirstName(customer.getFirstName());
            dto.setLastName(customer.getLastName());
            dto.setActive(customer.isActive());
            dto.setAddress(customer.getAddress().getCity().getCity() + "," + customer.getAddress().getCity().getCountry().getCountry());
            listOfCustomersDTO.add(dto);
        }
        return listOfCustomersDTO;

    }

    public List<Customer> findListOfCustomersWithFilters(String firstNameFilter, String lastNameFilter, String cityFilter, String countryFilter) {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(Customer.class);
        Criteria criteriaAdresse = criteria.createCriteria("address");
        Criteria criteriaCity = criteriaAdresse.createCriteria("city");
        
        if (isNotEmpty(firstNameFilter)) {
            Criterion firstNameCriterion = Restrictions.like("firstName", firstNameFilter, MatchMode.ANYWHERE);
            criteria.add(firstNameCriterion);
        }
        if (isNotEmpty(firstNameFilter)) {
            Criterion lastNameCriterion = Restrictions.like("lastName", lastNameFilter, MatchMode.ANYWHERE);
            criteria.add(lastNameCriterion);
        }
        if (isNotEmpty(cityFilter)) {
            Criterion cityCriterion = Restrictions.like("city", cityFilter, MatchMode.ANYWHERE);
            criteriaCity.add(cityCriterion);
        }
        if (isNotEmpty(countryFilter)) {
            Criteria criteriaCountry = criteriaCity.createCriteria("country");
            Criterion cityCriterion = Restrictions.like("country", countryFilter, MatchMode.ANYWHERE);
            criteriaCountry.add(cityCriterion);
        }

        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List<Customer> listOfCustomers = criteria
                .list();
        session.close();
        return listOfCustomers;
    }

    private boolean isNotEmpty(String value) {
        if (value != null) {
            return !value.equals("");
        }
        return false;
    }

}
