package beans;
 

import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

import businessLogic.ApplicationFacadeInterface;
import domain.Booking;
import domain.RuralHouse;
 
@ManagedBean
public class BookRuralHouseBean {

    private Date date;
    private Set<RuralHouse> ruralHouse;
    private RuralHouse house;
    private int noches;
    private String tlfno;
    
    private Booking book;
    
    private int bookNumber;
    private float precio;
    
    
    ApplicationFacadeInterface facade;
    public BookRuralHouseBean() throws Exception{
    	facade = MasterBean.getInstance();
    }
    
    public String createBook() throws Exception{
    	if(date!=null){
    		Date last = sumarDiasFecha(date, noches);
    		
    		//ApplicationFacadeInterface facade = (ApplicationFacadeInterface) Naming.lookup("rmi://localhost:1099/RuralHouses");
    		book = facade.createBooking(house, date, last, tlfno);
    		if(book!=null){
    			System.out.println("Reserva creada!!");
    			return "";
    			//return "act5";
    		}else
    			System.out.println("No se ha podido realizar la reserva!");
    			return null;
    	}	
    	System.out.println("error");
    	return null;
    	
    }

	public Set<RuralHouse> getRuralHouse() throws Exception {
		//ApplicationFacadeInterface facade = (ApplicationFacadeInterface) Naming.lookup("rmi://localhost:1099/RuralHouses");
    	this.ruralHouse =  facade.getAllRuralHouses();
    	return ruralHouse;
	}

	public void setRuralHouse(Set<RuralHouse> ruralHouse){
		this.ruralHouse = ruralHouse;
	}
     
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

 
    public Date getDate() {
        return date;
    }
 
    public void setDate(Date date) {
        this.date = date;
    }

	public RuralHouse getHouse() {
		return house;
	}

	public void setHouse(RuralHouse house) {
		this.house = house;
	}

	public int getNoches() {
		return noches;
	}

	public void setNoches(int noches) {
		this.noches = noches;
	}

	public String getTlfno() {
		return tlfno;
	}

	public void setTlfno(String tlfno) {
		this.tlfno = tlfno;
	}
    
	 //Suma los días recibidos a la fecha  
    public Date sumarDiasFecha(Date fecha, int dias){
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(fecha); 
         calendar.add(Calendar.DAY_OF_YEAR, dias);  
         return calendar.getTime(); 

    }
	
	public int getBookNumber() {
		bookNumber = book.getBookingNumber();
		return bookNumber;
	}

	public void setBookNumber(int bookNumber) {
		this.bookNumber = bookNumber;
	}

	public String obtenerCuenta(){
		return book.getOffer().getRuralHouse().getOwner().getBankAccount();
	}

	public float getPrecio() {
		precio = book.getPrice();
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}
    
	public float obtenerDeposito(){
		return book.getPrice()*(float)0.2;
	}
    
    
}