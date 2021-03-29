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

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.util.Loggable;

/**
 * Backing bean for Category entities.
 * <p/>
 * This class provides CRUD functionality for all Category entities. It focuses purely on Java EE 8 standards (e.g.
 * <tt>&#64;ConversationScoped</tt> for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
@Loggable
public class CategoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Conversation conversation;

    @PersistenceContext(unitName = "applicationPetstorePU", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    private Category category;

    /*
     * Support creating and retrieving Category entities
     */

    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String create() {
        this.conversation.begin();
        this.conversation.setTimeout(1800000L);
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
            category = example;
        } else {
            this.category = findById(getId());
        }
    }

    public Category findById(Long id) {
        return this.entityManager.find(Category.class, id);
    }

    /*
     * Support updating and deleting Category entities
     */

    public String update() {
        conversation.end();

        try {
            if (id == null) {
                entityManager.persist(this.category);
                return "search?faces-redirect=true";
            }

            entityManager.merge(this.category);
            return "view?faces-redirect=true&id=" + this.category.getId();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        conversation.end();

        try {
            Category deletableEntity = findById(getId());

            entityManager.remove(deletableEntity);
            entityManager.flush();
            return "search?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    /*
     * Support searching Category entities with pagination
     */

    private int page;
    private long count;
    private List<Category> pageItems;

    private Category example = new Category();

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return 10;
    }

    public Category getExample() {
        return this.example;
    }

    public void setExample(Category example) {
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
        Root<Category> root = countCriteria.from(Category.class);
        countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
        this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

        // Populate this.pageItems

        CriteriaQuery<Category> criteria = builder.createQuery(Category.class);
        root = criteria.from(Category.class);
        TypedQuery<Category> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
        this.pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Category> root) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String name = this.example.getName();
        if (name != null && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }
        String description = this.example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    public List<Category> getPageItems() {
        return this.pageItems;
    }

    public long getCount() {
        return this.count;
    }

    /*
     * Support listing and POSTing back Category entities (e.g. from inside an HtmlSelectOneMenu)
     */

    public List<Category> getAll() {

        CriteriaQuery<Category> criteria = this.entityManager.getCriteriaBuilder().createQuery(Category.class);
        return this.entityManager.createQuery(criteria.select(criteria.from(Category.class))).getResultList();
    }

    @Resource
    private SessionContext sessionContext;

    public Converter<Category> getConverter() {

        final CategoryBean ejbProxy = sessionContext.getBusinessObject(CategoryBean.class);

        return new Converter<Category>() {

            @Override
            public Category getAsObject(FacesContext context, UIComponent component, String value) {
                return ejbProxy.findById(Long.valueOf(value));
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Category value) {
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

    private Category add = new Category();

    public Category getAdd() {
        return add;
    }

    public Category getAdded() {
        Category added = add;
        add = new Category();
        return added;
    }
}
