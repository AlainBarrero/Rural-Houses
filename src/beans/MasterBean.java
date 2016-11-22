package beans;

import businessLogic.ApplicationFacadeInterface;
import businessLogic.FacadeImplementation;

public class MasterBean {
	
	//private static ApplicationFacadeInterface facade;
	private static ApplicationFacadeInterface facade;
	
	private MasterBean(){
		try{
			//facade = (ApplicationFacadeInterface) Naming.lookup("rmi://localhost:1099/RuralHouses");
			facade = new FacadeImplementation();
		}catch (Exception e){
			System.out.println("Hubo un problema");
		}
	}

	public static ApplicationFacadeInterface getInstance() throws Exception{
		if(facade==null){
			new MasterBean();
			return facade;
		}
		return facade;
	}
}
