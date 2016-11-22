package beans;

import java.util.Date;
import java.util.Set;
import java.text.SimpleDateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import businessLogic.ApplicationFacadeInterface;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;


public class SetAvailability {
	
    private Set<Owner> owners;
    private Set<RuralHouse> ruralHouse;
    private Date firtDay;
    private Date lastDay;
    private float price;
    private RuralHouse house;
    
    private Offer oferta;
    private Owner owner;
    
    
    ApplicationFacadeInterface facade;
    public SetAvailability() throws Exception{
    	facade = MasterBean.getInstance();
    }
    
    public Offer crearOferta() throws Exception{
    	try{
    		//ApplicationFacadeInterface facade = (ApplicationFacadeInterface) Naming.lookup("rmi://localhost:1099/RuralHouses");
    		oferta = facade.createOffer(house, firtDay, lastDay, price);
    		System.out.println("Oferta creada!!");
    		return oferta;
    	}catch (Exception e){
    		System.out.println("No se a podido crear la oferta");
    		return null;
    	}
    }

	public Set<Owner> getOwners() throws Exception {
		//ApplicationFacadeInterface facade = (ApplicationFacadeInterface) Naming.lookup("rmi://localhost:1099/RuralHouses");
    	this.owners = facade.getOwners();
    	return owners;
	}

	public void setOwners(Set<Owner> owners) {
		this.owners = owners;
	}
	
	public String casasOwner(){
		if(owner!=null){
			this.ruralHouse =  owner.getRuralHouses();
			return "ok";
		}
		else{
			return "";
		}
	}
	
	public Set<RuralHouse> getRuralHouse() throws Exception {
    	return  ruralHouse;
	}

	public void setRuralHouse(Set<RuralHouse> ruralHouse) throws Exception{
		this.ruralHouse =  ruralHouse;
	}
	
	public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

	public Date getFirtDay() {
		return firtDay;
	}

	public void setFirtDay(Date firtDay) {
		this.firtDay = firtDay;
	}

	public Date getLastDay() {
		return lastDay;
	}

	public void setLastDay(Date lastDay) {
		this.lastDay = lastDay;
	}
	

	public RuralHouse getHouse() {
		return house;
	}

	public void setHouse(RuralHouse house) {
		this.house = house;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
	
	
}
