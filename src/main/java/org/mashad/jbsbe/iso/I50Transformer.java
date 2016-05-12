package org.mashad.jbsbe.iso;

public interface I50Transformer<T extends I50Message> {

    /**
     * Set the field according to the rules.
     * 
     * @param id
     *            Field id
     * @param value
     *            The value that has to be transformed and set
     * @param message
     *            The message to be manipulated
     * @return true if succeeded to set transformed value and false if not
     */
    public boolean setField(int id, Object value, T message);

}
