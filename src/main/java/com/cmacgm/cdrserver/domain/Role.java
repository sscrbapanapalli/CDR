package com.cmacgm.cdrserver.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    @Column(name="role_name",length = 100)
    private String roleName;
    @Column(name="created_date")
    private Date createdDate;
    @Column(name="updated_date")
    private Date updatedDate;
    @Column(name="active_indicator")
    private boolean activeIndicator;
    @Column(name="created_by",length = 100)
    private String createdBy;
    @Column(name="updated_by",length = 100)
    private String updatedBy;  
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;  
  
    public Role() {
        super();
    }

    public Role(final String roleName) {
        super();
        this.roleName = roleName;
    }

    //

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }   
  
    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean isActiveIndicator() {
		return activeIndicator;
	}

	public void setActiveIndicator(boolean activeIndicator) {
		this.activeIndicator = activeIndicator;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(final Collection<User> users) {
        this.users = users;
    }    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role role = (Role) obj;
        if (!role.equals(role.roleName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Role [id=").append(id).append("]").append("[roleName=").append(roleName).append("]")
        .append("[createdDate=").append(createdDate).append("]").append("[updatedDate=").append(updatedDate).append("]")
        .append("[activeIndicator=").append(activeIndicator).append("]").append("[createdBy=").append(createdBy).append("]")
        .append("[updatedBy=").append(updatedBy).append("]");
        return builder.toString();
    }
}