package org.agoncal.application.petstore.view.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.util.Loggable;

/**
 * Backing bean for Customer entities.
 * <p/>
 * This class provides CRUD functionality for all Customer entities. It focuses purely on Java EE 6 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class CustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Conversation conversation;

    @PersistenceContext(unitName = "applicationPetstorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    /*
     * Support creating and retrieving Customer entities
     */

    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Customer customer;

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String create() {
        conversation.begin();
        conversation.setTimeout(1800000L);
        return "create?faces-redirect=true";
    }

    public void retrieve() {
        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (conversation.isTransient()) {
            conversation.begin();
            conversation.setTimeout(1800000L);
        }

        if (id == null) {
            customer = example;
        } else {
            customer = findById(getId());
        }
    }

    public Customer findById(Long id) {
        return this.entityManager.find(Customer.class, id);
    }

    /*
     * Support updating and deleting Customer entities
     */

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(this.customer);
                return "search?faces-redirect=true";
            }

            entityManager.merge(this.customer);
            return "view?faces-redirect=true&id=" + this.customer.getId();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Customer deletableEntity = findById(getId());

            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            return "search?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    /*
     * Support searching Customer entities with pagination
     */

    private int page;
    private long count;
    private List<Customer> pageItems;

    private Customer example = new Customer();

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return 10;
    }

    public Customer getExample() {
        return this.example;
    }

    public void setExample(Customer example) {
        this.example = example;
    }

    public String search() {
        this.page = 0;
        return null;
    }

    public void paginate() {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

        // Populate this.count

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Customer> root = countCriteria.from(Customer.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        root = criteria.from(Customer.class);
        TypedQuery<Customer> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
        this.pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Customer> root) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String firstName = this.example.getFirstName();
        if (firstName != null && !"".equals(firstName)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("firstName")), '%' + firstName.toLowerCase() + '%'));
        }
        String lastName = this.example.getLastName();
        if (lastName != null && !"".equals(lastName)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("lastName")), '%' + lastName.toLowerCase() + '%'));
        }
        String telephone = this.example.getTelephone();
        if (telephone != null && !"".equals(telephone)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("telephone")), '%' + telephone.toLowerCase() + '%'));
        }
        String email = this.example.getEmail();
        if (email != null && !"".equals(email)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("email")), '%' + email.toLowerCase() + '%'));
        }
        String login = this.example.getLogin();
        if (login != null && !"".equals(login)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("login")), '%' + login.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Customer> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    /*
     * Support listing and POSTing back Customer entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Customer> getAll() {

        CriteriaQuery<Customer> criteria = this.entityManager.getCriteriaBuilder().createQuery(Customer.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Customer.class))).getResultList();
    }

    @Resource
    private SessionContext sessionContext;

    public Converter<Customer> getConverter() {

        final CustomerBean ejbProxy = this.sessionContext.getBusinessObject(CustomerBean.class);

        return new Converter<Customer>() {

            @Override
            public Customer getAsObject(FacesContext context, UIComponent component, String value) {
                return ejbProxy.findById(Long.valueOf(value));
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Customer value) {
                if (value == null) {
                    return "";
                }

                return String.valueOf(value.getId());
            }
        };
    }

    /*
     * Support adding children to bidirectional, one-to-many tables
     */

    private Customer add = new Customer();

    public Customer getAdd() {
        return add;
    }

    public Customer getAdded() {
        Customer added = add;
        add = new Customer();
        return added;
    }
}
