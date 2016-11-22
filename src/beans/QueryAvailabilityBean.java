package beans;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

import businessLogic.ApplicationFacadeInterface;
import domain.Offer;
import domain.RuralHouse;
 
@ManagedBean
public class QueryAvailabilityBean{

    private Date date;
    private Set<RuralHouse> ruralHouses;
    private Set<Offer> ofertas;
    
    private Integer noches=0;

	//Manejo de la oferta
    private int offerNumber;
    private RuralHouse ruralHouse;
	private Date firstDay; 
    private Date lastDay;
    private double price;
    
    ApplicationFacadeInterface facade;
    public QueryAvailabilityBean() throws Exception{
    	facade = MasterBean.getInstance();
    }
    
   
	////////////////////////////////////////////////////////////////////////////////////////////

	public List<Offer> getOfertas() throws Exception{
		List<Offer> list = null;
		if(date!=null){
			Date FF = sumarDiasFecha(this.date, this.noches);
			this.ofertas = ruralHouse.getOffers(date, FF);
			System.out.println(ofertas.size());
			list = new ArrayList<Offer>(ofertas);
			System.out.println(list.size());
		}
		return list;
	}

	public void setOfertas(Set<Offer> ofertas) {
		this.ofertas = ofertas;
	}

	public Set<RuralHouse> getRuralHouses() throws Exception {
		//ApplicationFacadeInterface facade = (ApplicationFacadeInterface) Naming.lookup("rmi://localhost:1099/RuralHouses");
    	ruralHouses =  facade.getAllRuralHouses();
    	return ruralHouses;
	}

	public void setRuralHouses(Set<RuralHouse> ruralHouse) throws Exception{
		this.ruralHouses = ruralHouse;
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
    
    
    public int getNoches() {
		return noches;
	}

	public void setNoches(int noches) {
		this.noches = noches;
	}

	public int getOfferNumber() {
		return offerNumber;
	}

	public void setOfferNumber(int offerNumber) {
		this.offerNumber = offerNumber;
	}

	public Date getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(Date firstDay) {
		this.firstDay = firstDay;
	}

	public Date getLastDay() {
		return lastDay;
	}

	public void setLastDay(Date lastDay) {
		this.lastDay = lastDay;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public RuralHouse getRuralHouse(){
		return ruralHouse;
	}

	public void setRuralHouse(RuralHouse ruralHouse) {
		this.ruralHouse = ruralHouse;
	}

	//Suma los días recibidos a la fecha  
     public Date sumarDiasFecha(Date fecha, int dias){
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(fecha); 
          calendar.add(Calendar.DAY_OF_YEAR, dias);  
          return calendar.getTime(); 

     }
     
     

    
    
}