/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.model.docu.entities.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ObjectList<T> implements List<T>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<T> items = new ArrayList<T>();
	
	public ObjectList() {
	}
	
	public ObjectList(final List<T> items) {
		this.items = items;
	}
	
	public ObjectList(final T[] items) {
		this.items = Arrays.asList(items);
	}
	
	@Override
	public int size() {
		return items.size();
	}
	
	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	@Override
	public boolean contains(final Object o) {
		return items.contains(o);
	}
	
	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}
	
	@Override
	public Object[] toArray() {
		return items.toArray();
	}
	
	@Override
	@SuppressWarnings("hiding")
	public <T> T[] toArray(final T[] a) {
		return items.toArray(a);
	}
	
	@Override
	public boolean add(final T e) {
		return items.add(e);
	}
	
	@Override
	public boolean remove(final Object o) {
		return items.remove(o);
	}
	
	@Override
	public boolean containsAll(final Collection<?> c) {
		return items.containsAll(c);
	}
	
	@Override
	public boolean addAll(final Collection<? extends T> c) {
		return items.addAll(c);
	}
	
	@Override
	public boolean addAll(final int index, final Collection<? extends T> c) {
		return items.addAll(index, c);
	}
	
	@Override
	public boolean removeAll(final Collection<?> c) {
		return items.removeAll(c);
	}
	
	@Override
	public boolean retainAll(final Collection<?> c) {
		return items.retainAll(c);
	}
	
	@Override
	public void clear() {
		items.clear();
	}
	
	@Override
	public T get(final int index) {
		return items.get(index);
	}
	
	@Override
	public T set(final int index, final T element) {
		return items.set(index, element);
	}
	
	@Override
	public void add(final int index, final T element) {
		items.add(index, element);
	}
	
	@Override
	public T remove(final int index) {
		return items.remove(index);
	}
	
	@Override
	public int indexOf(final Object o) {
		return items.indexOf(o);
	}
	
	@Override
	public int lastIndexOf(final Object o) {
		return items.lastIndexOf(o);
	}
	
	@Override
	public ListIterator<T> listIterator() {
		return items.listIterator();
	}
	
	@Override
	public ListIterator<T> listIterator(final int index) {
		return items.listIterator(index);
	}
	
	@Override
	public List<T> subList(final int fromIndex, final int toIndex) {
		return items.subList(fromIndex, toIndex);
	}
	
}
