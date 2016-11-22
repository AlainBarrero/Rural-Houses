package dataAccess;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.Session;

import domain.Booking;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import exceptions.OfferCanNotBeBooked;
import exceptions.OverlappingOfferExists;

public class HibernateManager {
	
	private static HibernateManager hibernateManager=null;

	public HibernateManager(){}
	
	public static HibernateManager getInstance(){
		if (hibernateManager==null)
			hibernateManager=new HibernateManager();
		return hibernateManager;
	}
	
	public static void initializeDB(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		 Owner jon = new Owner("Jon", "Jonlog", "passJon","12345677");
		 Owner alfredo = new Owner("Alfredo","AlfredoLog", "passAlfredo","77654321");
		 Owner jesus = new Owner("Jesús", "Jesuslog", "passJesus","12344321");
		 Owner josean = new Owner("Josean","JoseanLog", "passJosean","43211234");
		 
		 RuralHouse RH1 = new RuralHouse(1, jon, "Sol", "Beasain");
		 RuralHouse RH2 = new RuralHouse(2, alfredo, "Lluvia", "Beasain");
		 RuralHouse RH3 = new RuralHouse(3, jesus, "Nubes", "Beasain");
		 RuralHouse RH4 = new RuralHouse(4, josean, "Resol", "Beasain");
		 
		 session.save(RH1);
		 session.save(RH2);
		 session.save(RH3);
		 session.save(RH4);
		 
		 session.save(jon);
		 session.save(alfredo);
		 session.save(jesus);
		 session.save(josean);
		 session.getTransaction().commit();
	}
	
	
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price){
		try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List result = session.createQuery
					("from RuralHouse where housenumber='"+ruralHouse.getHouseNumber()+"'").list();
			RuralHouse rh = (RuralHouse)result.get(0);
			Offer o=rh.createOffer(ruralHouse.getHouseNumber(),firstDay, lastDay, price);
			session.save(o);
			session.getTransaction().commit();
			return o;
		}catch(Exception e){
			System.out.println("No se pudo crear la oferta!");
			return null;
		}
	}
	
	public Booking createBooking(RuralHouse ruralHouse, Date firstDate, Date lastDate, String bookTelephoneNumber)
			throws OfferCanNotBeBooked{
		try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List result = session.createQuery
					("from RuralHouse where housenumber='"+ruralHouse.getHouseNumber()+"' "
					 + "and description='"+ruralHouse.getDescription()+"' and city= '"+ruralHouse.getCity()+"'").list();
			RuralHouse rh=(RuralHouse)result.get(0);
			Offer offer = rh.findOffer(firstDate, lastDate);
			if (offer!=null) {
				Booking book = offer.createBooking(offer.getOfferNumber(), bookTelephoneNumber);
				session.save(book);
				session.delete(offer);
				session.getTransaction().commit();
				return offer.getBooking();
			}	 
			return null;
			
		}catch(Exception e){
			System.out.println("No se pudo crear la reserva!");
			return null;
		}
	}
	
	public Set<Owner> getOwners() {
		try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List owner = session.createQuery("from Owner").list();
			session.getTransaction().commit();
			Set<Owner> set = new HashSet<Owner>(owner);
			return  set;
		}finally{
		}
	} 
	
	public Set<RuralHouse> getAllRuralHouses(){
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List ruralhouse = session.createQuery("from RuralHouse").list();
			session.getTransaction().commit();
			Set<RuralHouse> set = new HashSet<RuralHouse>(ruralhouse);
			return  set;
	     }finally{
	     }
	}
	
	public boolean existsOverlappingOffer(RuralHouse rh,Date firstDay, Date lastDay) throws RemoteException, OverlappingOfferExists{
		 try{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List result = session.createQuery
						("from RuralHouse where housenumber='"+rh.getHouseNumber()+"'").list(); 
			RuralHouse rhn = (RuralHouse)result.get(0);
			if(rhn.overlapsWith(firstDay, lastDay)!=null)throw new OverlappingOfferExists();
			else return false;
	     }finally{
	     }
	}
	
	public void deleteDB() {
		 try{
			 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			 session.beginTransaction();
			 List owner = session.createQuery("from Owner").list(); 
			 for(int i=0; i<owner.size(); i++){
				 session.delete(owner.get(i));
			 }
			 session.getTransaction().commit();
	     }finally{
	     }
	}
	
	public void close(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.close();
		System.out.println("DataBase closed");
	}
	
	///////////////////////////////MAIN//////////////////////////////////////////////
	public static void main(String[] args) throws OfferCanNotBeBooked, ParseException{
		HibernateManager hm = new HibernateManager();
		
		System.out.println("Creación de owners");
		initializeDB();
		System.out.println("Finalizado");
		
		System.out.println("Obeniendo las casas:");
		Set<RuralHouse> ruralHouse = hm.getAllRuralHouses();
		Iterator<RuralHouse> itr = ruralHouse.iterator();
		while(itr.hasNext()){
			RuralHouse rh = itr.next();
			System.out.println(rh.toString());
		}
		
		System.out.println("Obeniendo los propietarios:");
		Set<Owner>owners = hm.getOwners();
		Iterator<Owner> ito = owners.iterator();
		while(ito.hasNext()){
			Owner o = ito.next();
			System.out.println(o.toString());
		}
		
		System.out.println("Creando la oferta:");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Owner Alain = new Owner("Alain", "Alainlog", "passAlain","94300222");
		RuralHouse RH1 = new RuralHouse(5, Alain, "Sol", "Beasain");
		session.save(Alain);
		session.save(RH1);
		session.getTransaction().commit();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringFechaConHora = "2014-09-15 15:03:23";
		Date d1 = sdf.parse(stringFechaConHora);
		
		SimpleDateFormat sdfa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringFechaConHoraa = "2014-09-20 15:03:23";
		Date d2 = sdfa.parse(stringFechaConHoraa);

		Offer o = hm.createOffer(RH1, d1, d2, 150);
		
		//System.out.println("Creando la reserva:");
		//hm.createBooking(RH1, d1, d2, "943027794");
	}
}
