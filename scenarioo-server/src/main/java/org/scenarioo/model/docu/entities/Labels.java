package org.scenarioo.model.docu.entities;

import org.scenarioo.api.rules.CharacterChecker;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Labels {

	@XmlElement(name = "label")
	private Set<String> labels = new LinkedHashSet<String>();

	/**
	 * <p>
	 * Add a single label to the set of labels. This method can be used in a chained way:
	 * </p>
	 * <p>
	 * <code>Labels labels = new Labels().addLabel("first label").addLabel("second label");</code>
	 * </p>
	 */
	public Labels addLabel(final String label) {
		CharacterChecker.checkLabel(label);
		labels.add(label);
		return this;
	}

	/**
	 * Replaces all labels with the supplied set of labels.
	 */
	public void setLabels(final Set<String> labels) {
		checkLabels(labels);
		this.labels = new LinkedHashSet<String>(labels);
	}

	/**
	 * @return All labels as a set, never <code>null</code>.
	 */
	public Set<String> getLabels() {
		if (labels == null) {
			labels = new HashSet<String>();
		}
		return labels;
	}

	private void checkLabels(final Set<String> labels) {
		if (labels == null) {
			throw new NullPointerException("Labels must not be null.");
		}
		for (String label : labels) {
			CharacterChecker.checkLabel(label);
		}
	}

	public int size() {
		return labels.size();
	}

	public boolean isEmpty() {
		return labels.isEmpty();
	}

	public boolean contains(final Object o) {
		return labels.contains(o);
	}

	public Iterator<String> iterator() {
		return labels.iterator();
	}

	public Object[] toArray() {
		return labels.toArray();
	}

	public <T> T[] toArray(final T[] a) {
		return labels.toArray(a);
	}

	public boolean add(final String e) {
		return labels.add(e);
	}

	public boolean remove(final Object o) {
		return labels.remove(o);
	}

	public boolean containsAll(final Collection<?> c) {
		return labels.containsAll(c);
	}

	public boolean addAll(final Collection<? extends String> c) {
		return labels.addAll(c);
	}

	public boolean retainAll(final Collection<?> c) {
		return labels.retainAll(c);
	}

	public boolean removeAll(final Collection<?> c) {
		return labels.removeAll(c);
	}

	public void clear() {
		labels.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((labels == null) ? 0 : labels.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Labels other = (Labels) obj;
		if (labels == null) {
			if (other.labels != null) return false;
		} else if (!labels.equals(other.labels)) return false;
		return true;
	}

}
