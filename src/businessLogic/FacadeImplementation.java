package businessLogic;


import java.util.Date;
import java.util.Set;
import java.util.Vector;

import dataAccess.HibernateManager;
//import configuration.ConfigXML;¡
import domain.Booking;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import exceptions.BadDates;
import exceptions.OfferCanNotBeBooked;
import exceptions.OverlappingOfferExists;


public class FacadeImplementation implements ApplicationFacadeInterface {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;

	Set<Owner> owners;
	Set<RuralHouse> ruralHouses;
	HibernateManager hm;
 

	public FacadeImplementation() {
		hm=HibernateManager.getInstance();
	}
	

	/**
	 * This method creates an offer with a house number, first day, last day and price
	 * 
	 * @param House
	 *            number, start day, last day and price
	 * @return the created offer, or null, or an exception
	 */
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay,
			float price) throws OverlappingOfferExists, BadDates, Exception {
		if (firstDay.compareTo(lastDay)>=0) throw new BadDates();
		ruralHouses=null;
		owners=null;
		boolean b = hm.existsOverlappingOffer(ruralHouse,firstDay,lastDay); // The ruralHouse object in the client may not be updated
		if (!b) return hm.createOffer(ruralHouse,firstDay,lastDay,price);			
		return null;
	}

	/**
	 * This method creates a book with a corresponding parameters
	 * 
	 * @param First
	 *            day, last day, house number and telephone
	 * @return a book
	 */
	public Booking createBooking(RuralHouse ruralHouse, Date firstDate, Date lastDate, String bookTelephoneNumber)
			throws OfferCanNotBeBooked {
		ruralHouses=null;
		owners=null;
		return hm.createBooking(ruralHouse,firstDate,lastDate,bookTelephoneNumber);
	}


	/**
	 * This method existing  owners 
	 * 
	 */
	public Set<Owner> getOwners() throws 
			Exception {
		
		if (owners!=null) { System.out.println("Owners obtained directly from business logic layer");
							return owners; }
		else return owners= hm.getOwners();
	}
		
	public Set<RuralHouse> getAllRuralHouses() throws 
	Exception {
		
		if (ruralHouses!=null) { System.out.println("RuralHouses obtained directly from business logic layer");
								 return ruralHouses; }
		else return ruralHouses= hm.getAllRuralHouses();

	}
	
	public void close() {
		hm.close();
	}

	}

