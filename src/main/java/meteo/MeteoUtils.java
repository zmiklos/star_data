package meteo;



public class MeteoUtils {
	
	/**
	 * permet de convertir une temperature en Celsius en Kelvin et inversement
	 * @param pDegre temperature a convertir
	 * @param pConversion true pour convertir le Celsius en Kelvin et false inversement
	 * @return temperature converties
	 */
	/**
	 * permet de convertir une temperature de Celsius en une autre unite et inversement
	 * @param pDegre temperature a convertir
	 * @param tempUnit true pour convertir le Celsius en une autre unite et false inversement
	 * @param pConversion unite de la temperature 
	 * @return
	 */
	public static double convertCelsiusToOtherUnit(double pDegre,String tempUnit,Boolean pConversion){
		if(tempUnit.equalsIgnoreCase("k")){
			if(pConversion){
				pDegre=pDegre+273.15;
			}else{
				pDegre=pDegre-273.15;
			}
		}else if(tempUnit.equalsIgnoreCase("f")){
			if(pConversion){
				pDegre=(pDegre*9/5)+32;
			}else{
				pDegre=(pDegre-32)*5/9;
			}
		}
//		switch (tempUnit) {
//		case "k":
//			if(pConversion){
//				pDegre=pDegre+273.15;
//			}else{
//				pDegre=pDegre-273.15;
//			}
//			break;
//		case "f":
//			if(pConversion){
//				pDegre=(pDegre*9/5)+32;
//			}else{
//				pDegre=(pDegre-32)*5/9;
//			}
//			break;
//		default:
//			break;
//		}

		return pDegre;
	}
	
	
	
}
