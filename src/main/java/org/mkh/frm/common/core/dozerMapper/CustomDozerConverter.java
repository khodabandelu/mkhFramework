package org.mkh.frm.common.core.dozerMapper;

import org.apache.commons.lang3.ClassUtils;
import org.dozer.ConfigurableCustomConverter;
import org.dozer.MappingException;

public abstract class CustomDozerConverter<A, B> implements ConfigurableCustomConverter {

	private String parameter;
	private final Class<A> prototypeA;
	private final Class<B> prototypeB;

	/**
	 * Defines two types, which will take part transformation. As Dozer supports
	 * bi-directional mapping it is not known which of the classes is source and
	 * which is destination. It will be decided in runtime.
	 *
	 * @param prototypeA
	 *            type one
	 * @param prototypeB
	 *            type two
	 */
	public CustomDozerConverter(final Class<A> prototypeA, final Class<B> prototypeB) {
		this.prototypeA = prototypeA;
		this.prototypeB = prototypeB;
	}

	// Method first checks exact type matches and only then checks for
	// assignement
	public Object convert(final Object existingDestinationFieldValue, final Object sourceFieldValue, final Class<?> dClass, final Class<?> sClass) {
		final Class<?> dstClass = ClassUtils.primitiveToWrapper(dClass);
		final Class<?> srcClass = ClassUtils.primitiveToWrapper(sClass);

		if (prototypeA.equals(dstClass)) {
			return convertFrom((B) sourceFieldValue, srcClass, (A) existingDestinationFieldValue, dstClass);
		} else if (prototypeB.equals(dstClass)) {
			return convertTo((A) sourceFieldValue, srcClass, (B) existingDestinationFieldValue, dstClass);
		} else if (prototypeA.equals(srcClass)) {
			return convertTo((A) sourceFieldValue, srcClass, (B) existingDestinationFieldValue, dstClass);
		} else if (prototypeB.equals(srcClass)) {
			return convertFrom((B) sourceFieldValue, srcClass, (A) existingDestinationFieldValue, dstClass);
		} else if (prototypeA.isAssignableFrom(dstClass)) {
			return convertFrom((B) sourceFieldValue, srcClass, (A) existingDestinationFieldValue, dstClass);
		} else if (prototypeB.isAssignableFrom(dstClass)) {
			return convertTo((A) sourceFieldValue, srcClass, (B) existingDestinationFieldValue, dstClass);
		} else if (prototypeA.isAssignableFrom(srcClass)) {
			return convertTo((A) sourceFieldValue, srcClass, (B) existingDestinationFieldValue, dstClass);
		} else if (prototypeB.isAssignableFrom(srcClass)) {
			return convertFrom((B) sourceFieldValue, srcClass, (A) existingDestinationFieldValue, dstClass);
		} else {
			throw new MappingException("Destination Type (" + dstClass.getName() + ") is not accepted by this Custom Converter (" + this.getClass().getName() + ")!");
		}

	}

	/**
	 * Converts the source field to the destination field and return the
	 * resulting destination value.
	 *
	 * @param source
	 *            the value of the source field
	 * @param destination
	 *            the current value of the desitinatino field (or null)
	 * @return the resulting value for the destinatino field
	 */
	abstract public B convertTo(A source, Class sourceClass, B destination, Class destinationClass);

	/**
	 * Converts the source field to the destination field and return the
	 * resulting destination value
	 *
	 * @param source
	 *            the value of the source field
	 * @param destination
	 *            the current value of the desitinatino field (or null)
	 * @return the resulting value for the destinatino field
	 */
	abstract public A convertFrom(B source, Class sourceClass, A destination, Class destinationClass);

	/**
	 * Sets the configured parameter value for this converter instance. Should
	 * be called by Dozer internaly before actual mapping.
	 *
	 * @param parameter
	 *            configured parameter value
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Retrieves the static parameter configured for this particular converter
	 * instance. It is not advisable to call this method from converter
	 * constructor as the parameter is not yet there.
	 *
	 * @return parameter value
	 * @throws IllegalStateException
	 *             if parameter has not been set yet.
	 */
	public String getParameter() {
		if (parameter == null) {
			throw new IllegalStateException("Custom Converter Parameter has not yet been set!");
		}
		return parameter;
	}

}
