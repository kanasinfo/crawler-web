package com.ch.common.support;

import java.io.Serializable;


/**
 * The <code>AbstractModel</code> class is a template for domain object.
 * Domain objects can extend this class, and they must implement those
 * abstract methods.
 *
 * @author J.Mars
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractModel<ID extends Serializable> implements Serializable {

    /**
     * Used by equals method to identity an object equals another one.
     * Notice: this method must return a not null result.
     *
     * @return
     */
    public abstract ID getId();

    /**
     * Justifies that object o equals the original object.
     *
     * @return boolean result of the justification.
     * @see Object#equals()
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractModel<?> other = (AbstractModel<?>) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("class:\"").append(this.getClass().getName()).append("\",id:\"")
                .append(this.getId()).append("\"}");
        return sb.toString();
    }
}
