package ch.hslu.swde.wda.persister.api;


/**
 * Diese Schnittstelle gibt die Funktionalitäten vor, welche für die
 * Persistierung von Mitarbeiterdaten benötigt werden.
 */

import ch.hslu.swde.wda.domain.*;

public interface PersisterEmployee {

	/**
     * Speichert den übergebenen Mitarbeiter.
     *
     * @param employee der zu speicherende Kunde
     * @return der gespeicherte Mitarbeiter
     * @throws Exception falls das Speichern misslingen sollte
     */
	
	void saveEmployee(Employee employee) throws Exception;
	
	/**
     * Aktualisiert den übergebenen Mitarbeiter
     *
     * @param employee den neuen Mitarbeiter
     * @param id die Mitarbeiter ID
     * @return der aktualisierte Mitarbeiter
     * @throws Exception falls die Aktualisierung misslingen sollte
     */
	
	boolean updateEmployee(Employee employee, long id) throws Exception;
	
	/**
     * Löscht den übergebenen Mitarbeiter
     *
     * @param employee der Mitarbeiter der gelöscht werden soll
     * @return der gelöschte Mitarbeiter
     * @throws Exception falls Löschvorgang misslingen sollte
     */
	
	boolean deleteEmployee(Employee employee) throws Exception;
	
	/**
     * Liefert den Mitarbeiter für die übergebene Id zurück.
     *
     * @param personNummer die MitarbeiterId
     * @return der gefundene Mitarbeiter
     * @throws Exception falls die Suche misslingen sollte
     */
	
	Employee findEmployee(long id) throws Exception;
	
	/**
	 * Liefert den Mitarbeiter für den Namen und das angegebene Passwort
	 * 
	 * @param pwd das Passwort 
	 * @param name den Namen 
	 * @return den gefundenen Employee
	 * @throws Exception
	 */

    Employee findEmployeeByPassword(String name, String pwd) throws Exception;
	
	
}