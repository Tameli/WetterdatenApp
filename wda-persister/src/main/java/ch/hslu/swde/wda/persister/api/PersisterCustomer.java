package ch.hslu.swde.wda.persister.api;


import ch.hslu.swde.wda.domain.Customer;

/**
 * Diese Schnittstelle gibt die Funktionalitäten vor, welche für die
 * Persistierung von Kundendaten benötigt werden.
 */

public interface PersisterCustomer {
	
	/**
     * Speichert den übergebenen Kunden.
     *
     * @param customer der zu speicherende Kunde
     * @return der gespeicherte Kunde
     * @throws Exception falls das Speichern misslingen sollte
     */
	void saveCustomer(Customer customer) throws Exception;
	
	/**
     * Aktualisiert den übergebenen Kunden.
     *
     * @param customer der neue Kunde
     * @return true wenn Kunde erfolgreich aktualisiert wurde
     * @throws Exception falls die Aktualisierung misslingen sollte
     */

	boolean updateCustomer(Customer customer) throws Exception;
	
	/**
     * Löscht den übergebenen Kunden.
     *
     * @param customer der Kunde der gelöscht werden soll
     * @return true wenn der Kunde erfolgreich gelöschte wurde
     * @throws Exception falls Löschvorgang misslingen sollte
     */

	boolean deleteCustomer(Customer customer) throws Exception;
	
	
	/**
     * Liefert den Kunden für die übergebene Id zurück.
     *
     * @param personNummer die KundenId
     * @return der gefundene Kunde
     * @throws Exception falls die Suche misslingen sollte
     */
    Customer findCustomer(String emailAddress) throws Exception;
}
